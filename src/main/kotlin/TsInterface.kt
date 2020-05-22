package com.epam.drill.ts.kt2dts

data class TsInterface(
    val name: String,
    val fields: List<TsField>
)

data class TsField(
    val name: String,
    val type: String
)

fun Sequence<TsInterface>.appendTo(appendable: Appendable, module: String) {
    appendable.appendln("declare module '$module' {")
    forEach { it.appendTo(appendable, indent = "  ", modifier = "export ") }
    appendable.appendln("}")
}

fun TsInterface.appendTo(
    appendable: Appendable,
    modifier: String = "",
    indent: String = ""
) {
    appendable.appendln("$indent${modifier}interface $name {")
    fields.forEach {
        appendable.appendln("$indent  ${it.name}: ${it.type};")
    }
    appendable.appendln("$indent}")
}
