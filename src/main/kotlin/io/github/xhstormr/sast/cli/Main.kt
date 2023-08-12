package io.github.xhstormr.sast.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.options.unique
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.Path

object App : CliktCommand(printHelpOnEmptyArgs = true) {

    init {
        context {
            helpFormatter = { MordantHelpFormatter(it, showRequiredTag = true, showDefaultValues = true) }
        }
    }

    private val sourceDir by option().path(mustBeReadable = true).default(Path("/source"))

    private val outputDir by option().path(mustBeWritable = true).default(Path("/output"))

    private val upload by option().flag()

    private val scanTool by option().enum<ScanTool>().split(",").required().unique()

    override fun run() {
        echo(sourceDir)
        echo(outputDir)
        echo(upload)
        echo(scanTool)
        echo("=====")

        scanTool.forEach {
            val exitcode = process {
                commandLine("sh", "-es", "--", sourceDir, outputDir)
                standardInput = loadResource(it.script)
            }
            echo("exitcode: $exitcode")
        }
    }
}

fun main(args: Array<String>) = App.main(args)
