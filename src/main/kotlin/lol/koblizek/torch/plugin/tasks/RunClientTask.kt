package lol.koblizek.torch.plugin.tasks

import lol.koblizek.torch.plugin.ModProject
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.JavaExec

abstract class RunClientTask : JavaExec() {
    init {
        group = "torch"
        description = "Starts a game on a client environment"
        classpath = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("main")
            .runtimeClasspath
        mainClass.set("net.minecraft.client.Main")
        jvmArgs("-Xmx2G")
        args(parseGameArguments())
    }
    private fun parseGameArguments(): String {
        val auth_player_name = "TestPlayer"
        val version_name = "Torch Loader"
        val game_directory = "runClient/"
        val assets_root = "assets/"
        val assets_index_name = Regex("^(\\d+\\.\\d+)(\\.\\d+)?$")
            .matchEntire(ModProject.modProjectInstance.minecraft)?.groupValues?.get(0)

        val args = arrayOf(
            "--username",
            "${auth_player_name}",
            "--version",
            "${version_name}",
            "--gameDir",
            "${game_directory}",
            "--assetsDir",
            "${assets_root}",
            "--assetIndex",
            "${assets_index_name}",
        ).joinToString(" ")
        return args
    }
}