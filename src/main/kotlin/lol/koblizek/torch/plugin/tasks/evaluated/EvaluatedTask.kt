package lol.koblizek.torch.plugin.tasks.evaluated

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.gradle.api.Project

abstract class EvaluatedTask {
    abstract val name: String
    val logger = TorchLoaderPlugin.logger


    fun execute(project: Project) {
        logger.quiet("[Torch] Task execution for :$name started")
        onEvaluation(ModProject.modProjectInstance, project)
        logger.quiet("[Torch] Task execution for :$name ended")
    }

    abstract fun onEvaluation(modProject: ModProject, project: Project)
}