package lol.koblizek.torch.plugin.tasks

import codechicken.diffpatch.PatchOperation
import codechicken.diffpatch.util.PatchMode
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


abstract class ApplyPatchTask : DefaultTask() {
    init {
        group = "torch"
        description = "applies patches from origin/ directory to source"
    }

    @TaskAction
    fun apply() {
        val patchDir = project.file("patches/")
        val base = project.file("src/main/java/")
        val patchOperation = PatchOperation.builder()
            .basePath(base.toPath())
            .patchesPath(patchDir.toPath())
            .outputPath(base.toPath())
            .mode(PatchMode.OFFSET)
            .build()
        val status = patchOperation.doPatch()
        println("Patch status: $status")
    }
}