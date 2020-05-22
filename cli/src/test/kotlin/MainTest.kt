package com.epam.drill.ts.kt2dts.cli

import java.io.*
import kotlin.test.*

class MainTest {
    @Test
    fun `no classes to convert`() {
        main(arrayOf())
    }

    @Test
    fun `converts classes from sample-api jar without errors`() {
        val module = "api-sample"
        val rootDir = File("..", module)
        val jarFile = rootDir.walkTopDown().first { it.name.endsWith(".jar") }
        val file = File.createTempFile("kt2dts-", "-api-sample.d.ts")
        println(file)
        try {
            val args = listOf(
                "--cp=${jarFile.path}",
                "--module=$module",
                "--output=${file.path}"
            )
            main(args.toTypedArray())
            assertTrue("Output file is empty") { file.length() > 0 }
        } finally {
//            file.delete()
        }
    }
}
