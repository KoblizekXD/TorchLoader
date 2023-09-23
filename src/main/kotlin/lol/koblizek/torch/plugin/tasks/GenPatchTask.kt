package lol.koblizek.torch.plugin.tasks

import codechicken.diffpatch.DiffOperation
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GenPatchTask : DefaultTask() {
    init {
        group = "torch"
        description = "generates patch files from origin/ directory"
    }

    @TaskAction
    fun genPatches() {
        val patchDir = project.file("patches/")
        if (!patchDir.exists()) {
            patchDir.mkdirs()
        }
        val diffOperation = DiffOperation.builder()
            .aPath(project.file("origin/").toPath())
            .bPath(project.file("src/main/java/").toPath())
            .aPrefix(null)
            .bPrefix(null)
            .filter { it.endsWith(".java") }
            .outputPath(patchDir.toPath())
            .build()
        diffOperation.doDiff()
        println("Patch done.")
    }
}