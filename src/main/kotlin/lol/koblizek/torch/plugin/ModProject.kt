package lol.koblizek.torch.plugin

class ModProject {
    companion object {
        lateinit var modProjectInstance: ModProject

        fun isModProjectInitialized(): Boolean = ::modProjectInstance.isInitialized
    }

    lateinit var minecraft: String
    lateinit var mappings: String

    fun isMinecraftInitialized(): Boolean = ::minecraft.isInitialized
    fun areMappingsInitialized(): Boolean = ::mappings.isInitialized
}