package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VersionManifest(latest: Latest, versions: Array<Version>) {
    @SerializedName("latest")
    @Expose
    private val latest: Latest
    @SerializedName("versions")
    @Expose
    private val versions: Array<Version>

    init {
        this.latest = latest
        this.versions = versions
    }
    fun getLatest(): Latest {
        return this.latest
    }
    fun getVersions(): Array<Version> {
        return this.versions
    }
}