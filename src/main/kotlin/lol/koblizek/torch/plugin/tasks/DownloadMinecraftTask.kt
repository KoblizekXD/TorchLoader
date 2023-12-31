package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonObject
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileWriter

abstract class DownloadMinecraftTask : DefaultTask() {
    init {
        group = "torch"
        description = "Downloads a Minecraft jar and parses it's libraries into separate Json"
        dependsOn(TorchLoaderPlugin.downloadJsonTask)
    }

    @TaskAction
    fun download() {
        val file = File(TorchLoaderPlugin.downloadJsonTask.temporaryDir, "minecraft-data.json")
        val json = Gson().fromJson(file.readText(), JsonObject::class.java)
        val clientUrl = json.getAsJsonObject("downloads")
            .getAsJsonObject(if (ModProject.modProjectInstance.useSide()) ModProject.modProjectInstance.side else "client")
            .getAsJsonPrimitive("url").asString
        Download(clientUrl, "minecraft.jar", true, this)
        val gameArgs = FileWriter(File(temporaryDir, "args.json"))
        val fileWrite = FileWriter(File(temporaryDir, "libraries.json"))
        Gson().toJson(json.getAsJsonObject("arguments"), gameArgs)
        Gson().toJson(json.getAsJsonArray("libraries"),
            fileWrite)
        gameArgs.close()
        fileWrite.close()
    }
}