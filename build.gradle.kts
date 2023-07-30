import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    greeting
    val kotlinVersion = libs.versions.kotlin
    kotlin("jvm") version kotlinVersion apply true
    alias(libs.plugins.ktlint)
}

allprojects {

    group = "com.example.plugin"
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
