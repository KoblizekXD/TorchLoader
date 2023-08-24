package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionManifest(@SerializedName("latest")
                           @Expose val latest: Latest,
                           @SerializedName("versions") @Expose val versions: Array<Version>) {

    fun findByVersion(id: String): Version? {
        return versions.find { version -> version.id == id }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VersionManifest

        if (latest != other.latest) return false
        if (!versions.contentEquals(other.versions)) return false

        return true
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun hashCode(): Int {
        var result = latest.hashCode()
        result = 31 * result + versions.contentHashCode()
        return result
    }
}