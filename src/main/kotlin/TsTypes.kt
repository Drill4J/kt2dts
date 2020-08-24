package com.epam.drill.ts.kt2dts

sealed class TsType {
    abstract val name: String
}

data class TsUnion(
    override val name: String,
    val types: List<String> = emptyList()
) : TsType()

data class TsInterface(
    override val name: String,
    val fields: List<TsField> = emptyList()
) : TsType()

data class TsField(
    val name: String,
    val type: String
)

fun Sequence<TsType>.appendTo(appendable: Appendable, module: String) {
    appendable.appendln("declare module '$module' {")
    forEach { it.appendTo(appendable, indent = "  ", modifier = "export ") }
    appendable.appendln("}")
}

fun TsType.render(): String = "${StringBuffer().also { appendTo(it) }}"

fun TsType.appendTo(
    appendable: Appendable,
    modifier: String = "",
    indent: String = ""
) {
    when(this) {
        is TsInterface -> {
            appendable.appendln("$indent${modifier}interface $name {")
            fields.forEach {
                appendable.appendln("$indent  ${it.name}: ${it.type};")
            }
            appendable.appendln("$indent}")
        }
        is TsUnion -> {
            appendable.appendln("$indent${modifier}type $name = ${types.joinToString(" | ")}")
        }
    }
}
