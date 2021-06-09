package com.gwm.compiler;

import com.google.auto.service.AutoService;
import com.gwm.annotation.http.HttpInterceptor;
import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.retrofit.DNS;
import com.gwm.annotation.retrofit.HttpModel;
import com.gwm.annotation.retrofit.OkHttp;
import com.gwm.annotation.retrofit.ResponseConverter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RetrofitProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(IOCWork.class);
        for (Element element : elements){
            if (element.getKind() == ElementKind.CLASS){
                IOCWork iocWork = element.getAnnotation(IOCWork.class);
                TypeSpec.Builder retrofitUtil = TypeSpec.classBuilder("RetrofitUtil").addModifiers(Modifier.PUBLIC);

                FieldSpec.Builder okhttpclient = FieldSpec.builder(ClassName.get("okhttp3","OkHttpClient"),"okhttpClient",Modifier.PRIVATE);
                Set<? extends Element> okhttpeles = roundEnvironment.getElementsAnnotatedWith(OkHttp.class);
                for (Element okhttpele : okhttpeles){
                    if (okhttpele.getKind() == ElementKind.FIELD){
                        OkHttp annotation = okhttpele.getAnnotation(OkHttp.class);
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("new OkHttpClient.Builder()\n");
                        buffer.append("\t\t\t.connectTimeout("+annotation.connectTimeout()+",$T.SECONDS)\n");
                        buffer.append("\t\t\t.readTimeout("+annotation.readTimeout()+",TimeUnit.SECONDS)\n");
                        buffer.append("\t\t\t.writeTimeout("+annotation.readTimeout()+",TimeUnit.SECONDS)\n");
                        Set<? extends Element> dnsEle = roundEnvironment.getElementsAnnotatedWith(DNS.class);
                        for (Element dns : dnsEle){
                            if (dns.getKind() == ElementKind.CLASS){
                                String packageName = processingEnv.getElementUtils().getPackageOf(dns).getQualifiedName().toString();
                                String simpleName = dns.getSimpleName().toString();
                                buffer.append("\t\t\t.dns(new "+packageName+"."+simpleName+"())\n");
                            }
                        }
                        buffer.append("\t\t\t.retryOnConnectionFailure("+annotation.retryOnConnectionFailure()+")\n");
                        buffer.append("\t\t\t.addInterceptor(new $T("+annotation.retry()+"))\n");
                        buffer.append("\t\t\t.addInterceptor(new $T(new HttpLoggingInterceptor.Logger() {\n" +
                                "\t\t\t\t@Override\n" +
                                "\t\t\t\tpublic void log(String var1) {\n" +
                                "\t\t\t\t\t$T.i(\"TAG\",var1);\n" +
                                "\t\t\t\t}\n" +
                                "\t\t\t}))\n");
                        Set<? extends Element> interceptoreles = roundEnvironment.getElementsAnnotatedWith(HttpInterceptor.class);
                        for (Element interceptorele : interceptoreles){
                            if (interceptorele.getKind() == ElementKind.CLASS){
                                String simpleName = interceptorele.getSimpleName().toString();
                                String packaName = processingEnv.getElementUtils().getPackageOf(interceptorele).getQualifiedName().toString();
                                buffer.append("\t\t\t.addInterceptor(new "+packaName+"."+simpleName+"())\n");

                            }
                        }
                        buffer.append("\t\t\t.build()");
                        okhttpclient.initializer(buffer.toString(),
                                ClassName.get("java.util.concurrent","TimeUnit"),
                                ClassName.get("com.jetpackframework.http","RetryIntercepter"),
                                ClassName.get("com.jetpackframework.http","HttpLoggingInterceptor"),
                                ClassName.get("android.util","Log"));
                    }
                }
                retrofitUtil.addField(okhttpclient.build());
                FieldSpec.Builder retrofit = FieldSpec.builder(ClassName.get("com.jetpackframework.retrofit","Retrofit"),"retrofit",Modifier.PRIVATE);
                retrofitUtil.addField(retrofit.build());
                MethodSpec.Builder create = MethodSpec.methodBuilder("create")
                        .addModifiers(Modifier.PUBLIC);
                Set<? extends Element> retrofitEles = roundEnvironment.getElementsAnnotatedWith(HttpModel.class);
                for (Element retrofitEle : retrofitEles){
                    if (retrofitEle.getKind() == ElementKind.INTERFACE){
                        HttpModel annotation = retrofitEle.getAnnotation(HttpModel.class);
                        create.addCode("if (retrofit == null){\n");
                        create.addCode("\tretrofit = new Retrofit.Builder()\n" +
                                "\t\t.baseUrl(\""+annotation.baseUrl()+"\")\n" +
                                "\t\t.client(okhttpClient)\n");
                        Set<? extends Element> responseConverter = roundEnvironment.getElementsAnnotatedWith(ResponseConverter.class);
                        for (Element ele : responseConverter){
                            ClassName name = ClassName.get(processingEnv.getElementUtils().getPackageOf(ele).getQualifiedName().toString(),ele.getSimpleName().toString());
                            create.addCode("\t\t.addResponseConverter(new $T())\n",name);
                        }
                        create.addCode("\t\t.builder();\n");

                        ClassName className = ClassName.get(processingEnv.getElementUtils().getPackageOf(retrofitEle).getQualifiedName().toString(),retrofitEle.getSimpleName().toString());
                        ParameterizedTypeName ptn = ParameterizedTypeName.get(ClassName.get("com.jetpackframework.retrofit","IRetrofitUtil"),className);
                        retrofitUtil.addSuperinterface(ptn);
                        create.returns(className);
                        FieldSpec.Builder httpApi = FieldSpec.builder(className,"httpApi",Modifier.PRIVATE);
                        retrofitUtil.addField(httpApi.build());
                        create.addCode("}\n");
                        create.addCode("if(httpApi == null){\n");
                        create.addCode("\thttpApi = retrofit.create($T.class);\n",className);
                        create.addCode("}\n");
                        create.addCode("return httpApi;\n",className);

                    }
                }
                retrofitUtil.addMethod(create.build());
                FieldSpec instance = FieldSpec.builder(ClassName.get("com."+iocWork.value()+".retrofit", "RetrofitUtil"), "instance")
                        .addModifiers(Modifier.STATIC, Modifier.PRIVATE)
                        .build();
                retrofitUtil.addField(instance);
                MethodSpec contrutor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
                retrofitUtil.addMethod(contrutor);
                MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance").addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED).returns(ClassName.get("com."+iocWork.value()+".retrofit", "RetrofitUtil"));
                getInstance.addCode("if(instance == null){\n\tinstance = new RetrofitUtil();\n}\nreturn instance;\n");
                retrofitUtil.addMethod(getInstance.build());
                writeClazz("com."+iocWork.value()+".retrofit",retrofitUtil.build());
            }
        }
        return false;
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(HttpInterceptor.class.getCanonicalName());
        stringSet.add(HttpModel.class.getCanonicalName());
        stringSet.add(OkHttp.class.getCanonicalName());
        stringSet.add(IOCWork.class.getCanonicalName());
        stringSet.add(ResponseConverter.class.getCanonicalName());
        stringSet.add(DNS.class.getCanonicalName());
        return stringSet;
    }
}
