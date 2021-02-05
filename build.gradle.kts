import java.net.*

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.hierynomus.license")
    `maven-publish`
}

val scriptUrl: String by extra

val jvmTarget = JavaVersion.VERSION_1_8

allprojects {
    apply(from = rootProject.uri("$scriptUrl/git-version.gradle.kts"))

    repositories {
        mavenLocal()
        apply(from = rootProject.uri("$scriptUrl/maven-repo.gradle.kts"))
        jcenter()
    }

    plugins.withType<JavaBasePlugin> {
        java.targetCompatibility = jvmTarget
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        kotlin {
            target.compilations.all {
                kotlinOptions.jvmTarget = "$jvmTarget"
            }
            listOf(
                "kotlinx.serialization.InternalSerializationApi",
                "kotlinx.serialization.ExperimentalSerializationApi"
            ).let { annotations ->
                sourceSets.all { annotations.forEach(languageSettings::useExperimentalAnnotation) }
            }
        }
    }
}

val kxSerializationVersion: String by extra

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kxSerializationVersion")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation(project(":api-sample"))
}

publishing.publications {
    create<MavenPublication>("default") {
        from(components["java"])
    }
}

val licenseFormatSettings by tasks.registering(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
    source = fileTree(project.projectDir).also {
        include("**/*.kt", "**/*.java", "**/*.groovy")
        exclude("**/.idea")
    }.asFileTree
    headerURI = URI("https://raw.githubusercontent.com/Drill4J/drill4j/develop/COPYRIGHT")
}

license {
    skipExistingHeaders = true
}

tasks["licenseFormat"].dependsOn(licenseFormatSettings)
