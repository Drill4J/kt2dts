plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `maven-publish`
}

val scriptUrl: String by extra

allprojects {
    apply(from = rootProject.uri("$scriptUrl/git-version.gradle.kts"))

    pluginManager.withPlugin("org.gradle.java-base") {
        java.targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenLocal()
        apply(from = rootProject.uri("$scriptUrl/maven-repo.gradle.kts"))
        jcenter()
    }

    val kxSerializationVersion: String by extra
    val depConstraints = listOf(
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kxSerializationVersion"
    ).map(dependencies.constraints::create)

    configurations.all {
        dependencyConstraints += depConstraints
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation(project(":api-sample"))
}

publishing.publications {
    create<MavenPublication>("default") {
        from(components["java"])
    }
}
