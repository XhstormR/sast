package io.github.xhstormr.gradle.task

import loadResource
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.submit
import org.gradle.process.ExecOperations
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

abstract class ShellTask : DefaultTask() {

    // TODO https://github.com/gradle/gradle/issues/12009#issuecomment-1665314659
    @get:InputDirectory
    @get:Option(option = "sourceDir", description = "")
    abstract val sourceDir: Property<String> // : DirectoryProperty

    @get:OutputDirectory
    @get:Option(option = "outputDir", description = "")
    abstract val outputDir: Property<String> // : DirectoryProperty

    @get:Inject
    abstract val worker: WorkerExecutor

    @get:Inject
    abstract val fs: FileSystemOperations

    @get:Internal
    val reportDir = project.layout.buildDirectory.dir("report")

    @TaskAction
    fun run() {
        val sourceProvider = project.layout.dir(project.provider { File(sourceDir.get()) })
        val outputProvider = project.layout.dir(project.provider { File(outputDir.get()) })

        val queue = worker.noIsolation()

        repeat(5) {
            queue.submit(ShellWork::class) {
                sourceDir = sourceProvider
                outputDir = outputProvider
            }
        }
    }
}

abstract class ShellWork : WorkAction<ShellWorkParameters> {

    @get:Inject
    abstract val exec: ExecOperations

    override fun execute() {
        val sourceDir = parameters.sourceDir.asFile.get()
        val outputDir = parameters.outputDir.asFile.get()
        exec.exec {
            commandLine("sh", "-es", "--", sourceDir, outputDir)
            standardInput = loadResource("shell-task.sh")
        }
    }
}

interface ShellWorkParameters : WorkParameters {
    val sourceDir: DirectoryProperty
    val outputDir: DirectoryProperty
}
