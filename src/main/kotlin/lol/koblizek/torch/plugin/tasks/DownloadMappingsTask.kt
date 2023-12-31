package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonArray
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import lol.koblizek.torch.plugin.util.Download
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.InputStreamReader
import java.net.URL
import java.util.zip.ZipFile


abstract class DownloadMappingsTask : DefaultTask() {
    init {
        group = "torch"
        description = "Downloads the specified game mappings for specified version"
        dependsOn(TorchLoaderPlugin.downloadMinecraftTask)
    }

    @TaskAction
    fun downloadMappings() {
        if (ModProject.modProjectInstance.mappings == "yarn") {
            val finalUrl = getMappingsUrl(ModProject.modProjectInstance.minecraft)
            val mappingFile = Download(finalUrl, "mappings-jar.jar").file
            val zipFile = ZipFile(mappingFile)
            val file = Download.getFile("mappings.tiny", true, this)
            FileUtils.copyInputStreamToFile(
                zipFile.getInputStream(zipFile.getEntry("mappings/mappings.tiny")),
                file
            )
        } else if (ModProject.modProjectInstance.minecraftDevelopment.useCustomMappings()) {
            val mappingFile = Download(ModProject.modProjectInstance.minecraftDevelopment.mappings, "mappings-jar.jar").file
            val zipFile = ZipFile(mappingFile)
            val file = Download.getFile("mappings.tiny", true, this)
            FileUtils.copyInputStreamToFile(
                zipFile.getInputStream(zipFile.getEntry(ModProject.modProjectInstance.getSideOrDefault())),
                file
            )
        }
    }

    /**
     * Gets url for yarn mapping file with specified game version
     *
     * @param gameVersion game version to use
     * @return url with mappings jar
     */
    private fun getMappingsUrl(gameVersion: String): String {
        val array = readJsonArray(gameVersion)
        val latestMappings = array[0].asJsonObject.getAsJsonPrimitive("version").asString
        return "https://maven.fabricmc.net/net/fabricmc/yarn/$latestMappings/yarn-$latestMappings-mergedv2.jar"
    }

    /**
     * Gets all yarn mapping versions for specified game version
     *
     * @param version game version to read the yarn mappings from
     * @return json array containing all yarn mapping versions for the specified game version
     */
    private fun readJsonArray(version: String): JsonArray {
        return Gson().fromJson(
            InputStreamReader(URL("https://meta.fabricmc.net/v2/versions/yarn/$version").openStream()),
            JsonArray::class.java)
    }
}