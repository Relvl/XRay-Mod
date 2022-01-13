package com.fgtXray;

import org.lwjgl.input.Keyboard;

import com.fgtXray.config.ConfigHandler;
import com.fgtXray.proxy.ServerProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

@Mod(modid = "fgtXray", name = "Fgt X-Ray", version = "1.0.1.75")
public class FgtXRay {
    public static final String FORMAT = "\u00A7";

    public static int localPlyX, localPlyY, localPlyZ; // For internal use in the ClientTick thread.

    // Keybindings
    public static final int keyIndex_toggleXray = 0;
    public static final int keyIndex_showXrayMenu = 1;
    public static final int[] keyBind_keyValues = {Keyboard.KEY_NONE, Keyboard.KEY_NONE};
    public static final String[] keyBind_descriptions = {"Toggle X-Ray", "Open X-Ray Menu"};
    public static KeyBinding[] keyBind_keys;

    @Instance("FgtXray")
    public static FgtXRay instance;
    @SidedProxy(clientSide = "com.fgtXray.proxy.ClientProxy", serverSide = "com.fgtXray.proxy.ServerProxy")
    public static ServerProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.setup(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.proxyInit();
    }

    public static String mcFormat(String message, String f) {
        return String.format("%s%s%s%s%s", FORMAT, f, message, FORMAT, "r");
    }

    public static void postChat(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(String.format("[%s] %s", mcFormat("XRay", "b"), message)));
    }
}
