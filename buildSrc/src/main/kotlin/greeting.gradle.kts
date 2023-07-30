import com.example.GreetingTask

tasks {
    register<GreetingTask>("greet") {
        logger.lifecycle("hello, I'm in the configuration phase")
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

    register<Exec>("testExec") {
        commandLine("git", "--version")
    }

    register<Exec>("testExec2") {
        commandLine("sh", "-")

        doFirst {
            standardInput = loadResource("testExec2.sh")
        }
    }
}
