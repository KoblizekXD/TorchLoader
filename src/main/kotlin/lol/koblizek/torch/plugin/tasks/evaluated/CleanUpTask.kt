package lol.koblizek.torch.plugin.tasks.evaluated

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.gradle.api.Project
import java.io.File

class CleanUpTask : EvaluatedTask() {
    override val name: String = "cleanUp"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        println("Deleting all temporary files...")
        getDeletableFiles().forEach {
            println("${it.name} deletion status: ${it.delete()}")
        }
        println("Done")
    }

    companion object {
        fun getDeletableFiles(): Array<File> {
            return arrayOf(
                File(TorchLoaderPlugin.downloadManifestTask.temporaryDir, "version-data.json"),
                File(TorchLoaderPlugin.downloadJsonTask.temporaryDir, "minecraft-data.json"),
                File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft.jar"),
                File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "libraries.json"),
                File(TorchLoaderPlugin.downloadMappingsTask.temporaryDir, "mappings.tiny"),
            )
        }
    }
}