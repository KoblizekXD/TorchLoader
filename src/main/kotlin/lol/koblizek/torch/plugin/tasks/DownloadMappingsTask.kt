package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonArray
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingNsCompleter
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.tinyremapper.NonClassCopyMode
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.*
import java.net.URL
import java.util.regex.Pattern
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
            deobfuscate(
                Download.getFile("minecraft.jar", true, this),
                Download.getFile("minecraft-deobf.jar", true, this),
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
        return Gson().fromJson(InputStreamReader(URL("https://meta.fabricmc.net/v2/versions/yarn/$version").openStream()),
            JsonArray::class.java)
    }
    private fun deobfuscate(inputJar: File, outputPath: File, mappings: File) {
        val writer = StringWriter()
        MappingWriter.create(writer, MappingFormat.TINY_2).use { mapper ->
            MappingReader.read(
                mappings.toPath(), MappingNsCompleter(
                    MappingSourceNsSwitch(mapper, "official", true), emptyMap<String, String>()
                )
            )
        }
        val remapper = TinyRemapper.newRemapper().invalidLvNamePattern(Pattern.compile("\\$\\$\\d+"))
            .inferNameFromSameLvIndex(true)
            .withMappings(
                TinyUtils.createTinyMappingProvider(
                    BufferedReader(StringReader(writer.toString())),
                    "official",
                    "named"
                )
            ).build()
        writer.close()
        try {
            OutputConsumerPath.Builder(outputPath.toPath()).build().use { outputConsumer ->
                outputConsumer.addNonClassFiles(inputJar.toPath(), NonClassCopyMode.FIX_META_INF, remapper)
                remapper.readInputs(inputJar.toPath())
                remapper.apply(outputConsumer)
            }
        } catch (e: IOException) {
            logger.error("Error occurred but was ignored")
        } finally {
            remapper.finish()
        }
    }
}