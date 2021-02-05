rootProject.name = "kt2dts"

pluginManagement {
    val kotlinVersion: String by extra
    val licenseVersion: String by extra
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.github.hierynomus.license") version licenseVersion
    }
}

include(":cli")
include(":api-sample")
