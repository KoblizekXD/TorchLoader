package lol.koblizek.torch.plugin.tasks.evaluated

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.MappingWriter
import net.fabricmc.mappingio.adapter.MappingNsCompleter
import net.fabricmc.mappingio.adapter.MappingSourceNsSwitch
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.tinyremapper.NonClassCopyMode
import net.fabricmc.tinyremapper.OutputConsumerPath
import net.fabricmc.tinyremapper.TinyRemapper
import net.fabricmc.tinyremapper.TinyUtils
import net.minecraftforge.fart.api.Renamer
import net.minecraftforge.fart.api.SignatureStripperConfig
import net.minecraftforge.fart.api.SourceFixerConfig
import net.minecraftforge.fart.api.Transformer
import org.gradle.api.Project
import java.io.*
import java.util.regex.Pattern

class DeobfuscateTask : EvaluatedTask() {
    override val name: String = "deobfuscateTask"

    override fun onEvaluation(modProject: ModProject, project: Project) {
        Renamer.builder().add(Transformer.recordFixerFactory())
            .add(Transformer.sourceFixerFactory(SourceFixerConfig.JAVA))
            .add(Transformer.signatureStripperFactory(SignatureStripperConfig.ALL)).build()
            .run(File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft.jar"),
                File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft-fix.jar"),)
        deobfuscate(
            File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft-fix.jar"),
            File(TorchLoaderPlugin.downloadMinecraftTask.temporaryDir, "minecraft-deobf.jar"),
            File(TorchLoaderPlugin.downloadMappingsTask.temporaryDir, "mappings.tiny")
        )
    }

    /**
     * Deobfuscates a jar with specified mappings files using Tiny Remapper and Mappings IO
     *
     * @param inputJar jar that will be remapped
     * @param outputPath path for new remapped jar
     * @param mappings mapping file to use
     * @author people on fabric discord
     */
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
            .renameInvalidLocals(true)
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