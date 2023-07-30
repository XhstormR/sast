package com.example

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class GreetingTask : DefaultTask() {

    @get:Input
    abstract val greeting: Property<String>

    @TaskAction
    fun greet() {
        println(greeting.get())
    }
}
