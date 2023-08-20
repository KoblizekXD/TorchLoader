package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project

class DownloadJsonTask : EvaluatedTask() {
    override val name: String = "downloadJson"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        val url: String = if (modProject.minecraft == "1.19.2") {
            "https://piston-meta.mojang.com/v1/packages/ed548106acf3ac7e8205a6ee8fd2710facfa164f/1.19.2.json"
        } else throw RuntimeException("Invalid Minecraft version: ${modProject.minecraft}")

        println("Downloading Minecraft source JSON")
        Download(url, "minecraft-data.json")
        println("Finished source JSON download")
    }
}