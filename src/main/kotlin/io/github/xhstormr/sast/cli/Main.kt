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
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.Path
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable

object App : CliktCommand(printHelpOnEmptyArgs = true) {

    init {
        context {
            helpFormatter = { MordantHelpFormatter(it, showRequiredTag = true, showDefaultValues = true) }
        }
    }

    private val sourceDir by option().path().default(Path("/source")).validate {
        require(it.isReadable()) {
            with(context.localization) { pathIsNotReadable(pathTypeDirectory(), it.toString()) }
        }
    }

    private val outputDir by option().path().default(Path("/output")).validate {
        require(it.isWritable()) {
            with(context.localization) { pathIsNotWritable(pathTypeDirectory(), it.toString()) }
        }
    }

    private val scanTool by option().enum<ScanTool>().split(",").required().unique()

    private val upload by option().flag()

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
