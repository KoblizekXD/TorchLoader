package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.json.VersionManifest
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.Project

class DownloadJsonTask : EvaluatedTask() {
    override val name: String = "downloadJson"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        val manifest = Gson().fromJson(Download.getFile("version-data.json").readText(), VersionManifest::class.java)
        val version = manifest.findByVersion(modProject.minecraft)
            ?: throw RuntimeException("Specified Minecraft version(${modProject.minecraft}) doesn't exist")
        if (!Download.getFile("minecraft-data.json").exists()) {
            println("Downloading Minecraft source JSON")
            Download(version.url, "minecraft-data.json")
            println("Finished source JSON download")
        }
    }
}