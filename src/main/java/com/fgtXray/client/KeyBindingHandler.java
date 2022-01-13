package com.fgtXray.client;

import com.fgtXray.FgtXRay;
import com.fgtXray.client.gui.GuiSettings;
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

        if (OresSearch.searchList.isEmpty()) {
            OresSearch.fillDictionary();
        }

        if (FgtXRay.keyBind_keys[FgtXRay.keyIndex_toggleXray].isPressed()) {
            FgtXRay.drawOres = !FgtXRay.drawOres;
            RenderTick.ores.clear();
            return;
        }

        if (FgtXRay.keyBind_keys[FgtXRay.keyIndex_showXrayMenu].isPressed()) {
            mc.displayGuiScreen(new GuiSettings());
            return;
        }
    }
}
