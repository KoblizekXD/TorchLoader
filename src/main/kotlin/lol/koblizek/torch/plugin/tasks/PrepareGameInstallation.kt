package lol.koblizek.torch.plugin.tasks

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Task

abstract class PrepareGameInstallation : DefaultTask() {
    init {
        group = "torch"
        doFirst {

        }
    }
}