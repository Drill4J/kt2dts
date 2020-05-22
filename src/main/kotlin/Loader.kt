package com.epam.drill.ts.kt2dts

import kotlinx.serialization.*
import java.io.*
import java.net.*
import java.util.jar.*
import kotlin.reflect.*

typealias Descriptor = Pair<KClass<out Any>, SerialDescriptor>

fun findSerialDescriptors(
    classLoader: URLClassLoader? = null,
    filter: (String) -> Boolean = { true }
): Sequence<Descriptor> = System.getProperty("java.class.path").run {
    val extraPaths = classLoader?.run {
        urLs.asSequence().map { File(it.toURI()) }
    } ?: emptySequence()
    split(File.pathSeparatorChar).distinct().map(::File).asSequence() + extraPaths
}.findClassNames(filter).map { it.toSerialDescriptor(classLoader ?: Thread.currentThread().contextClassLoader) }

private fun String.toSerialDescriptor(
    classLoader: ClassLoader
): Descriptor = classLoader.loadClass(this).kotlin.run {
    this to serializer().descriptor
}

private fun Sequence<File>.findClassNames(
    filter: (String) -> Boolean = { true }
): Sequence<String> = flatMap { pathElement ->
    val suffix = "\$\$serializer.class"
    if (pathElement.isDirectory) {
        val baseUri = pathElement.toURI()
        pathElement.walkTopDown().map(File::toURI)
            .filter { it.path.endsWith(suffix) }
            .map { baseUri.relativize(it).path }
            .map { it.toClassName(suffix) }
    } else if (pathElement.name.endsWith(".jar")) {
        JarFile(pathElement).entries().asSequence()
            .filter { it.name.endsWith(suffix) }
            .map { it.name.toClassName(suffix) }
    } else emptySequence()
}.filter(filter).distinct()

private fun String.toClassName(suffix: String): String = replace(suffix, "").replace('/', '.')
