rootProject.name = "sast"

pluginManagement {
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        mavenCentral()
    }
}
