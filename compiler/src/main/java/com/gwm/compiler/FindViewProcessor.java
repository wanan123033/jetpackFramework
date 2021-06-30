package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.IOCWork;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class FindViewProcessor extends BaseProcessor {
    private static String packageName = "com.app.layout";
    private static String classNameR = "R";
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(IOCWork.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.CLASS) {
                String values = element.getAnnotation(IOCWork.class).value();
                System.out.println("----------");
                TypeSpec.Builder datainflater = TypeSpec.classBuilder("LayoutInflaterUtils").addModifiers(Modifier.PUBLIC);
                datainflater.addAnnotation(AnnotationSpec.builder(com.gwm.annotation.router.AutoService.class).addMember("value","$T.class",ClassName.get("com.jetpackframework.ioc","LayoutUtil")).build());
                FieldSpec layouts = FieldSpec.builder(Map.class, "layouts", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).build();
                datainflater.addField(layouts);
                datainflater.addSuperinterface(ClassName.get("com.jetpackframework.ioc", "LayoutUtil"));
                FieldSpec layoutArr = FieldSpec.builder(HashMap.class, "layoutArr", Modifier.PRIVATE).build();
                datainflater.addField(layoutArr);
                CodeBlock.Builder block = CodeBlock.builder();
                block.add(CodeBlock.builder().add("layouts = new HashMap<Integer,String>();\n" +
                        "layouts.clear();\n").build());
                packageName = "com." + values + ".layout";
                File layoutDir = new File(values + "/src/main/res/layout");
                File build = new File(values + "/build.gradle");
                try {
                    FileInputStream fileReader = new FileInputStream(build);
                    byte[] buf = new byte[128];
                    fileReader.read(buf, 0, 128);
                    String string = new String(buf);
                    if (string.contains("com.android.application")) {
                        classNameR = "R2";
                    } else if (string.contains("com.android.library")) {
                        classNameR = "R2";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (layoutDir.isDirectory()) {
                    File[] files = layoutDir.listFiles();
                    for (File file : files) {
                        String name = file.getName();
                        TypeSpec.Builder clazz = TypeSpec.classBuilder(name.substring(0, name.indexOf("."))).addModifiers(Modifier.PUBLIC);
                        MethodSpec.Builder getLayoutId = MethodSpec.methodBuilder("getLayoutId")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(int.class);
                        getLayoutId.addCode("return this.layoutId;\n");
                        clazz.addMethod(getLayoutId.build());

                        clazz.addField(FieldSpec.builder(int.class,"layoutId",Modifier.PRIVATE).build());

                        TypeVariableName mTypeVariable = TypeVariableName.get("View");
                        ParameterizedTypeName mListTypeName = ParameterizedTypeName.get(ClassName.get("android.util","SparseArray"), mTypeVariable);
                        clazz.addField(FieldSpec.builder(mListTypeName,"views",Modifier.PUBLIC).build());

                        MethodSpec.Builder builder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
                        builder.addCode("this.layoutId = $T.layout."+name.substring(0, name.indexOf("."))+";\n",ClassName.get(getRPackageName(values), "R"));
                        builder.addCode("views = new SparseArray<View>();\n");
                        clazz.addMethod(builder.build());

                        Map<String, ClassName> views = getViews(file);
                        Set<String> keySet = views.keySet();
                        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("bindView")
                                .addParameter(ParameterSpec.builder(ClassName.get("android.view", "View"), "view").build())
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC);
                        clazz.addSuperinterface(ClassName.get("com.jetpackframework.ioc", "IViewBind"));
                        List<String> strings = new ArrayList<>();
                        for (String key : keySet) {
                            ClassName className = views.get(key);
                            FieldSpec fieldSpec = FieldSpec.builder(className, key, Modifier.PUBLIC).build();
                            clazz.addField(fieldSpec);
                            //TODO 兼容include标签
                            if (!className.packageName().equals(packageName)) {
                                methodSpec.addCode(key + "=view.findViewById($T.id." + key + ");\n", ClassName.get(getRPackageName(values), "R"));
                                methodSpec.addCode("views.put($T.id."+key+","+key+");\n",ClassName.get(getRPackageName(values), "R2"));
                            } else {
                                methodSpec.addCode(key + "=new " + className.simpleName() + "();\n");
                                methodSpec.addCode(key + ".bindView(" + "view.findViewById($T.id." + key + "));\n", ClassName.get(getRPackageName(values), "R"));
                                strings.add(key);
                            }

                        }
                        clazz.addMethod(methodSpec.build());

                        MethodSpec.Builder findViewById = MethodSpec.methodBuilder("findViewById")
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(int.class,"resId")
                                .returns(ClassName.get("android.view","View"));
                        findViewById.addCode("View view = views.get(resId);\n");
                        for (String str : strings) {
                            findViewById.addCode("if (view == null){\n");
                            findViewById.addCode("\tview = "+str+".findViewById(resId);\n");
                            findViewById.addCode("}\n");
                        }
                        findViewById.addCode("return view;\n");
                        clazz.addMethod(findViewById.build());
                        writeClazz(packageName, clazz.build());
                        block.add(CodeBlock.builder().add("layouts.put($T.layout." + name.substring(0, name.indexOf(".")) + ",$S);\n",
                                ClassName.get(getRPackageName(values), classNameR),
                                packageName + "." + name.substring(0, name.indexOf("."))).build());
                    }
                    datainflater.addStaticBlock(block.build());
                    MethodSpec getViewBind = MethodSpec.methodBuilder("getViewBind")
                            .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                            .addParameter(ParameterSpec.builder(int.class, "layoutId").build())
                            .addCode("if(layoutArr == null){\n" +
                                    "      layoutArr = new HashMap<Integer,IViewBind>();\n" +
                                    " }\n" +
                                    " IViewBind bind = (IViewBind)layoutArr.get(layoutId);\n" +
                                    " if(bind == null){\n" +
                                    " try {\n" +
                                    "      bind = (IViewBind) Class.forName((String)layouts.get(layoutId)).newInstance();\n" +
                                    " } catch (IllegalAccessException e) {\n" +
                                    "      e.printStackTrace();\n" +
                                    " } catch (InstantiationException e) {\n" +
                                    "      e.printStackTrace();\n" +
                                    " } catch (ClassNotFoundException e) {\n" +
                                    "      e.printStackTrace();\n" +
                                    " }\n" +
                                    " layoutArr.put(layoutId,bind);\n" +
                                    " }\n" +
                                    " return bind;\n")
                            .addAnnotation(Override.class)
                            .returns(ClassName.get("com.jetpackframework.ioc", "IViewBind"))
                            .build();
                    datainflater.addMethod(getViewBind);

                    MethodSpec clear = MethodSpec.methodBuilder("clear")
                            .addModifiers(Modifier.PUBLIC)
                            .addAnnotation(Override.class)
                            .addCode("layoutArr.clear();\n" +
                                    "layouts.clear();\n")
                            .build();
                    MethodSpec getPackageName = MethodSpec.methodBuilder("getPackageName")
                            .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                            .returns(String.class)
                            .addCode("return \""+getRPackageName(values)+"\";\n").build();
                    datainflater.addMethod(clear);
                    datainflater.addMethod(getPackageName);
                    FieldSpec instance = FieldSpec.builder(ClassName.get(packageName, "LayoutInflaterUtils"), "instance")
                            .addModifiers(Modifier.STATIC, Modifier.PRIVATE)
                            .build();
                    datainflater.addField(instance);
                    MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance").addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED).returns(ClassName.get(packageName, "LayoutInflaterUtils"));
                    getInstance.addCode("if(instance == null){\n\tinstance = new LayoutInflaterUtils();\n}\nreturn instance;\n");
                    datainflater.addMethod(getInstance.build());
                    writeClazz(packageName, datainflater.build());
                }
            }
        }
        return false;
    }
    public Map<String, ClassName> getViews(File layoutFile) {
        Map<String, ClassName> views = new HashMap<>();
        //TODO  解析xml文件  id====类型
        paserXml(views,layoutFile);
        return views;
    }
    private void paserXml(Map<String, ClassName> views, File file) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            org.dom4j.Element rootele = document.getRootElement();
            getMap(views, rootele);
            Iterator it = rootele.elementIterator();
            paserit(views, it);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private Map<String, String> getDatas(File layoutFile) {
        Map<String, String> views = new HashMap<>();
        //TODO  解析xml文件  id====类型
        paserXmlData(views,layoutFile);
        return views;
    }

    private void paserXmlData(Map<String, String> views, File layoutFile) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(layoutFile);
            org.dom4j.Element rootele = document.getRootElement();
            getTextMap(views, rootele);
            Iterator it = rootele.elementIterator();
            paseritData(views, it);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private void paserit(Map<String, ClassName> views, Iterator it) {
        while (it.hasNext()){
            org.dom4j.Element next = (org.dom4j.Element) it.next();
            getMap(views, next);
            if (next.elementIterator().hasNext()){
                paserit(views,next.elementIterator());
            }
        }
    }
    private void paseritData(Map<String, String> views, Iterator it) {
        while (it.hasNext()){
            org.dom4j.Element next = (org.dom4j.Element) it.next();
            getTextMap(views, next);
            if (next.elementIterator().hasNext()){
                paseritData(views,next.elementIterator());
            }
        }
    }


    private void getMap(Map<String, ClassName> views, org.dom4j.Element next) {
        List<Attribute> attributes = next.attributes();
        for (Attribute attr : attributes){
            String name = attr.getName();
            if (name.equals("id")){
                String id = attr.getValue();
                if (id.startsWith("@+id/")) {
                    id = id.substring(5);
                }else if (id.startsWith("@id/")){
                    id = id.substring(4);
                }
                String clazzName = next.getName();
                if (clazzName.equals("com.gwm.databind.data")){
                    continue;
                }
                if (clazzName.equals("include")){
                    String value = next.attributeValue("layout").substring(8);
                    ClassName clazz = ClassName.get(packageName,value);
                    views.put(id,clazz);
                    continue;
                }
                if (clazzName.indexOf(".") != -1){
                    ClassName clazz = ClassName.get(clazzName.substring(0,clazzName.lastIndexOf(".")),clazzName.substring(clazzName.lastIndexOf(".") + 1));
                    views.put(id,clazz);
                }else if (clazzName.equals("WebView")){
                    ClassName clazz = ClassName.get("android.webkit",clazzName);
                    views.put(id,clazz);
                }else if (clazzName.equals("TextureView") || clazzName.equals("SurfaceView")){
                    ClassName clazz = ClassName.get("android.view",clazzName);
                    views.put(id,clazz);
                }else if (!clazzName.equals("View")){
                    ClassName clazz = ClassName.get("android.widget",clazzName);
                    views.put(id,clazz);
                }else {
                    ClassName clazz = ClassName.get("android.view",clazzName);
                    views.put(id,clazz);
                }
            }
        }
    }

    private void getTextMap(Map<String, String> views, org.dom4j.Element next) {
        List<Attribute> attributes = next.attributes();
        for (Attribute attr : attributes){
            String name = attr.getName();
            if (name.equals("id")){
                String id = attr.getValue();
                if (id.startsWith("@+id/")) {
                    id = id.substring(5);
                }else if (id.startsWith("@id/")){
                    id = id.substring(4);
                }
                for (Attribute att : attributes){
                    String dd = att.getName();
                    if (dd.equals("text")){
                        String value = att.getValue();
                        views.put(id,value);
                    }
                }
            }
        }
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new HashSet<>();
        strings.add(IOCWork.class.getCanonicalName());
        return strings;
    }
}
