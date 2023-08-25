package lol.koblizek.torch.plugin.util

import org.gradle.api.DefaultTask
import java.io.File
import java.net.URL

/**
 * Downloads file from desired URL
 *
 * @param url download URL
 * @param name name of the file
 */
class Download(url: String, name: String, asTask: Boolean = false, task: DefaultTask? = null) {
    internal val file: File

    init {
        val uri = URL(url)
        file = if (asTask && task != null) {
            task.temporaryDir
        } else File(File(System.getProperty("java.io.tmpdir")), name)
        uri.openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
    companion object {
        fun getFile(name: String, asTask: Boolean = false, task: DefaultTask? = null): File {
            return if (asTask && task != null) {
                File(task.temporaryDir, name)
            } else File(File(System.getProperty("java.io.tmpdir")), name)
        }
    }
}