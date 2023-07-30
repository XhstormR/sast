plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://repo.huaweicloud.com/repository/maven/")
    gradlePluginPortal()
}

dependencies {
    implementation(plugin("com.google.cloud.tools.jib", "+"))
}

fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
