package lol.koblizek.torch.plugin

class ModProject {
    companion object {
        lateinit var modProjectInstance: ModProject

        fun isModProjectInitialized(): Boolean = ::modProjectInstance.isInitialized
    }

    lateinit var minecraft: String
    lateinit var mappings: String
    lateinit var minecraftDevelopment: MinecraftDevelopment

    fun development(development: MinecraftDevelopment.() -> Unit) {
        minecraftDevelopment = MinecraftDevelopment().also { development(it) }
    }

    private fun isMinecraftInitialized(): Boolean = ::minecraft.isInitialized
    private fun areMappingsInitialized(): Boolean = ::mappings.isInitialized
    fun fieldsInitialized(): Boolean = isMinecraftInitialized() && areMappingsInitialized()
}