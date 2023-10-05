plugins {
    `kotlin-dsl`
}

dependencies {
}

fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
