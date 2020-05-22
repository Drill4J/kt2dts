rootProject.name = "kt2dts"

pluginManagement {
    val kotlinVersion: String by extra
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

include(":cli")
include(":api-sample")
