package com.fgtXray.client.gui;

import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.BlockInfo;
import net.minecraft.client.gui.GuiButton;

/** @author Johnson on 13.01.2022 */
public class GuiOreButton extends GuiButton {
    private final BlockInfo ore;

    public GuiOreButton(int id, BlockInfo ore, int x, int y) {
        super(id, x, y, 100, 20, ore.name);
        this.ore = ore;
        this.updateTitle();
    }

    private void updateTitle() {
        displayString = ore.name + ": " + (ore.enabled ? "on" : "off");
    }

    public void toggleEnabled() {
        ore.enabled = !ore.enabled;
        ConfigHandler.update(ore.name, ore.enabled);
        updateTitle();
    }

    public BlockInfo getOre() {
        return ore;
    }

    public void deleteOre(){

    }
}
