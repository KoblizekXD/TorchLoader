package lol.koblizek.torch.plugin.tasks.evaluated

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.Project
import java.io.File

class DownloadLibraries : EvaluatedTask() {
    override val name: String = "downloadLibraries"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        val json = Gson().fromJson(
            File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "libraries.json")
                .readText(),
            JsonArray::class.java
        )
        json.forEach {
            val library = it.asJsonObject.getAsJsonPrimitive("name").asString
            if (shouldDownload(it.asJsonObject)) {
                project.dependencies.add(if (isNative(library)) "runtimeOnly" else "implementation", library)
            }
        }
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