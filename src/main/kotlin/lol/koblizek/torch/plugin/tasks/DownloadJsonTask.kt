package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.json.VersionManifest
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DownloadJsonTask : DefaultTask() {
    init {
        group = "torch"
    }
    @TaskAction
    fun downloadJson() {
        val manifest = Gson().fromJson(Download.getFile("version-data.json", true, this).readText(), VersionManifest::class.java)
        val version = manifest.findByVersion(ModProject.modProjectInstance.minecraft)
            ?: throw RuntimeException("Specified Minecraft version(${ModProject.modProjectInstance.minecraft}) doesn't exist")
        println("Downloading Minecraft source JSON")
        Download(version.url, "minecraft-data.json", true, this)
        println("Finished source JSON download")
    }
}