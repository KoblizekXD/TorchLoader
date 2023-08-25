package lol.koblizek.torch.plugin.tasks.evaluated

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.gradle.api.Project
import java.io.File

class CleanUpTask : EvaluatedTask() {
    override val name: String = "cleanUp"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        println("Deleting all temporary files...")
        File(TorchLoaderPlugin.downloadManifestTask.temporaryDir, "version-data.json")
            .delete()
        File(TorchLoaderPlugin.downloadJsonTask.temporaryDir, "minecraft-data.json")
            .delete()
        File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft.jar")
            .delete()
        File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "libraries.json")
            .delete()
        File(TorchLoaderPlugin.downloadMappingsTask.temporaryDir, "mappings-jar.jar")
            .delete()
        File(TorchLoaderPlugin.downloadMappingsTask.temporaryDir, "mappings.tiny")
            .delete()
        println("Done")
    }
}