plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val kxSerializationVersion: String by extra

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kxSerializationVersion")
}
