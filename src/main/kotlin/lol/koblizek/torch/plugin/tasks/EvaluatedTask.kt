package lol.koblizek.torch.plugin.tasks

import org.gradle.api.Project

abstract class EvaluatedTask {
    abstract val name: String


    fun execute(project: Project) {
        println("task :$name")
        onEvaluation(project)
    }

    abstract fun onEvaluation(project: Project)
}