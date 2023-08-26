package lol.koblizek.torch.plugin.tasks.evaluated

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.jetbrains.java.decompiler.main.Fernflower
import org.jetbrains.java.decompiler.main.decompiler.DirectoryResultSaver
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences
import java.io.File


class DecompileTask : EvaluatedTask() {
    override val name: String = "finalizeGameSetup"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        if (modProject.isMinecraftDevelopmentInitialized() && modProject.minecraftDevelopment.decompile) {
            if (!project.file("src/main/resources/assets/").exists()) {
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
                fernFlower.addSource(getDeobfuscatedJar())
                fernFlower.decompileContext()
                moveNonCodeFiles(project)
            } else {
                logger.quiet("Minecraft resource are not missing, no need to redownload")
            }
        } else {
            // TODO: Apply binary patches
            project.dependencies.add("implementation", getDeobfuscatedJar())
        }
    }

    /**
     * Gets a file for deobfuscated Minecraft jar
     *
     * @return deobfuscated minecraft jar file
     */
    private fun getDeobfuscatedJar(): File {
        return File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft-deobf.jar")
    }
    /**
     * Moves all files decompiled by fern-flower which are not code, into a resource directory of main source set
     *
     * @param project applying project
     */
    private fun moveNonCodeFiles(project: Project) {
        val resources = project.file("src/main/resources/")
        val main = project.file("src/main/java/")

        main.listFiles()?.forEach {
            if (it.name == "com" || it.name == "net") return
            FileUtils.moveFileToDirectory(it, resources, true)
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