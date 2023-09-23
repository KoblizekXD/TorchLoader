package lol.koblizek.torch.plugin

class MinecraftDevelopment {
    /**
     * if project should be decompiled
     */
    var decompile: Boolean = false
    lateinit var mappings: String

    fun useCustomMappings(): Boolean {
        return ::mappings.isInitialized
    }
}