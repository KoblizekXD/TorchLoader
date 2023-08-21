package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project

class DownloadManifest : EvaluatedTask() {
    override val name: String = "downloadAssetIndex"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        println("Download version manifest...")
        val file = Download("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json", "minecraft-data.json").file
        println("Done")
    }
}