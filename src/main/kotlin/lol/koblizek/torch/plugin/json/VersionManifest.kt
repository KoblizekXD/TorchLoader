package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionManifest(@SerializedName("latest")
                           @Expose val latest: Latest,
                           @SerializedName("versions") @Expose val versions: Array<Version>) {

    fun findByVersion(id: String): Version? {
        return versions.find { version -> version.id == id }
    }
}