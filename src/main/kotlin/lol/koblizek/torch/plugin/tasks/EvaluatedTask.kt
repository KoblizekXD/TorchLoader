package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import org.gradle.api.Project

abstract class EvaluatedTask {
    abstract val name: String


    fun execute(project: Project) {
        println("task :$name")
        onEvaluation(ModProject.modProjectInstance, project)
    }

    abstract fun onEvaluation(modProject: ModProject, project: Project)
}