package com.gwm.compiler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.room.ContentProvider;
import com.gwm.annotation.room.Provider;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RoomProviderProcessor extends BaseProcessor {
    private static final String pkgName = "com.app.db";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(ContentProvider.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                genAppProviderContractClazz(roundEnvironment);

                TypeSpec.Builder contentProvider = TypeSpec.classBuilder("AppProvider");
                contentProvider.addModifiers(Modifier.PUBLIC);
                //生成onCreate()
                genOnCreateMethod(contentProvider,roundEnvironment);
                //生成query()
                genQueryMethod(contentProvider,roundEnvironment);
                //生成insert()
                genInsertMethod(contentProvider,roundEnvironment);
                //生成update()
                genUpdateMethod(contentProvider,roundEnvironment);
                //生成getType()
                genGetTypeMethod(contentProvider);
                //生成delete()
                genDeleteMethod(contentProvider,roundEnvironment);

                //生成static{}
                genStaticBlock(contentProvider,roundEnvironment);

                contentProvider.superclass(ClassName.get("android.content","ContentProvider"));
                writeClazz(pkgName,contentProvider.build());

            }
        }
        return false;
    }

    private void genAppProviderContractClazz(RoundEnvironment roundEnvironment) {
        TypeSpec.Builder AppProviderContract = TypeSpec.classBuilder("AppProviderContract")
                .addModifiers(Modifier.PUBLIC);
        int index = 0;
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                index++;
                String name = element.getAnnotation(Provider.class).uri().replaceAll("/","_").toUpperCase();
                FieldSpec fieldSpec = FieldSpec.builder(int.class,name)
                        .addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL)
                        .initializer(index+"").build();
                AppProviderContract.addField(fieldSpec);
            }
        }
        writeClazz(pkgName,AppProviderContract.build());
    }

    private void genStaticBlock(TypeSpec.Builder contentProvider, RoundEnvironment roundEnvironment) {
        FieldSpec matcher = FieldSpec.builder(ClassName.get("android.content","UriMatcher"),"matcher",Modifier.PRIVATE,Modifier.STATIC).build();
        contentProvider.addField(matcher);
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(IOCWork.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                String value = element.getAnnotation(IOCWork.class).value();
                FieldSpec AUTHORITY = FieldSpec.builder(String.class,"AUTHORITY",Modifier.STATIC,Modifier.PUBLIC,Modifier.FINAL).initializer("$S",getRPackageName(value)).build();
                contentProvider.addField(AUTHORITY);
                break;
            }
        }
        CodeBlock.Builder staticBlock = CodeBlock.builder();
        staticBlock.add("matcher = new UriMatcher(UriMatcher.NO_MATCH);\n");
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                String uri = element.getAnnotation(Provider.class).uri();
                staticBlock.add("matcher.addURI(AUTHORITY,$S,"+"AppProviderContract."+uri.replaceAll("/","_").toUpperCase()+");\n",uri);
            }
        }
        contentProvider.addStaticBlock(staticBlock.build());
    }

    private void genDeleteMethod(TypeSpec.Builder contentProvider, RoundEnvironment roundEnvironment) {
        MethodSpec.Builder delete = MethodSpec.methodBuilder("delete");
        delete.addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(ClassName.get("android.net","Uri"),"uri")
                .addParameter(String.class,"selection")
                .addParameter(String[].class,"selectionArgs");
        delete.addCode("int code = matcher.match(uri);\n");
        delete.addCode("Cursor cursor = null; \n");
        delete.addCode("switch (code){\n");
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                Provider provider = element.getAnnotation(Provider.class);
                Delete delete1 = element.getAnnotation(Delete.class);
                if (delete1 != null){
                    String name = provider.uri().replaceAll("/", "_").toUpperCase();
                    delete.addCode("\tcase AppProviderContract." + name + ":\n");
                    StringBuffer buffer = new StringBuffer();
                    List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                    for (AnnotationMirror am : annotationMirrors) {
                        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = am.getElementValues();
                        Set<? extends ExecutableElement> keySet = elementValues.keySet();
                        for (ExecutableElement key : keySet) {
                            AnnotationValue value = elementValues.get(key);
                            String string = value.toString();
                            if (string.contains(Provider.ParamterType.class.getCanonicalName())) {
                                string = string.substring(1, string.length() - 1);
                                String[] valueArr = string.split("@" + Provider.ParamterType.class.getCanonicalName());
                                int index = 0;
                                for (String valuess : valueArr) {
                                    if (index == 0) {
                                        index++;
                                        continue;
                                    }
                                    index++;
                                    valuess = valuess.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
                                    String nameStr = valuess.substring(valuess.indexOf("\"") + 1, valuess.lastIndexOf("\""));
                                    String typeStr = valuess.substring(valuess.indexOf("type=") + 5, valuess.lastIndexOf(".class"));
                                    buffer.append("new com.google.gson.Gson().fromJson(selectionArgs[0],"+typeStr+".class)");
                                }
                            }
                        }
                    }
                    delete.addCode("\t\tm"+provider.daoClazzName()+"."+element.getSimpleName().toString()+"("+buffer.toString()+");\n");
                    delete.addCode("\t\tbreak;\n");
                }
            }
        }
        delete.addCode("}\n");
        delete.addCode("return 0;\n");
        delete.addAnnotation(Override.class);
        contentProvider.addMethod(delete.build());
    }

    private void genGetTypeMethod(TypeSpec.Builder contentProvider) {
        MethodSpec getType = MethodSpec.methodBuilder("getType")
                .addAnnotation(Override.class)
                .returns(String.class)
                .addParameter(ClassName.get("android.net","Uri"),"uri")
                .addCode("return null;\n")
                .addModifiers(Modifier.PUBLIC)
                .build();
        contentProvider.addMethod(getType);
    }

    private void genUpdateMethod(TypeSpec.Builder contentProvider, RoundEnvironment roundEnvironment) {
        MethodSpec.Builder update = MethodSpec.methodBuilder("update");
        update.addModifiers(Modifier.PUBLIC).returns(int.class)
                .addParameter(ClassName.get("android.net","Uri"),"uri")
                .addParameter(ClassName.get("android.content","ContentValues"),"values")
                .addParameter(String.class,"selection")
                .addParameter(String[].class,"selectionArgs");
        update.addCode("int code = matcher.match(uri);\n");
        update.addCode("Cursor cursor = null; \n");
        update.addCode("switch (code){\n");
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                Provider provider = element.getAnnotation(Provider.class);
                Update update1 = element.getAnnotation(Update.class);
                if (update1 != null){
                    String name = provider.uri().replaceAll("/", "_").toUpperCase();
                    update.addCode("\tcase AppProviderContract." + name + ":\n");
                    StringBuffer buffer = new StringBuffer();
                    List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                    for (AnnotationMirror am : annotationMirrors) {
                        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = am.getElementValues();
                        Set<? extends ExecutableElement> keySet = elementValues.keySet();
                        for (ExecutableElement key : keySet) {
                            AnnotationValue value = elementValues.get(key);
                            String string = value.toString();
                            if (string.contains(Provider.ParamterType.class.getCanonicalName())) {
                                string = string.substring(1, string.length() - 1);
                                String[] valueArr = string.split("@" + Provider.ParamterType.class.getCanonicalName());
                                int index = 0;
                                for (String valuess : valueArr) {
                                    if (index == 0) {
                                        index++;
                                        continue;
                                    }
                                    index++;
                                    valuess = valuess.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
                                    String nameStr = valuess.substring(valuess.indexOf("\"") + 1, valuess.lastIndexOf("\""));
                                    String typeStr = valuess.substring(valuess.indexOf("type=") + 5, valuess.lastIndexOf(".class"));
                                    buffer.append("new "+typeStr+"(values,false)");
                                }
                            }
                        }
                    }
                    update.addCode("\t\tm"+provider.daoClazzName()+"."+element.getSimpleName().toString()+"("+buffer.toString()+");\n");
                    update.addCode("\t\tbreak;\n");
                }
            }
        }
        update.addCode("}\n");
        update.addCode("return 0;\n");
        update.addAnnotation(Override.class);
        contentProvider.addMethod(update.build());
    }

    private void genInsertMethod(TypeSpec.Builder contentProvider, RoundEnvironment roundEnvironment) {
        MethodSpec.Builder insert = MethodSpec.methodBuilder("insert");
        insert.addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("android.net","Uri"))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.net","Uri"),"uri")
                .addParameter(ClassName.get("android.content","ContentValues"),"values");
        insert.addCode("int code = matcher.match(uri);\n");
        insert.addCode("Cursor cursor = null; \n");
        insert.addCode("switch (code){\n");
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                Provider provider = element.getAnnotation(Provider.class);
                Insert insert1 = element.getAnnotation(Insert.class);
                if (insert1 != null){
                    String name = provider.uri().replaceAll("/", "_").toUpperCase();
                    insert.addCode("\tcase AppProviderContract." + name + ":\n");
                    StringBuffer buffer = new StringBuffer();
                    List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                    for (AnnotationMirror am : annotationMirrors) {
                        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = am.getElementValues();
                        Set<? extends ExecutableElement> keySet = elementValues.keySet();
                        for (ExecutableElement key : keySet) {
                            AnnotationValue value = elementValues.get(key);
                            String string = value.toString();
                            if (string.contains(Provider.ParamterType.class.getCanonicalName())) {
                                string = string.substring(1, string.length() - 1);
                                String[] valueArr = string.split("@" + Provider.ParamterType.class.getCanonicalName());
                                int index = 0;
                                for (String valuess : valueArr) {
                                    if (index == 0) {
                                        index++;
                                        continue;
                                    }
                                    index++;
                                    valuess = valuess.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
                                    String nameStr = valuess.substring(valuess.indexOf("\"") + 1, valuess.lastIndexOf("\""));
                                    String typeStr = valuess.substring(valuess.indexOf("type=") + 5, valuess.lastIndexOf(".class"));
                                    buffer.append("new "+typeStr+"(values,true)");
                                }
                            }
                        }
                    }
                    insert.addCode("\t\tm"+provider.daoClazzName()+"."+element.getSimpleName().toString()+"("+buffer.toString()+");\n");
                    insert.addCode("\t\tbreak;\n");
                }
            }
        }
        insert.addCode("}\n");
        insert.addCode("return null;\n");
        insert.addAnnotation(Override.class);
        contentProvider.addMethod(insert.build());
    }

    private void genQueryMethod(TypeSpec.Builder contentProvider, RoundEnvironment roundEnvironment) {
        MethodSpec.Builder query = MethodSpec.methodBuilder("query");
        query.addAnnotation(Override.class);
        query.addModifiers(Modifier.PUBLIC);
        query.returns(ClassName.get("android.database","Cursor"));
        query.addParameter(ClassName.get("android.net","Uri"),"uri")
                .addParameter(String[].class,"projection")
                .addParameter(String.class,"selection")
                .addParameter(String[].class,"selectionArgs")
                .addParameter(String.class,"sortOrder");
        query.addCode("int code = matcher.match(uri);\n");
        query.addCode("Cursor cursor = null; \n");
        query.addCode("switch (code){\n");
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Provider.class)) {
            if (element.getKind() == ElementKind.METHOD){
                Provider provider = element.getAnnotation(Provider.class);
                Query query1 = element.getAnnotation(Query.class);
                if (query1 != null) {
                    String name = provider.uri().replaceAll("/", "_").toUpperCase();
                    query.addCode("\tcase AppProviderContract." + name + ":\n");
                    StringBuffer buffer = new StringBuffer();
                    List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                    for (AnnotationMirror am : annotationMirrors) {
                        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = am.getElementValues();
                        Set<? extends ExecutableElement> keySet = elementValues.keySet();
                        for (ExecutableElement key : keySet) {
                            AnnotationValue value = elementValues.get(key);
                            String string = value.toString();
                            if (string.contains(Provider.ParamterType.class.getCanonicalName())) {
                                string = string.substring(1, string.length() - 1);
                                String[] valueArr = string.split("@" + Provider.ParamterType.class.getCanonicalName());
                                int index = 0;
                                for (String valuess : valueArr) {
                                    if (index == 0) {
                                        index++;
                                        continue;
                                    }
                                    index++;
                                    valuess = valuess.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
                                    String nameStr = valuess.substring(valuess.indexOf("\"") + 1, valuess.lastIndexOf("\""));
                                    String typeStr = valuess.substring(valuess.indexOf("type=") + 5, valuess.lastIndexOf(".class"));
                                    if (typeStr.contains("String")) {
                                        buffer.append("selectionArgs[" + (index - 2) + "],");
                                    } else if (typeStr.contains("int")) {
                                        buffer.append("Integer.parseInt(selectionArgs[" + (index - 2) + "]),");
                                    } else if (typeStr.contains("double")) {
                                        buffer.append("Double.parseDouble(selectionArgs[" + (index - 2) + "]),");
                                    } else if (typeStr.contains("byte[]")) {
                                        buffer.append("selectionArgs[" + (index - 2) + "].getBytes(),");
                                    } else if (typeStr.contains("long")) {
                                        buffer.append("Long.parseLong(selectionArgs[" + (index - 2) + "]),");
                                    }
                                }
                            }
                        }
                    }
                    buffer.deleteCharAt(buffer.length() - 1);
                    query.addCode("\t\tcursor = m" + provider.daoClazzName() + "." + element.getSimpleName().toString() + "(" + buffer.toString() + ");\n");
                    query.addCode("\t\tbreak;\n");
                }
            }
        }
        query.addCode("}\n");
        query.addCode("return cursor; \n");
        contentProvider.addMethod(query.build());
    }

    private void genOnCreateMethod(TypeSpec.Builder provider, RoundEnvironment roundEnvironment) {
        MethodSpec.Builder onCreate = MethodSpec.methodBuilder("onCreate");
        onCreate.returns(boolean.class);
        onCreate.addModifiers(Modifier.PUBLIC);
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(Dao.class)) {
            if (element.getKind() == ElementKind.INTERFACE || element.getKind() == ElementKind.CLASS) {
                String packName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
                String clazzName = element.getSimpleName().toString();
                FieldSpec dao = FieldSpec.builder(ClassName.get(packName,clazzName),"m"+clazzName,Modifier.PRIVATE).build();
                provider.addField(dao);
                onCreate.addCode("if(m"+clazzName+" == null){\n");
                onCreate.addCode("\tm"+clazzName+" = com.app.db.AppDatabase.get"+clazzName+"();\n");
                onCreate.addCode("}\n");
            }
        }
        onCreate.addCode("return false;\n");
        onCreate.addAnnotation(Override.class);
        provider.addMethod(onCreate.build());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Provider.class.getCanonicalName());
        stringSet.add(ContentProvider.class.getCanonicalName());
        stringSet.add(IOCWork.class.getCanonicalName());
        stringSet.add(Dao.class.getCanonicalName());
        return stringSet;
    }
}
