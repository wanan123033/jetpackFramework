package com.gwm.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by Administrator on 2019/1/15.
 */

public abstract class BaseProcessor extends AbstractProcessor {
    private Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();

    }
    protected void writeClazz(String packageName,TypeSpec clazz) {
        JavaFile javaFile = JavaFile.builder(packageName,clazz)
                .skipJavaLangImports(true)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected String getRPackageName(String value){
        File layoutDir = new File(value + "/src/main/AndroidManifest.xml");
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(layoutDir);
            Element rootElement = document.getRootElement();
            Attribute attr = rootElement.attribute("package");
            return attr.getValue();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getPackageName(String value){
        File layoutDir = new File(value + "/build/intermediates/bundle_manifest/debug/processDebugManifest/bundle_manifest/AndroidManifest.xml");
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(layoutDir);
            Element element = document.getRootElement().element("application");
            List<Element> elements = element.elements();
            for (Element e : elements){
                if (e.getName().equals("meta-data") && e.attribute("android:name").equals("packageName")){
                    return e.attribute("android:value").getValue();
                }
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        if (cs[0] >= 'a' || cs[0] <= 'z') {
            cs[0] -= 32;
        }
        return String.valueOf(cs);

    }
}