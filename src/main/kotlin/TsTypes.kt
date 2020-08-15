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
    appendable.appendLine("declare module '$module' {")
    forEach { it.appendTo(appendable, indent = "  ", modifier = "export ") }
    appendable.appendLine("}")
}

fun TsType.render(): String = "${StringBuffer().also { appendTo(it) }}"

fun TsType.appendTo(
    appendable: Appendable,
    modifier: String = "",
    indent: String = ""
) {
    when(this) {
        is TsInterface -> {
            appendable.appendLine("$indent${modifier}interface $name {")
            fields.forEach {
                appendable.appendLine("$indent  ${it.name}: ${it.type};")
            }
            appendable.appendLine("$indent}")
        }
        is TsUnion -> {
            appendable.appendLine("$indent${modifier}type $name = ${types.joinToString(" | ")}")
        }
    }
}
