package lol.koblizek.torch.plugin

import lol.koblizek.torch.plugin.tasks.DownloadJsonTask
import lol.koblizek.torch.plugin.tasks.DownloadManifest
import lol.koblizek.torch.plugin.tasks.DownloadMinecraftTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

/**
 * Main plugin entrypoint
 */
class TorchLoaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        logger = project.logger
        project.afterEvaluate {
            DownloadManifest().execute(project)
            DownloadJsonTask().execute(project)
            DownloadMinecraftTask(project).execute(project)
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