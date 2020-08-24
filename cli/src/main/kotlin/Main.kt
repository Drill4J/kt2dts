package com.epam.drill.ts.kt2dts.cli

import com.epam.drill.ts.kt2dts.*
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import java.io.*
import java.net.*


fun main(args: Array<String>) = Kt2DtsMain().main(args)

private class Kt2DtsMain : CliktCommand() {
    private val cp: List<String>? by option(help = "Classpath (comma separated)").split(",")
    private val module: String by option(help = "Module name").default("example")
    private val output: String? by option(help = "Output file, if not specified stdout is used")

    override fun run() {
        val classLoader = cp?.run {
            val urls = map { File(it).toURI().toURL() }
            println(urls)
            URLClassLoader(urls.toTypedArray(), Thread.currentThread().contextClassLoader)
        }
        val converted = findSerialDescriptors(classLoader).convert()
        output?.let {
            File(it).bufferedWriter().use { writer ->
                converted.appendTo(writer, module)
            }
        } ?: converted.appendTo(System.out, module)
    }
}
