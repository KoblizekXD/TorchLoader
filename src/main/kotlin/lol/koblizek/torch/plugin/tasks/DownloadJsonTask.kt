package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import lol.koblizek.torch.plugin.json.VersionManifest
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class DownloadJsonTask : DefaultTask() {
    init {
        group = "torch"
        description = "Downloads Json file for currently specified minecraft version"
        dependsOn(TorchLoaderPlugin.downloadManifestTask)
    }
    @TaskAction
    fun downloadJson() {
        val manifest = Gson().fromJson(File(TorchLoaderPlugin.downloadManifestTask.temporaryDir, "version-data.json").readText(), VersionManifest::class.java)
        val version = manifest.findByVersion(ModProject.modProjectInstance.minecraft)
            ?: throw RuntimeException("Specified Minecraft version(${ModProject.modProjectInstance.minecraft}) doesn't exist")
        println("Downloading Minecraft source JSON")
        if (ModProject.modProjectInstance.useCustomManifest())
            Download(ModProject.modProjectInstance.customManifest, "minecraft-data.json", true, this)
        else Download(version.url, "minecraft-data.json", true, this)
        println("Finished source JSON download")
    }
}