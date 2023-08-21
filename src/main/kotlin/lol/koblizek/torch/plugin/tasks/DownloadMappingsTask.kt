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
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.ivy.util.Message.progress
import java.io.File
import java.io.InputStreamReader
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
            val finalUrl = "https://maven.fabricmc.net/net/fabricmc/yarn/$version/yarn-$version-v2.jar"
            val mappingFile = Download(finalUrl, "mapjar.jar").file
            val zipFile = ZipFile(mappingFile)
            val file = File(System.getProperty("java.io.tmpdir"), "mp.temp")
            FileUtils.copyInputStreamToFile(zipFile.getInputStream(zipFile.getEntry("mappings/mappings.tiny")), file)
            deobfuscate(Download.getFile("minecraft.jar"), file)
            println("Work done, deobf minecraft in: ${Download.getFile("minecraft.jar").absolutePath}")
        }
    }
    private fun deobfuscate(jar: File, mappings: File) {
        println("[Enigma] Begin deobf work...")
        val enigma = Enigma.create()
        val listener = ProgressListener.none()
        val project = enigma.openJar(jar.toPath(), ClasspathClassProvider(), listener)
        setMappings(project, mappings)
        val export = project.exportRemappedJar(listener)
        export.write(File(System.getProperty("java.io.tmpdir"), "minecraft-deobf.jar").toPath(), listener)
        println("[Enigma] Done")
    }
    private fun setMappings(project: EnigmaProject, mappings: File) {
        val saveParameters: MappingSaveParameters = project.enigma.profile.mappingSaveParameters
        val entries: EntryTree<EntryMapping> = MappingFormat.TINY_V2.read(mappings.toPath(), ProgressListener.none(), saveParameters)

        project.setMappings(entries)
    }
}