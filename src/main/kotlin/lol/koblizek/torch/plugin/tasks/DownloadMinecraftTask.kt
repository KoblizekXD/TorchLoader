package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project

class DownloadMinecraftTask : EvaluatedTask() {
    override val name: String = "downloadMinecraft"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        val file = Download.getFile("minecraft-data.json")

    }
}