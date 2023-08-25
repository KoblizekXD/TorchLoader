package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.jetbrains.java.decompiler.main.Fernflower
import org.jetbrains.java.decompiler.main.decompiler.DirectoryResultSaver
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences
import java.io.File
import java.util.zip.ZipFile


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
                fernFlower.addSource(Download.getFile("minecraft-deobf.jar"))
                fernFlower.addWhitelist("net/minecraft")
                fernFlower.addWhitelist("com/mojang")
                fernFlower.decompileContext()
                copyFromZipToResources(Download.getFile("minecraft-deobf.jar"), project)
            } else {
                logger.quiet("Minecraft resource are not missing, no need to redownload")
            }
        } else {
            // TODO: Apply binary patches
            project.dependencies.add("implementation", Download.getFile("minecraft-deobf.jar"))
        }
    }
    private fun copyFromZipToResources(file: File, project: Project) {
        val zipFile = ZipFile(file)
        val resourceDirectory = "src/main/resources/"
        fun copy(name: String) {
            FileUtils.copyInputStreamToFile(
                zipFile.getInputStream(zipFile.getEntry(name)),
                project.file(resourceDirectory + name)
            )
        }
        copy("assets/")
        copy("data/")
        copy("pack.png")
        copy("version.json")
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