package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Latest(release: String, snapshot: String) {

    @SerializedName("release")
    @Expose
    private var release: String

    @SerializedName("snapshot")
    @Expose
    private var snapshot: String

    init {
        this.release = release
        this.snapshot = snapshot
    }

    fun getRelease(): String {
        return release
    }

    fun getSnapshot(): String {
        return snapshot
    }
}