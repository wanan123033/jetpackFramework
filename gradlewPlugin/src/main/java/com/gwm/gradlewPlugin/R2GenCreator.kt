package com.gwm.gradlewPlugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
open class R2GenCreator : DefaultTask(){
    @get:OutputDirectory
    var outputDir: File? = null

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    var rFile: FileCollection? = null

    @get:Input
    var packageName: String? = null

    @get:Input
    var className: String? = null

    @Suppress("unused") // Invoked by Gradle.
    @TaskAction
    fun brewJava() {
        brewJava(rFile!!.singleFile, outputDir!!, packageName!!, className!!)
    }
}

fun brewJava(
    rFile: File,
    outputDir: File,
    packageName: String,
    className: String
) {
    FinalRClassBuilder(packageName, className)
        .also { ResourceSymbolListReader(it).readSymbolTable(rFile) }
        .build()
        .writeTo(outputDir)
}
