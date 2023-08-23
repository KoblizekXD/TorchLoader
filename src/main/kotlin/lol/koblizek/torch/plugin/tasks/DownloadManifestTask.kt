package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project

class DownloadManifestTask : EvaluatedTask() {
    override val name: String = "downloadAssetIndex"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        println("Download version manifest...")
        Download("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json", "version-data.json")
        println("Done")
    }
}