package io.github.relvl.schematicaadvancement.client;

import io.github.relvl.schematicaadvancement.ModInstance;
import io.github.relvl.schematicaadvancement.client.gui.GuiScreenXRayMenu;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

public class KeyBindingHandler {
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
            return;
        }
        if (mc.currentScreen != null || mc.theWorld == null) {
            return;
        }

        if (ModInstance.KEY_TOGGLE.isPressed()) {
            ConfigHandler.globalEnabled = !ConfigHandler.globalEnabled;
            RenderTick.ores.clear();
            ModInstance.postChat(ConfigHandler.globalEnabled ? ModInstance.mcFormat("enabled", "a") : ModInstance.mcFormat("disabled", "7"));
            return;
        }

        if (ModInstance.KEY_MENU.isPressed()) {
            mc.displayGuiScreen(new GuiScreenXRayMenu());
            return;
        }
    }
}
