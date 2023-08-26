package lol.koblizek.torch.plugin

import lol.koblizek.torch.plugin.tasks.*
import lol.koblizek.torch.plugin.tasks.evaluated.CleanUpTask
import lol.koblizek.torch.plugin.tasks.evaluated.DecompileTask
import lol.koblizek.torch.plugin.tasks.evaluated.DeobfuscateTask
import lol.koblizek.torch.plugin.tasks.evaluated.DownloadLibraries
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
                if (temporaryFilesExist()) {
                    DownloadLibraries().execute(project)
                    DeobfuscateTask().execute(project)
                    DecompileTask().execute(project)
                    CleanUpTask().execute(project)
                } else {
                    println("Temps not found")
                }
            } else {
                throw RuntimeException("Missing \"minecraft\" block, no environment can be setup")
            }
        }
    }

    /**
     * Check if temporary files from `torch` tasks exists, returns true they do, false otherwise
     * @see CleanUpTask.getDeletableFiles
     * @return true if at least one file exist, if no files exist, returns false
     */
    private fun temporaryFilesExist(): Boolean {
        var exist = true
        CleanUpTask.getDeletableFiles().forEach {
            if (!it.exists()) exist = false
        }
        return exist
    }

    /**
     * Returns main Minecraft maven repository
     * @return Minecraft's library repository
     */
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

/**
 * Main modding entrypoint, used to specify:
 *  - game version
 *  - game mappings
 *  - game api to use
 *  - if game should be used as dependency or as source
 * Example usage:
 * ```kotlin
 * minecraft {
 *     mappings = "yarn" // Minecraft mappings to use(currently supports only yarn)
 *     minecraft = "1.19.4" // Minecraft version to use
 *     development {
 *          decompile = true // decompile Minecraft's source code into main source set
 *     }
 * }
 * ```
 */
fun minecraft(notation: ModProject.() -> Unit) {
    val project = ModProject()
    notation(project)
    ModProject.modProjectInstance = project
}