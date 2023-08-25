package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonObject
import lol.koblizek.torch.plugin.util.Download
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileWriter

class DownloadMinecraftTask : DefaultTask() {
    init {
        group = "torch"
    }

    @TaskAction
    fun download() {
        val file = Download.getFile("minecraft-data.json")
        val json = Gson().fromJson(file.readText(), JsonObject::class.java)
        val clientUrl = json.getAsJsonObject("downloads")
            .getAsJsonObject("client")
            .getAsJsonPrimitive("url").asString
        Download(clientUrl, "minecraft.jar", true, this)
        Gson().toJson(json.getAsJsonArray("libraries"),
            FileWriter(File(temporaryDir, "libraries.json")))
/*        json.getAsJsonArray("libraries").forEach {
            val library = it.asJsonObject.getAsJsonPrimitive("name").asString
            if (shouldDownload(it.asJsonObject)) {
                project.dependencies.add(if (isNative(library)) "runtimeOnly" else "implementation", library)
            }
        }*/
    }
    private fun shouldDownload(library: JsonObject): Boolean {
        if (library.getAsJsonArray("rules") == null) return true
        val name = library.getAsJsonArray("rules")[0]
            .asJsonObject.getAsJsonObject("os")
            .getAsJsonPrimitive("name").asString
        return if (SystemUtils.IS_OS_WINDOWS && name == "windows") {
            true
        } else if (SystemUtils.IS_OS_MAC_OSX && name == "osx") {
            true
        } else if (SystemUtils.IS_OS_LINUX && name == "linux") {
            true
        } else {
            false
        }
    }
    private fun isNative(libraryName: String): Boolean {
        return libraryName.contains("native")
    }
}