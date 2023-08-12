package io.github.xhstormr.sast.cli

fun loadResource(path: String) = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
    ?: error("Resource $path not found")
