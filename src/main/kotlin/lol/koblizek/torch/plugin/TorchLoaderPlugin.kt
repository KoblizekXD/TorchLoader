package lol.koblizek.torch.plugin

import lol.koblizek.torch.plugin.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.logging.Logger

/**
 * Main plugin entrypoint
 *
 * Standard mod development procedure:
 * - Downloads libraries, minecraft jar and minecraft json file
 * - Deobfuscates Minecraft Jar using Tiny Remapper
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

        downloadManifestTask = project.tasks.create("downloadManifest", DownloadManifestTask::class.java)
        downloadJsonTask = project.tasks.create("downloadJson", DownloadJsonTask::class.java)
        downloadMinecraftTask = project.tasks.create("downloadMinecraft", DownloadMinecraftTask::class.java)
        downloadMappingsTask = project.tasks.create("downloadMappings", DownloadMappingsTask::class.java)

        project.afterEvaluate {
            project.repositories.add(getMavenRepository(project))

            if (ModProject.isModProjectInitialized() && ModProject.modProjectInstance.fieldsInitialized()) {
                DecompileTask().execute(project)
            } else {
                throw RuntimeException("Missing \"minecraft\" block, no environment can be setup")
            }
        }
    }
    private fun getMavenRepository(project: Project): MavenArtifactRepository {
        return project.repositories.maven {
            it.url = project.uri("https://libraries.minecraft.net")
        }
    }

    companion object {
        internal lateinit var logger: Logger

        internal lateinit var downloadManifestTask: DownloadManifestTask
        internal lateinit var downloadJsonTask: DownloadJsonTask
        internal lateinit var downloadMinecraftTask: DownloadMinecraftTask
        internal lateinit var downloadMappingsTask: DownloadMappingsTask
    }
}

fun minecraft(notation: ModProject.() -> Unit) {
    val project = ModProject()
    notation(project)
    ModProject.modProjectInstance = project
}