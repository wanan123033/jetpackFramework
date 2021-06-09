package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.router.Merge;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
@AutoService(Processor.class)
public class MergeProcessor extends BaseProcessor{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Merge.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.CLASS){
                TypeSpec.Builder moudleRouter = TypeSpec.classBuilder("MoudleRouter")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ClassName.get("com.jetpackframework.arouter","RouterInitialization"));
                MethodSpec.Builder onInit = MethodSpec.methodBuilder("onInit")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(Map.class, "routerMap");
                Merge merge = element.getAnnotation(Merge.class);
                for (String str : merge.value()){
                    onInit.addCode("new $T().onInit(routerMap);\n",ClassName.get("com."+str+".arouter",captureName(str)+"Router"));
                }

                MethodSpec cur = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();
                moudleRouter.addMethod(onInit.build());
                moudleRouter.addMethod(cur);
                MethodSpec.Builder notFound = MethodSpec.methodBuilder("notFound")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ClassName.get("android.content", "Context"), "context");
                moudleRouter.addMethod(notFound.build());
                MethodSpec.Builder onError = MethodSpec.methodBuilder("onError")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(Exception.class, "e");
                moudleRouter.addMethod(onError.build());
                writeClazz("com.router.merge",moudleRouter.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annos = new HashSet<>();
        annos.add(Merge.class.getCanonicalName());
        return annos;
    }
}
