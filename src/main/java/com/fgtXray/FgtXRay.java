package com.fgtXray;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.fgtXray.config.ConfigHandler;
import com.fgtXray.config.DefaultConfig;
import com.fgtXray.proxy.ServerProxy;
import com.fgtXray.reference.BlockInfo;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = "fgtXray", name = "Fgt X-Ray", version = "1.0.1.75")
public class FgtXRay {
    public static int localPlyX, localPlyY, localPlyZ; // For internal use in the ClientTick thread.
    public static boolean drawOres; // Off by default

    // Keybindings
    public static final int keyIndex_toggleXray = 0;
    public static final int keyIndex_showXrayMenu = 1;
    public static final int[] keyBind_keyValues = {Keyboard.KEY_NONE, Keyboard.KEY_NONE};
    public static final String[] keyBind_descriptions = {"Toggle X-Ray", "Open X-Ray Menu"};
    public static KeyBinding[] keyBind_keys;

    public static Configuration config;

    public static List<BlockInfo> blocks = new ArrayList<BlockInfo>();

    // The instance of your mod that Forge uses.
    @Instance("FgtXray")
    public static FgtXRay instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.fgtXray.proxy.ClientProxy", serverSide = "com.fgtXray.proxy.ServerProxy")
    public static ServerProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        if (config.getCategoryNames().isEmpty()) {
            DefaultConfig.create(config);
            config.save();
        }

        ConfigHandler.setup(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.proxyInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    public static void postChat(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("[§aXRay§r] " + message));
    }
}
