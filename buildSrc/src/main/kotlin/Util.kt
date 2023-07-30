import org.gradle.api.plugins.ObjectConfigurationAction

fun ObjectConfigurationAction.kotlin(module: String) = plugin("org.jetbrains.kotlin.$module")

fun loadResource(path: String) = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    ?: error("Resource $path not found")
