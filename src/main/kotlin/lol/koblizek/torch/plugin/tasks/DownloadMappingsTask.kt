package lol.koblizek.torch.plugin.tasks

import com.google.gson.Gson
import com.google.gson.JsonArray
import cuchaz.enigma.Enigma
import cuchaz.enigma.EnigmaProject
import cuchaz.enigma.ProgressListener
import cuchaz.enigma.classprovider.ClasspathClassProvider
import cuchaz.enigma.translation.mapping.EntryMapping
import cuchaz.enigma.translation.mapping.serde.MappingFormat
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters
import cuchaz.enigma.translation.mapping.tree.EntryTree
import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.util.Download
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingNsCompleter
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.tinyremapper.NonClassCopyMode
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.*
import java.net.URL
import java.util.zip.ZipFile


class DownloadMappingsTask : EvaluatedTask() {
    override val name: String = "downloadMappings"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        if (modProject.mappings == "yarn") {
            val array = Gson().fromJson(InputStreamReader(URL(
                "https://meta.fabricmc.net/v2/versions/yarn/${modProject.minecraft}"
            ).openStream()), JsonArray::class.java)
            val version = array[0].asJsonObject.getAsJsonPrimitive("version").asString
            val finalUrl = "https://maven.fabricmc.net/net/fabricmc/yarn/$version/yarn-$version-mergedv2.jar"
            val mappingFile = Download(finalUrl, "mapjar.jar").file
            val zipFile = ZipFile(mappingFile)
            val file = File(System.getProperty("java.io.tmpdir"), "mappings.tiny")
            FileUtils.copyInputStreamToFile(zipFile.getInputStream(zipFile.getEntry("mappings/mappings.tiny")), file)
            deobfuscate(Download.getFile("minecraft.jar"), File(System.getProperty("java.io.tmpdir"), "minecraft-deobf.jar"), file)
        }
    }
    @Deprecated("Deprecated as of Fabric's Enigma deprecation, use parseMappings instead(mappings-io api)")
    private fun deobfuscate(jar: File, mappings: File) {
        println("[Enigma] Begin deobfuscation work...")
        val enigma = Enigma.create()
        val listener = ProgressListener.none()
        val project = enigma.openJar(jar.toPath(), ClasspathClassProvider(), listener)
        setMappings(project, mappings)
        val export = project.exportRemappedJar(listener)
        export.write(File(System.getProperty("java.io.tmpdir"), "minecraft-deobf.jar").toPath(), listener)
        println("[Enigma] Done")
    }
    private fun deobfuscate(inputJar: File, outputPath: File, mappings: File) {
        val writer = StringWriter()
        MappingWriter.create(writer, net.fabricmc.mappingio.format.MappingFormat.TINY_2).use { mapper ->
            MappingReader.read(
                mappings.toPath(), MappingNsCompleter(
                    MappingSourceNsSwitch(mapper, "official", true), emptyMap<String, String>()
                )
            )
        }
        val remapper = TinyRemapper.newRemapper()
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
            println("Error occurred but was ignored")
        } finally {
            remapper.finish()
        }
    }
    private fun setMappings(project: EnigmaProject, mappings: File) {
        val saveParameters: MappingSaveParameters = project.enigma.profile.mappingSaveParameters
        val entries: EntryTree<EntryMapping> = MappingFormat.TINY_V2.read(mappings.toPath(), ProgressListener.none(), saveParameters)
        project.setMappings(entries)
    }
}