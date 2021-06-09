package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.webview.WebViewCommond;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class WebViewCommondProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (javax.lang.model.element.Element element : roundEnvironment.getElementsAnnotatedWith(IOCWork.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeSpec.Builder webAppInterface = TypeSpec.classBuilder("WebAppInterface");
                webAppInterface.superclass(ClassName.get("com.jetpackframework.webview","WebApp"));
                webAppInterface.addModifiers(Modifier.PUBLIC,Modifier.FINAL);
                MethodSpec.Builder init = MethodSpec.methodBuilder("initWebView");
                init.addModifiers(Modifier.PUBLIC);
                init.addAnnotation(Override.class);
                for (javax.lang.model.element.Element commondEle : roundEnvironment.getElementsAnnotatedWith(WebViewCommond.class)) {
                    if (commondEle.getKind() == ElementKind.CLASS) {
                        String packageName = processingEnv.getElementUtils().getPackageOf(commondEle).getQualifiedName().toString();
                        init.addCode("addCommond(new $T());\n",ClassName.get(packageName,commondEle.getSimpleName().toString()));
                    }
                }
                webAppInterface.addMethod(init.build());
                writeClazz("com.app.webview",webAppInterface.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
//        stringSet.add(WebViewCommond.class.getCanonicalName());
//        stringSet.add(IOCWork.class.getCanonicalName());
        return stringSet;
    }
}
