package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Latest(@SerializedName("release")
                  @Expose val release: String, @SerializedName("snapshot") @Expose val snapshot: String) {
}