package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.util.Download
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class DownloadManifestTask : DefaultTask() {
    init {
        group = "torch"
        description = "Downloads minecraft version manifest"
    }
    @TaskAction
    fun downloadManifest() {
        logger.quiet("Download version manifest...")
        Download("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json", "version-data.json",
            true, this)
        logger.quiet("Done")
    }
}