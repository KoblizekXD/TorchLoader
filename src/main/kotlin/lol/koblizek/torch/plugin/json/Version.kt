package lol.koblizek.torch.plugin.json

import com.google.gson.annotations.Expose

data class Version(
    @Expose val id: String,
    @Expose val type: String,
    @Expose val url: String,
    @Expose val time: String,
    @Expose val releaseTime: String,
    @Expose val sha1: String,
    @Expose val complianceLevel: Int
)