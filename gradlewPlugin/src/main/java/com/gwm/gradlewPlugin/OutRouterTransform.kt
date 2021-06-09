package com.gwm.gradlewPlugin

import com.android.tools.build.jetifier.core.utils.Log
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import org.gradle.api.Action
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import javax.lang.model.element.Modifier

class OutRouterTransform : Action<Project> {
    lateinit var fileOut:File
    public override fun execute(p:Project){
        var moudleRouter = TypeSpec.classBuilder("MoudleRouter")
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(
                ClassName.get(
                    "com.jetpackframework.arouter",
                    "RouterInitialization"
                )
            )
        var onInit = MethodSpec.methodBuilder("onInit")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addParameter(Map::class.java, "routerMap")
        val hasPlugin = p.plugins.hasPlugin("com.android.application")
        if (hasPlugin){
            fileOut = File("${p.buildDir.absolutePath}/generated/ap_generated_sources/debug/out/com/${p.name}/arouter")
        }
        Log.logConsumer.info("transform plugins ${fileOut.absolutePath}")
        val childProjects = p.rootProject.childProjects
        childProjects.keys.forEach{
            if (it == "anno" || it == "compiler" || it == "frameworkx" || it == "gradlewPlugin") {
                return@forEach
            }
            Log.logConsumer.info("transform plugins $it")
            val fileIn =
                File("${childProjects[it]?.buildDir?.absolutePath}/generated/ap_generated_sources/debug/out/com/${childProjects[it]?.name}/arouter/${childProjects[it]?.name?.toUpperCaseFirst()}Router.java")
            if (fileIn.exists()) {
                Log.logConsumer.info("transform plugins ${fileIn.absolutePath}")
                //生成自定义类合并所有路由
                onInit.addCode(
                    "new \$T().onInit(routerMap);\n", ClassName.get(
                        "com.${childProjects[it]?.name}.arouter",
                        "${childProjects[it]?.name?.toUpperCaseFirst()}Router"
                    )
                )

            }
        }
        moudleRouter.addMethod(onInit.build())
        var notFound = MethodSpec.methodBuilder("notFound")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addParameter(ClassName.get("android.content", "Context"), "context")
        moudleRouter.addMethod(notFound.build())
        var onError = MethodSpec.methodBuilder("onError")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
            .addParameter(Exception::class.java, "e")
        moudleRouter.addMethod(onError.build())
        writeClazz("com.${p.name}.arouter", moudleRouter.build(),"${p.buildDir.absolutePath}/generated/ap_generated_sources/debug/out")
    }
    fun writeClazz(packageName: String?, clazz: TypeSpec?,path:String?) {
        val javaFile = JavaFile.builder(packageName, clazz)
            .skipJavaLangImports(true)
            .build()
        try {
            javaFile.writeTo(File(path))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

private fun String.toUpperCaseFirst(): String {
    val toCharArray = toCharArray()
    if (toCharArray[0] in 'a'..'z'){
        toCharArray[0] = toCharArray[0] - 32
    }
    return String(toCharArray)
}

