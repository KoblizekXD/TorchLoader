package lol.koblizek.torch.plugin.util

import java.io.File
import java.net.URL

/**
 * Downloads file from desired URL
 *
 * @param url download URL
 * @param tempFile directory, where will be the file stored
 * @param name name of the file
 */
class Download(url: String, name: String) {
    private val file: File

    init {
        val uri = URL(url)
        file = File(File(System.getProperty("java.io.tmpdir")), name)
        uri.openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}