package io.github.relvl.schematicaadvancement;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.relvl.schematicaadvancement.client.ClientTick;
import io.github.relvl.schematicaadvancement.client.KeyBindingHandler;
import io.github.relvl.schematicaadvancement.client.RenderTick;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "schematica-advancement", name = "Schematica Advancement", version = "1.0.0")
public class ModInstance {
    private static final String FORMAT = "\u00A7";

    public static final KeyBinding KEY_TOGGLE = new KeyBinding("Toggle X-Ray", Keyboard.KEY_NUMPAD8, "X-Ray");
    public static final KeyBinding KEY_MENU = new KeyBinding("Open X-Ray Menu", Keyboard.KEY_NUMPAD9, "X-Ray");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.setup(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(KEY_TOGGLE);
        ClientRegistry.registerKeyBinding(KEY_MENU);

        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
        FMLCommonHandler.instance().bus().register(new ClientTick());
        MinecraftForge.EVENT_BUS.register(new ClientActionHandler());
        MinecraftForge.EVENT_BUS.register(new RenderTick());
    }

    public static String mcFormat(String message, String f) {
        return String.format("%s%s%s%s%s", FORMAT, f, message, FORMAT, "r");
    }

    public static void postChat(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(String.format("[%s] %s", mcFormat("XRay", "b"), message)));
    }
}
