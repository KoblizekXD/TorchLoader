package lol.koblizek.torch.plugin

class ModProject {
    companion object {
        lateinit var modProjectInstance: ModProject

        fun isModProjectInitialized(): Boolean = ::modProjectInstance.isInitialized
    }

    lateinit var minecraft: String
    lateinit var mappings: String
    lateinit var mcDev: MinecraftDevelopment

    fun development(development: MinecraftDevelopment.() -> Unit) {
        val dev = MinecraftDevelopment()
        development(dev)
        mcDev = dev
    }

    fun isMinecraftInitialized(): Boolean = ::minecraft.isInitialized
    fun areMappingsInitialized(): Boolean = ::mappings.isInitialized
    fun fieldsInitialized(): Boolean = isMinecraftInitialized() && areMappingsInitialized()
}