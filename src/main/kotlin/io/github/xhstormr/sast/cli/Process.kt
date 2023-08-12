package io.github.xhstormr.sast.cli

import java.io.InputStream

class ExecSpec {

    private val pb = ProcessBuilder()
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)

    var standardInput: InputStream? = null

    fun commandLine(vararg command: Any): ExecSpec {
        pb.command(command.map { it.toString() })
        return this
    }

    fun exec(): Process {
        val process = pb.start()
        standardInput?.use { input ->
            process.outputStream.use { input.transferTo(it) }
        }
        return process
    }
}

fun process(
    wait: Boolean = true,
    block: ExecSpec.() -> Unit
): Int {
    val execSpec = ExecSpec()
    block(execSpec)
    val process = execSpec.exec()
    return if (wait) process.waitFor() else 0
}
