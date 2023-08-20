package lol.koblizek.torch.plugin.tasks

import org.gradle.api.Project

abstract class EvaluatedTask {
    abstract fun onEvaluation(project: Project)
}