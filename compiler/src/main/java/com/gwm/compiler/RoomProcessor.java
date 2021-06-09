package com.gwm.compiler;

import androidx.room.Dao;
import androidx.room.Entity;

import com.google.auto.service.AutoService;
import com.gwm.annotation.room.Migration;
import com.gwm.annotation.room.RoomDatabaseOpenHelper;
import com.gwm.annotation.room.RoomFactory;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RoomProcessor extends BaseProcessor {
    private static final String packageName = "com.app.db";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element elements : roundEnvironment.getElementsAnnotatedWith(RoomDatabaseOpenHelper.class)) {
            if (elements.getKind() == ElementKind.CLASS) {
                TypeSpec.Builder appDatabaseClass = TypeSpec.classBuilder("AppDatabase");
                appDatabaseClass.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
                appDatabaseClass.superclass(ClassName.get("androidx.room", "RoomDatabase"));
                AnnotationSpec.Builder database = AnnotationSpec.builder(ClassName.get("androidx.room", "Database"));
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("{");
                Set<? extends Element> eles = roundEnvironment.getElementsAnnotatedWith(Entity.class);
                for (Element element : eles) {
                    if (element.getKind() == ElementKind.CLASS) {
                        String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName() + "";
                        String className = element.getSimpleName() + "";

                        stringBuffer.append(packageName);
                        stringBuffer.append(".");

                        stringBuffer.append(className + ".class,\n");
                    }
                }
                RoomDatabaseOpenHelper dv = elements.getAnnotation(RoomDatabaseOpenHelper.class);
                database.addMember("version", dv.version() + "");
                for (Element element1 : roundEnvironment.getElementsAnnotatedWith(Dao.class)) {
                    if (element1.getKind() == ElementKind.INTERFACE) {
                        String packageName1 = processingEnv.getElementUtils().getPackageOf(element1).getQualifiedName() + "";

                        MethodSpec.Builder getDao = MethodSpec.methodBuilder((element1.getSimpleName() + "").toLowerCase());
                        getDao.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
                        getDao.returns(ClassName.get(packageName1, element1.getSimpleName() + ""));
                        appDatabaseClass.addMethod(getDao.build());

                        MethodSpec.Builder getInstanceDao = MethodSpec.methodBuilder("get" + element1.getSimpleName());
                        getInstanceDao.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
                        getInstanceDao.returns(ClassName.get(packageName1, element1.getSimpleName() + ""));
                        StringBuffer stringBuffer1 = new StringBuffer();
                        stringBuffer1.append("\treturn db." + (element1.getSimpleName() + "").toLowerCase() + "();\n");
                        getInstanceDao.addCode("if(db != null){\n" +
                                stringBuffer1.toString() +
                                "}\n" +
                                "return null;\n");
                        appDatabaseClass.addMethod(getInstanceDao.build());
                    }
                }
//        stringBuffer.delete(stringBuffer.length(),stringBuffer.length());
                stringBuffer.append("}");
                database.addMember("entities", stringBuffer.toString());

                appDatabaseClass.addAnnotation(database.build());

                appDatabaseClass.addField(FieldSpec.builder(
                        ClassName.get(packageName, "AppDatabase"),
                        "db",
                        Modifier.PUBLIC, Modifier.STATIC).build());


                List<String> migrations = new ArrayList<>();
                for (Element element1 : roundEnvironment.getElementsAnnotatedWith(Migration.class)) {
                    if (element1.getKind() == ElementKind.FIELD) {
                        Set<Modifier> modifiers = element1.getModifiers();
                        if (modifiers.contains(Modifier.FINAL) && modifiers.contains(Modifier.PUBLIC) && modifiers.contains(Modifier.STATIC)) {
                            String packageName = processingEnv.getElementUtils().getPackageOf(element1).getQualifiedName() + "";
                            String name = element1.getSimpleName() + "";
                            for (Element element2 : roundEnvironment.getElementsAnnotatedWith(RoomDatabaseOpenHelper.class)) {
                                if (element2.getKind() == ElementKind.CLASS) {
                                    String className = element2.getSimpleName() + "";
                                    migrations.add(".addMigrations(" + packageName + "." + className + "." + name + ")\n");
                                    break;
                                }
                            }
                        }
                    }
                }
                Set<? extends Element> factory = roundEnvironment.getElementsAnnotatedWith(RoomFactory.class);
                for (Element eleFac : factory){
                    if (eleFac.getKind() == ElementKind.FIELD){
                        Set<Modifier> modifiers = eleFac.getModifiers();
                        if (modifiers.contains(Modifier.FINAL) && modifiers.contains(Modifier.PUBLIC) && modifiers.contains(Modifier.STATIC)) {
                            String packageName = processingEnv.getElementUtils().getPackageOf(eleFac).getQualifiedName() + "";
                            String name = eleFac.getSimpleName() + "";
                            for (Element element2 : roundEnvironment.getElementsAnnotatedWith(RoomDatabaseOpenHelper.class)) {
                                if (element2.getKind() == ElementKind.CLASS) {
                                    String className = element2.getSimpleName() + "";
                                    migrations.add(".openHelperFactory(" + packageName + "." + className + "." + name + ")\n");
                                    break;
                                }
                            }
                        }
                    }
                }
                MethodSpec.Builder createDB = MethodSpec.methodBuilder("createDB");
                createDB.addParameter(ClassName.get("android.content", "Context"), "context");
                createDB.addParameter(String.class, "databaseName");
                createDB.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED);

                createDB.returns(void.class);
                StringBuffer code = new StringBuffer();
                code.append("if(db != null) \n\t return; \n");
                code.append("db = androidx.room.Room.databaseBuilder(context,AppDatabase.class,databaseName)\n");
                code.append("\t.allowMainThreadQueries()\n");
                for (int j = 0; j < migrations.size(); j++) {
                    code.append("\t" + migrations.get(j));
                }
                code.append("\t.build();\n");
                createDB.addCode(code.toString());
                appDatabaseClass.addMethod(createDB.build());

                MethodSpec.Builder execSql = MethodSpec.methodBuilder("execSql");
                execSql.addModifiers(Modifier.PUBLIC,Modifier.STATIC);
                execSql.addParameter(String.class, "sql");
                execSql.addCode("db.getOpenHelper().getWritableDatabase().execSQL(sql);\n");
                appDatabaseClass.addMethod(execSql.build());

                MethodSpec.Builder execCursor = MethodSpec.methodBuilder("execCursor");
                execCursor.addModifiers(Modifier.PUBLIC,Modifier.STATIC);
                execCursor.addParameter(String.class, "sql");
                execCursor.returns(ClassName.get("android.database","Cursor"));
                execCursor.addCode("return db.getOpenHelper().getReadableDatabase().query(sql);\n");
                appDatabaseClass.addMethod(execCursor.build());
                writeClazz(packageName, appDatabaseClass.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Entity.class.getCanonicalName());
        stringSet.add(Dao.class.getCanonicalName());
        stringSet.add(Migration.class.getCanonicalName());
        stringSet.add(RoomDatabaseOpenHelper.class.getCanonicalName());
        return stringSet;
    }
}