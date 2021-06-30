package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.Module;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
@AutoService(Processor.class)
public class ARouterProcessor extends BaseProcessor{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Module.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                Module module = element.getAnnotation(Module.class);
                TypeSpec.Builder fileRouter = TypeSpec.classBuilder(captureName(module.value()) + "Router").addSuperinterface(ClassName.get("com.jetpackframework.arouter", "RouterInitialization"));
                fileRouter.addModifiers(Modifier.PUBLIC);
                fileRouter.addAnnotation(AnnotationSpec.builder(com.gwm.annotation.router.AutoService.class).addMember("value","$T.class",ClassName.get("com.jetpackframework.arouter","RouterInitialization")).build());
                MethodSpec.Builder onInit = MethodSpec.methodBuilder("onInit")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Map.class,"routerMap");
                Set<? extends Element> arouterElements = roundEnv.getElementsAnnotatedWith(ARouter.class);
                for (Element ele : arouterElements){
                    ARouter aRouter = ele.getAnnotation(ARouter.class);
                    String simple = processingEnv.getElementUtils().getPackageOf(ele).getQualifiedName().toString()+"."+ele.getSimpleName().toString();
                    onInit.addCode("routerMap.put($S,$S);\n",module.value()+"://${packageName}/"+aRouter.value(),simple);
                }
                fileRouter.addMethod(onInit.build());

                MethodSpec.Builder notFound = MethodSpec.methodBuilder("notFound").addModifiers(Modifier.PUBLIC);
                notFound.addAnnotation(Override.class);
                notFound.addParameter(ClassName.get("android.content","Context"),"context");
                fileRouter.addMethod(notFound.build());

                MethodSpec.Builder onError = MethodSpec.methodBuilder("onError").addModifiers(Modifier.PUBLIC);
                onError.addAnnotation(Override.class);
                onError.addParameter(Exception.class,"e");
                fileRouter.addMethod(onError.build());
                writeClazz("com."+module.value()+".arouter", fileRouter.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Module.class.getCanonicalName());
        stringSet.add(ARouter.class.getCanonicalName());
        return stringSet;
    }
}
