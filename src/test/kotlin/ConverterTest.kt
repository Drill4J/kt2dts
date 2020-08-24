package com.epam.drill.ts.kt2dts

import kotlin.test.*


class ConverterTest {
    @Test
    fun `converts Sample class correctly`() {
        val descriptors = sequenceOf(
            Sample::class.descriptor()
        )
        val tsInterface = descriptors.convert().first() as TsInterface
        assertEquals(Sample::class.simpleName, tsInterface.name)
        tsInterface.fields[0].run {
            assertEquals("num?", name)
            assertEquals("number", type)
        }
        tsInterface.fields[1].run {
            assertEquals("str", name)
            assertEquals("string | null", type)
        }
    }

    @Test
    fun `converts Complex class correctly`() {
        val expected = TsInterface(
            name = Complex::class.simpleName!!,
            fields = listOf(
                TsField(Complex::num.name, "number"),
                TsField(Complex::list.name, "string[]"),
                TsField(Complex::optList.name, "(string | null)[]"),
                TsField(Complex::listOfLists.name, "Sample[][]"),
                TsField(Complex::listOfOptLists.name, "(Sample[] | null)[]"),
                TsField(Complex::listOfListsOpt.name, "((Sample | null)[])[]"),
                TsField(Complex::mapOfLists.name, "{ [key: string]: string[] }"),
                TsField(Complex::mapOfOptLists.name, "{ [key: string]: string[] | null }")
            )
        )
        val descriptors = sequenceOf(
            Complex::class.descriptor()
        )
        val converted = descriptors.convert().first()
        assertEquals(expected.render(), converted.render())
    }
}
