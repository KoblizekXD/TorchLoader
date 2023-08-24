package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import lol.koblizek.torch.plugin.TorchLoaderPlugin
import org.gradle.api.Project

abstract class EvaluatedTask {
    abstract val name: String
    val logger = TorchLoaderPlugin.logger


    fun execute(project: Project) {
        println("===[Task] Task execution for :$name started===")
        onEvaluation(ModProject.modProjectInstance, project)
        println("===[Task] Task execution for :$name ended===")
    }

    abstract fun onEvaluation(modProject: ModProject, project: Project)
}