plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://repo.huaweicloud.com/repository/maven/")
    gradlePluginPortal()
}

dependencies {
}

fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
