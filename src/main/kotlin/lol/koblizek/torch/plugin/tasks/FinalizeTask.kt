package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project
import org.jetbrains.java.decompiler.main.Fernflower
import org.jetbrains.java.decompiler.main.decompiler.DirectoryResultSaver
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences


class FinalizeTask : EvaluatedTask() {
    override val name: String = "finalizeGameSetup"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        if (modProject.minecraftDevelopment.decompile) {
            val props: MutableMap<String, Any> = HashMap(IFernflowerPreferences.DEFAULTS)
            props[IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES] = "1"
            props[IFernflowerPreferences.REMOVE_SYNTHETIC] = "1"
            props[IFernflowerPreferences.INCLUDE_ENTIRE_CLASSPATH] = "1"
            props[IFernflowerPreferences.PATTERN_MATCHING] = "1"
            props[IFernflowerPreferences.TERNARY_CONDITIONS] = "1"
            props[IFernflowerPreferences.THREADS] = (Runtime.getRuntime().availableProcessors() / 2).toString()
            val fernFlower = Fernflower(
                DirectoryResultSaver(project.file("src/main/java/")),
                props,
                Logger()
            )
            fernFlower.addSource(Download.getFile("minecraft-deobf.jar"))
            fernFlower.addWhitelist("net.minecraft")
            fernFlower.addWhitelist("com.mojang")
            fernFlower.decompileContext()
        }
    }
    class Logger : IFernflowerLogger() {
        override fun writeMessage(message: String, severity: Severity) {
            if (severity.ordinal >= Severity.WARN.ordinal) {
                println(message)
            }
        }

        override fun writeMessage(message: String, severity: Severity, t: Throwable) {
            if (severity.ordinal >= Severity.WARN.ordinal) {
                println(message)
            }
        }

    }
}