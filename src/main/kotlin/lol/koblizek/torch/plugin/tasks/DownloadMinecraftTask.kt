package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonObject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileWriter

abstract class DownloadMinecraftTask : DefaultTask() {
    init {
        group = "torch"
        dependsOn(TorchLoaderPlugin.downloadJsonTask)
    }

    @TaskAction
    fun download() {
        val file = File(TorchLoaderPlugin.downloadJsonTask.temporaryDir, "minecraft-data.json")
        val json = Gson().fromJson(file.readText(), JsonObject::class.java)
        val clientUrl = json.getAsJsonObject("downloads")
            .getAsJsonObject("client")
            .getAsJsonPrimitive("url").asString
        Download(clientUrl, "minecraft.jar", true, this)
        val fileWrite = FileWriter(File(temporaryDir, "libraries.json"))
        Gson().toJson(json.getAsJsonArray("libraries"),
            fileWrite)
        fileWrite.close()
    }
}