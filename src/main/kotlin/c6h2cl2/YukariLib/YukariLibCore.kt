package c6h2cl2.YukariLib

import c6h2cl2.YukariLib.Common.CommonProxy
import c6h2cl2.YukariLib.Event.YukariLibEventHandler
import c6h2cl2.YukariLib.Util.RegistryUtil
import com.mojang.util.UUIDTypeAdapter
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.ModMetadata
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.client.Minecraft
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL

/**
 * @author C6H2Cl2
 */
@Mod(modid = YukariLibCore.MOD_ID, version = YukariLibCore.Version, useMetadata = true)
class YukariLibCore {
    companion object {
        const val MOD_ID = "YukariLib"
        const val DOMAIN = "yukarilib"
        const val Version = "1.1.0"
        @JvmStatic
        @Mod.Metadata
        var metadata: ModMetadata? = null
        @JvmStatic
        @SidedProxy(clientSide = "c6h2cl2.YukariLib.Client.ClientProxy", serverSide = "c6h2cl2.YukariLib.Common.CommonProxy")
        var proxy: CommonProxy? = null
        private var enableDeathLog = true
        @JvmStatic
        fun isEnableDeathLog() = enableDeathLog
    }

    @Mod.EventHandler
    fun preinit(event: FMLPreInitializationEvent) {
        val util = RegistryUtil()
        util.build(RegistryTester)
        util.handle()

        if (event.side.isClient && Launch.blackboard["fml.deobfuscatedEnvironment"]?.equals(true) == false) {
            var purchased = true
            val session = Minecraft.getMinecraft().session
            val userName = session.username
            val url = URL("https://api.mojang.com/users/profiles/minecraft/$userName")
            BufferedReader(InputStreamReader(url.openStream())).use {
                purchased = it.readLine() != null
            }
            try {
                UUIDTypeAdapter.fromString(session.playerID)
            } catch (e: Throwable) {
                purchased =  false
            }
            purchased = (purchased && !Minecraft.getMinecraft().isDemo)
            if(!purchased){
                throw PlayerNotOfficialPurchasedException()
            }
        }
        loadMeta()
        getConfig()
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(YukariLibEventHandler())
    }

    private fun getConfig() {
        val proxy = proxy as CommonProxy
        val cfg = Configuration(File(proxy.getDir(), "config/YukariLib.cfg"))
        cfg.load()
        enableDeathLog = cfg.getBoolean("Enable Death Log", "Common", true, "Set false to disable player position log on death.")
        cfg.save()
    }

    private fun loadMeta() {
        val meta = metadata as ModMetadata
        meta.modId = DOMAIN
        meta.name = MOD_ID
        meta.version = Version
        meta.authorList.add("C6H2Cl2")
        meta.credits = "This mod includes Kotlin STDLib and Jetbrains Annotations.\nThey are licensed under the Apache 2 OSS License."
        meta.description = "This mod is an wrapper for mods made from KotlinLanguage and provides some utils."
        meta.autogenerated = false
    }
}