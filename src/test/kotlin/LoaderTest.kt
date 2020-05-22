package com.epam.drill.ts.kt2dts

import kotlin.test.*

class LoaderTest {
    @Test
    fun `finds descriptor for Sample class`() {
        val descriptor = Sample::class to Sample.serializer().descriptor
        val descriptors = findSerialDescriptors().toList()
        assertTrue("Descriptor $descriptor not found in $descriptors") {
            descriptor in descriptors
        }
    }
}
