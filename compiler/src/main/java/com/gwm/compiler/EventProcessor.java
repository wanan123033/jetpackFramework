package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnCheckedChange;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.layout.OnItemClick;
import com.gwm.annotation.layout.OnItemSelected;
import com.gwm.annotation.layout.OnLongClick;
import com.gwm.annotation.layout.OnMultiClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
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
public class EventProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> with = roundEnvironment.getElementsAnnotatedWith(IOCWork.class);
        for (Element ele : with) {
            if (ele.getKind() == ElementKind.CLASS) {
                IOCWork iocWork = ele.getAnnotation(IOCWork.class);
                String value = iocWork.value();
                TypeSpec.Builder eventUtil = TypeSpec.classBuilder("EventInflaterUtils").addModifiers(Modifier.PUBLIC);
                eventUtil.addSuperinterface(ClassName.get("com.jetpackframework.ioc","EventClassUtil"));
                FieldSpec eventArr = FieldSpec.builder(ClassName.get("java.util","HashMap"),"eventArr", Modifier.PRIVATE).build();
                eventUtil.addField(eventArr);
                Set<? extends Element> layoutEles = roundEnvironment.getElementsAnnotatedWith(Layout.class);
                for (Element layoutEle : layoutEles) {
                    if (layoutEle.getKind() == ElementKind.CLASS) {
                        String simple = layoutEle.getSimpleName().toString();
                        String packageName = processingEnv.getElementUtils().getPackageOf(layoutEle).getQualifiedName().toString();
                        TypeSpec.Builder eventLis = TypeSpec.classBuilder(simple + "Event").addModifiers(Modifier.PUBLIC);
                        eventLis.addSuperinterface(ClassName.get("com.jetpackframework.ioc", "IEventClass"));

                        MethodSpec.Builder bindEvent = MethodSpec.methodBuilder("bindEvent").addModifiers(Modifier.PUBLIC);
                        bindEvent.addParameter(Object.class, "act");
                        bindEvent.addParameter(ClassName.get("com.jetpackframework.ioc", "IViewBind"), "view");
                        bindEvent.addCode("final $T activity = ($T)act;\n", ClassName.get(packageName, simple), ClassName.get(packageName, simple));
                        List<? extends Element> elements = layoutEle.getEnclosedElements();
                        for (Element me : elements) {
                            String methodName = me.getSimpleName().toString();
                            if (me.getAnnotation(OnClick.class) != null) {
                                OnClick onClick = me.getAnnotation(OnClick.class);
                                for (int resId : onClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnClickListener(new View.OnClickListener(){\n" +
                                            "\tpublic void onClick(View view){\n" +
                                            "\t\tactivity." + methodName + "(view);\n" +
                                            "\t}\n" +
                                            "});\n",ClassName.get("android.view","View"));
                                }
                            }
                            if (me.getAnnotation(OnItemClick.class) != null) {
                                OnItemClick onClick = me.getAnnotation(OnItemClick.class);
                                for (int resId : onClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnItemClickListener(new AdapterView.OnItemClickListener(){\n" +
                                            "\tpublic void onItemClick(AdapterView<?> parent, View view, int position, long id){\n" +
                                            "\t\tactivity." + methodName + "(parent,position);\n" +
                                            "\t}\n" +
                                            "});\n", ClassName.get("android.widget", "AdapterView"));
                                }
                            }
                            if (me.getAnnotation(OnItemSelected.class) != null) {
                                OnItemSelected onClick = me.getAnnotation(OnItemSelected.class);
                                for (int resId : onClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){\n" +
                                            "\tpublic void onItemSelected(AdapterView<?> parent, View view, int position, long id) {\n" +
                                            "\t\tactivity." + methodName + "(parent,position);\n" +
                                            "\t}\n" +
                                            "public void onNothingSelected(AdapterView<?> parent){\n" +
                                            "}\n" +
                                            "});\n", ClassName.get("android.widget", "AdapterView"));
                                }
                            }
                            if (me.getAnnotation(OnCheckedChange.class) != null) {
                                OnCheckedChange onClick = me.getAnnotation(OnCheckedChange.class);
                                for (int resId : onClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){\n" +
                                            "\tpublic void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {\n" +
                                            "\t\tactivity." + methodName + "(buttonView,isChecked);\n" +
                                            "\t}\n" +
                                            "});\n", ClassName.get("android.widget", "CompoundButton"));
                                }
                            }
                            if (me.getAnnotation(OnMultiClick.class) != null) {
                                OnMultiClick onMultiClick = me.getAnnotation(OnMultiClick.class);
                                for (int resId : onMultiClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnClickListener(new com.gwm.android.OnMultiClickListener(){\n" +
                                            "\tpublic void onMultiClick(View view){\n" +
                                            "\t\tactivity." + methodName + "(view);\n" +
                                            "\t}\n" +
                                            "});\n",ClassName.get("android.view","View"));
                                }
                            }
                            if (me.getAnnotation(OnLongClick.class) != null) {
                                OnLongClick onMultiClick = me.getAnnotation(OnLongClick.class);
                                for (int resId : onMultiClick.value()) {
                                    bindEvent.addCode("(($T)view.findViewById(" + resId + ")).setOnLongClickListener(new View.OnLongClickListener(){\n" +
                                            "\tpublic boolean onLongClick(View v){\n" +
                                            "\t\treturn activity." + methodName + "(v);\n" +
                                            "\t}\n" +
                                            "});\n",ClassName.get("android.view","View"));
                                }
                            }
                        }
                        eventLis.addMethod(bindEvent.build());
                        writeClazz("com."+value+".layoutevent", eventLis.build());
                    }
                }
                MethodSpec getViewBind = MethodSpec.methodBuilder("getEventClass")
                        .addModifiers(Modifier.PUBLIC,Modifier.SYNCHRONIZED)
                        .addParameter(ParameterSpec.builder(Class.class,"simpleName").build())
                        .addCode("if(eventArr == null){\n" +
                                "      eventArr = new HashMap<String,$T>();\n" +
                                " }\n" +
                                " IEventClass bind = (IEventClass)eventArr.get(simpleName.getPackage().getName()+\".\"+simpleName.getSimpleName());\n" +
                                " if(bind == null){\n" +
                                "\t try {\n" +
                                "\t      bind = (IEventClass) Class.forName(\"com."+value+".layoutevent.\"+simpleName.getSimpleName()+\"Event\").newInstance();\n" +
                                "\t } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {\n" +
                                "\t      e.printStackTrace();\n" +
                                "\t }\n" +
                                "\teventArr.put(simpleName.getPackage().getName()+\".\"+simpleName.getSimpleName(),bind);\n" +
                                " }\n" +
                                " return bind;\n",ClassName.get("com.jetpackframework.ioc","IEventClass"))
                        .addAnnotation(Override.class)
                        .returns(ClassName.get("com.jetpackframework.ioc","IEventClass"))
                        .build();
                eventUtil.addMethod(getViewBind);
                MethodSpec clear = MethodSpec.methodBuilder("clear")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addCode("eventArr.clear();\n")
                        .build();
                eventUtil.addMethod(clear);
                FieldSpec instance = FieldSpec.builder(ClassName.get("com."+value+".layoutevent","EventInflaterUtils"),"instance")
                        .addModifiers(Modifier.STATIC,Modifier.PRIVATE)
                        .build();
                eventUtil.addField(instance);
                MethodSpec contrutor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
                eventUtil.addMethod(contrutor);
                MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance").addModifiers(Modifier.PUBLIC,Modifier.STATIC,Modifier.SYNCHRONIZED).returns(ClassName.get("com."+value+".layoutevent","EventInflaterUtils"));
                getInstance.addCode("if(instance == null){\n\tinstance = new EventInflaterUtils();\n}\nreturn instance;\n");
                eventUtil.addMethod(getInstance.build());
                writeClazz("com."+value+".layoutevent",eventUtil.build());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(IOCWork.class.getCanonicalName());
        stringSet.add(Layout.class.getCanonicalName());
        stringSet.add(OnClick.class.getCanonicalName());
        stringSet.add(OnItemClick.class.getCanonicalName());
        stringSet.add(OnMultiClick.class.getCanonicalName());
        stringSet.add(OnCheckedChange.class.getCanonicalName());
        stringSet.add(OnItemSelected.class.getCanonicalName());
        stringSet.add(OnLongClick.class.getCanonicalName());
        return stringSet;
    }
}
