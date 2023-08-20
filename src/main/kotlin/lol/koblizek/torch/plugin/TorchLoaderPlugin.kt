package lol.koblizek.torch.plugin

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