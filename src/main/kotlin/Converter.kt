package com.epam.drill.ts.kt2dts

import kotlinx.serialization.*
import kotlin.reflect.*
import kotlin.reflect.full.*

fun Sequence<Descriptor>.convert(): Sequence<TsInterface> = map { (klass, descriptor) ->
    val klassName = klass.simpleName ?: descriptor.serialName
    val discriminator = klass.annotations.filterIsInstance<SerialName>().firstOrNull()?.let {
        listOf(TsField("type", "'${it.value}'"))
    } ?: emptyList()
    val classProps = klass.memberProperties.associate { it.name to it.returnType }
    val fields = descriptor.elementDescriptors().mapIndexed { i, ed ->
        val name = descriptor.getElementName(i)
        val kType: KType = classProps.getValue(name)
        val opt = "?".takeIf { descriptor.isElementOptional(i) } ?: ""
        TsField(
            name = "$name$opt",
            type = ed.toTsType(kType)
        )
    }
    TsInterface(klassName, discriminator + fields)
}

private fun SerialDescriptor.toTsType(kType: KType): String = when (kind) {
    PrimitiveKind.STRING -> "string"
    PrimitiveKind.BOOLEAN -> "boolean"
    is PrimitiveKind -> numberType()
    UnionKind.ENUM_KIND -> elementNames().joinToString(" | ") { "'$it'" }
    StructureKind.LIST -> kType.toTsArrayType()
    StructureKind.MAP -> kType.toTsIndexType()
    StructureKind.CLASS -> "$kType".substringAfterLast('.')
    else -> null
}?.let { if (isNullable) "${it.trimEnd('?')} | null" else it } ?: error("Unsupported type: $kType")

private fun SerialDescriptor.numberType(): String? = when (kind) {
    PrimitiveKind.BYTE,
    PrimitiveKind.SHORT,
    PrimitiveKind.INT,
    PrimitiveKind.LONG,
    PrimitiveKind.FLOAT,
    PrimitiveKind.DOUBLE -> "number"
    else -> null
}

private fun KType.toTsIndexType(): String? = run {
    val (keyType, valType) = arguments
    keyType.type?.toTsType()?.takeIf { it == "string" || it == "number" }?.let { tsKeyType ->
        valType.type?.run {
            toTsArrayType() ?: toTsType()
        }?.let { "{ [key: $tsKeyType]: $it }" }
    }?.let(::optNull)
}

private fun KType.toTsArrayType(): String? = takeIf { arguments.size == 1 }?.run {
    val (arg) = arguments
    arg.type?.toTsType()?.let {
        "${if ('|' in it) "($it)" else it}[]"
    }?.let(::optNull)
}

private fun KType.toTsType() = toTsArrayType() ?: (classifier as? KClass<*>)?.run {
    when (this) {
        String::class, Boolean::class -> simpleName?.decapitalize()
        Byte::class,
        Short::class,
        Int::class,
        Long::class,
        Float::class,
        Double::class -> "number"
        else -> simpleName
    }?.let(::optNull)
}

private fun KType.optNull(tsType: String) = if (isMarkedNullable) "$tsType | null" else tsType
