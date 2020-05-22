package com.epam.drill.ts.kt2dts

import kotlin.test.*

class ConvertAllTest {
    @Test
    fun `converts all found descriptors without errors`() {
        findSerialDescriptors().convert().appendTo(System.out, "example")
    }
}
