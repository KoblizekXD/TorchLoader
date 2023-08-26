package lol.koblizek.torch.plugin.util

import org.gradle.api.DefaultTask
import java.io.File
import java.net.URL

/**
 * Downloads file from desired URL, if downloading inside task, file will be placed
 * into temporary directory of the task, if it is not being downloading inside task, file will be placed
 * into file corresponding to system property "java.io.tmpdir"
 *
 * @param url download URL
 * @param name name of the file
 * @param asTask if is used inside task, defaults to false
 * @param task required field if is used inside task
 */
class Download(url: String, name: String, asTask: Boolean = false, task: DefaultTask? = null) {
    internal val file: File

    init {
        val uri = URL(url)
        file = if (asTask && task != null) {
            File(task.temporaryDir, name)
        } else File(File(System.getProperty("java.io.tmpdir")), name)
        uri.openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
    companion object {
        /**
         * Requests the downloaded temporary file
         *
         * @see Download
         * @param name name of the file
         * @param asTask if is used inside task, defaults to false
         * @param task required field if is used inside task
         */
        fun getFile(name: String, asTask: Boolean = false, task: DefaultTask? = null): File {
            return if (asTask && task != null) {
                File(task.temporaryDir, name)
            } else File(File(System.getProperty("java.io.tmpdir")), name)
        }
    }
}