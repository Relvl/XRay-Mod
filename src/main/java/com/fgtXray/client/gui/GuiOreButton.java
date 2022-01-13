package com.fgtXray.client.gui;

import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.BlockInfo;
import net.minecraft.client.gui.GuiButton;

/** @author Johnson on 13.01.2022 */
public class GuiOreButton extends GuiButton {
    private final BlockInfo info;

    public GuiOreButton(int id, BlockInfo info, int x, int y) {
        super(id, x, y, 100, 20, info.name);
        this.info = info;
        this.updateTitle();
    }

    private void updateTitle() {
        displayString = info.name + ": " + (info.enabled ? "on" : "off");
    }

    public void toggleEnabled() {
        info.enabled = !info.enabled;
        ConfigHandler.update(info);
        updateTitle();
    }

    public BlockInfo getInfo() {
        return info;
    }
}
