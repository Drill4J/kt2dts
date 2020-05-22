package com.epam.drill.ts.kt2dts

import kotlin.test.*


class ConverterTest {
    @Test
    fun `convert Sample class correctly`() {
        val descriptors = sequenceOf(
            Sample::class to Sample.serializer().descriptor
        )
        val tsInterface = descriptors.convert().first()
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
}
