import io.github.xhstormr.gradle.task.GreetingTask
import io.github.xhstormr.gradle.task.ShellTask

tasks {
    register<GreetingTask>("greet") {
        logger.lifecycle("hello, I'm in the configuration phase")
        group = "test"
        greeting = "hello world! I'm in the execution phase"

        doFirst {
            logger.lifecycle("hello, do first before execution phase")
            mkdir("hello")
        }
        doLast {
            logger.lifecycle("hello, do last after execution phase")
            delete("hello")
        }
    }

    register<ShellTask>("shell") {
        group = "test"
    }

    register<Exec>("testExec") {
        group = "test"
        commandLine("git", "--version")
    }
}
