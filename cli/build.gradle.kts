plugins {
    kotlin("jvm")
    application
    `maven-publish`
}

dependencies {
    implementation("com.github.ajalt:clikt:2.7.1")
    implementation(project(":"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    dependsOn(project(":api-sample").tasks.jar)
}

val mainClassName = "com.epam.drill.ts.kt2dts.cli.MainKt"

application {
    mainClass.set(mainClassName)
}

val fatJarName = "${rootProject.name}-${project.name}"

val fatJar by tasks.registering(Jar::class) {
    archiveBaseName.set(fatJarName)
    manifest.attributes["Main-Class"] = mainClassName
    from(
        sourceSets.main.get().output,
        configurations.runtimeClasspath.get().resolve().map {
            if (it.isFile) zipTree(it) else it
        }
    )
}

publishing.publications {
    create<MavenPublication>("default") {
        artifactId = fatJarName
        artifact(fatJar.get())
    }
}
