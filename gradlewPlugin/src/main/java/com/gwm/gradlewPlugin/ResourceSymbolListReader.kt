package com.gwm.gradlewPlugin

import com.squareup.javapoet.CodeBlock
import java.io.File
import java.util.*
import kotlin.random.Random

internal class ResourceSymbolListReader(private val builder: FinalRClassBuilder) {

  fun readSymbolTable(symbolTable: File) {
    symbolTable.forEachLine { processLine(it) }
  }

  private fun processLine(line: String) {
    val values = line.split(' ')
    if (values.size < 4) {
      return
    }
    val javaType = values[0]
    if (javaType != "int") {
      return
    }
    val symbolType = values[1]
    if (symbolType !in SUPPORTED_TYPES) {
      return
    }
    val name = values[2]
//    val value = CodeBlock.of("\$L", "R."+symbolType+"."+name)
    val value = CodeBlock.of("\$L", UUID.randomUUID().hashCode())
//    val value = CodeBlock.of("\$L", valuess)
    builder.addResourceField(symbolType, name, value)
  }
}
