package lol.koblizek.torch.plugin.tasks

import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.JavaExec

class RunClientTask : JavaExec() {
    init {
        group = "torch"
        description = "Starts a game on a client environment"
        classpath = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("main")
            .runtimeClasspath
        mainClass.set("net.minecraft.client.Main")
        jvmArgs("-Xmx2G")
    }
}