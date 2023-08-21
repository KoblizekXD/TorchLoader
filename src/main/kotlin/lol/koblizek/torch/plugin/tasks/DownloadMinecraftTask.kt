package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonObject
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.apache.commons.lang3.SystemUtils
import org.apache.groovy.util.SystemUtil
import org.gradle.api.Project

class DownloadMinecraftTask(val project: Project) : EvaluatedTask() {
    override val name: String = "downloadMinecraft"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        val file = Download.getFile("minecraft-data.json")
        val json = Gson().fromJson(file.readText(), JsonObject::class.java)
        val clientUrl = json.getAsJsonObject("downloads")
            .getAsJsonObject("client")
            .getAsJsonPrimitive("url").asString
        Download(clientUrl, "minecraft.jar")
        json.getAsJsonArray("libraries").forEach {
            val library = it.asJsonObject.getAsJsonPrimitive("name").asString
            if (shouldDownload(it.asJsonObject)) {
                if (isNative(library)) {
                    project.dependencies.add("runtimeOnly", library)
                } else {
                    project.dependencies.add("implementation", library)
                }
            }
        }
    }
    private fun shouldDownload(library: JsonObject): Boolean {
        if (library.getAsJsonArray("rules")[0] == null) return true
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