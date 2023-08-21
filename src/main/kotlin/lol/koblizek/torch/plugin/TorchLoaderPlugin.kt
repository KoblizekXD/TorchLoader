package lol.koblizek.torch.plugin

import lol.koblizek.torch.plugin.tasks.DownloadJsonTask
import lol.koblizek.torch.plugin.tasks.DownloadManifest
import lol.koblizek.torch.plugin.tasks.DownloadMinecraftTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

/**
 * Main plugin entrypoint
 *
 * Standard mod development procedure:
 * - Downloads libraries, minecraft jar and minecraft json file
 * - Deobfuscates Minecraft Jar using Enigma
 * - If used for Minecraft Development:
 *     - Decompiles Minecraft to project source directory
 *     - Notes for future development:
 *        - Task `genPatch` for normal patch generation(folder `root/patches`)
 * - If used for Mod development:
 *     - Downloads latest binary patch
 *     - Applies patch to Minecraft's jar and adds it to dependencies
 */
class TorchLoaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        logger = project.logger
        project.afterEvaluate {
            project.repositories.add(project.repositories.maven {
                it.url = project.uri("https://libraries.minecraft.net")
            })
            if (ModProject.isModProjectInitialized()) {
                if (ModProject.modProjectInstance.isMinecraftInitialized()
                    && ModProject.modProjectInstance.areMappingsInitialized()) {
                    DownloadManifest().execute(project)
                    DownloadJsonTask().execute(project)
                    DownloadMinecraftTask(project).execute(project)
                } else {
                    logger.error("Minecraft not setup - minecraft or mappings not initialized!")
                }
            } else {
                logger.error("Minecraft not setup - nothing to download!")
            }
        }
    }

    companion object {
        internal lateinit var logger: Logger
    }
}

fun minecraft(notation: ModProject.() -> Unit) {
    val proj = ModProject()
    notation(proj)
    ModProject.modProjectInstance = proj
}