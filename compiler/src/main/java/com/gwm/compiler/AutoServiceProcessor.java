package com.gwm.compiler;

import com.gwm.annotation.router.AutoService;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

@com.google.auto.service.AutoService(Processor.class)
public class AutoServiceProcessor extends BaseProcessor{
    private HashMap<String, Set<String>> providers = new HashMap<>();
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoService.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.CLASS){
                List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
                for (AnnotationMirror mirror : mirrors){

                    Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
                    Set<? extends ExecutableElement> keySet = elementValues.keySet();
                    for (ExecutableElement key : keySet){
                        AnnotationValue value = elementValues.get(key);
                        String valueString = value.toString();

                        if (!valueString.contains(".class")){
                            continue;
                        }
                        valueString = valueString.substring(0,valueString.length() - 6);
                        Set<String> providerValue = providers.get(valueString);
                        String className = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString()+"."+element.getSimpleName().toString();
                        if (providerValue == null){
                            providerValue = new HashSet<>();
                        }else if (providerValue.contains(className)){
                             continue;
                        }
                        providerValue.add(className);
                        providers.put(valueString,providerValue);
                    }
                }
            }
        }
        try {
            File dir = new File("E:/AndroidPro/jetpackframework/merga/src/main/resources/META-INF/services/");
            if (!dir.exists())
                dir.mkdirs();
            for (String fileName : providers.keySet()){
                File file = new File(dir,fileName);
                ByteArrayOutputStream stringBuilder = new ByteArrayOutputStream();
                if (!file.exists()){
                    file.createNewFile();
                }else {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = fileInputStream.read(buf)) != -1){
                        stringBuilder.write(buf,0,len);
                    }
                    fileInputStream.close();
                }
                Set<String> cacheString = providers.get(fileName);
                String s = new String(stringBuilder.toByteArray());
                String[] split = s.split("\n");
                for (String s1 : split){
                    cacheString.add(s1);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                if (fileName.equals("com.jetpackframework.applicationdelegate.ApplicationDelegate")){
                    cacheString.add("com.jetpackframework.ApplicationDelegate");
                }
                for (String ssss : cacheString){
                    byte[] bytes = (ssss+"\n").getBytes();
                    fileOutputStream.write(bytes,0,bytes.length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                stringBuilder.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new HashSet<>();
        strings.add(AutoService.class.getCanonicalName());
        return strings;
    }
}
