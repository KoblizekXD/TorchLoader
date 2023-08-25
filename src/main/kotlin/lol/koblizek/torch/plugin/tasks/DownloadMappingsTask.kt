package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonArray
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.InputStreamReader
import java.net.URL
import java.util.zip.ZipFile


class DownloadMappingsTask : DefaultTask() {
    init {
        group = "torch"
    }

    @TaskAction
    fun downloadMappings(modProject: ModProject, project: Project) {
        if (modProject.mappings == "yarn") {
            val finalUrl = getMappingsUrl(modProject.minecraft)
            val mappingFile = Download(finalUrl, "mappings-jar.jar").file
            val zipFile = ZipFile(mappingFile)
            val file = Download.getFile("mappings.tiny", true, this)
            FileUtils.copyInputStreamToFile(
                zipFile.getInputStream(zipFile.getEntry("mappings/mappings.tiny")),
                file
            )
        }
    }
    private fun getMappingsUrl(gameVersion: String): String {
        val array = readJsonArray(gameVersion)
        val latestMappings = array[0].asJsonObject.getAsJsonPrimitive("version").asString
        return "https://maven.fabricmc.net/net/fabricmc/yarn/$latestMappings/yarn-$latestMappings-mergedv2.jar"
    }
    private fun readJsonArray(version: String): JsonArray {
        return Gson().fromJson(
            InputStreamReader(URL("https://meta.fabricmc.net/v2/versions/yarn/$version").openStream()),
            JsonArray::class.java)
    }
}