package lol.koblizek.torch.plugin

class ModProject {
    companion object {
        /**
         * Instance of ModProject used by consumer
         */
        lateinit var modProjectInstance: ModProject

        /**
         * @return true if `minecraft` block is used
         */
        fun isModProjectInitialized(): Boolean = ::modProjectInstance.isInitialized
    }

    lateinit var minecraft: String
    lateinit var mappings: String
    lateinit var minecraftDevelopment: MinecraftDevelopment

    /**
     * Specifies the development of game api,
     * set `decompile` to true if you want the game to be decompiled into `main` source set
     */
    fun development(development: MinecraftDevelopment.() -> Unit) {
        minecraftDevelopment = MinecraftDevelopment().also { development(it) }
    }
    /**
     * @return true if is `development` block used inside `minecraft` block
     */
    fun isMinecraftDevelopmentInitialized(): Boolean = ::minecraftDevelopment.isInitialized
    /**
     * @return true if is minecraft field inside `minecraft` block used
     */
    private fun isMinecraftInitialized(): Boolean = ::minecraft.isInitialized
    /**
     * @return true if are mappings field inside `minecraft` block used
     */
    private fun areMappingsInitialized(): Boolean = ::mappings.isInitialized
    fun fieldsInitialized(): Boolean = isMinecraftInitialized() && areMappingsInitialized()
}