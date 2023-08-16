import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    greeting
    application
    val kotlinVersion = libs.versions.kotlin
    kotlin("jvm") version kotlinVersion
    alias(libs.plugins.ktlint)
    alias(libs.plugins.native)
}

application {
    applicationName = project.name
    mainClass = "io.github.xhstormr.sast.cli.MainKt"
}

graalvmNative {
    metadataRepository {
        enabled = true
    }

    binaries.all {
        resources.autodetect()
        // buildArgs("--static")
    }
}

dependencies {
    implementation(libs.clikt)
}

tasks {
    register<Jar>("fatJar") {
        archiveFileName = providers.gradleProperty("out").orNull
        manifest.attributes["Main-Class"] = application.mainClass
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets["main"].output)
        from(configurations.runtimeClasspath.get().map { zipTree(it) })
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_builtins")

        doLast {
            println(outputs.files.singleFile)
        }
    }
}

allprojects {

    group = "io.github.xhstormr.sast"
    version = "1.0.0-SNAPSHOT"

    apply {
        kotlin("jvm")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            }
        }

        withType<JavaCompile> {
            with(options) {
                encoding = Charsets.UTF_8.name()
                isFork = true
                isIncremental = true
            }
        }

        withType<Wrapper> {
            gradleVersion = libs.versions.gradle.get()
            distributionType = Wrapper.DistributionType.ALL
        }
    }
}
