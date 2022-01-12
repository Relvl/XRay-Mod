package com.fgtXray.client.gui;

import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.OreInfo;
import net.minecraft.client.gui.GuiButton;

/** @author Johnson on 13.01.2022 */
public class GuiOreButton extends GuiButton {
    private final OreInfo ore;

    public GuiOreButton(int id, OreInfo ore, int x, int y) {
        super(id, x, y, 100, 20, ore.oreName);
        this.ore = ore;
        this.updateTitle();
    }

    private void updateTitle() {
        displayString = ore.oreName + ": " + (ore.draw ? "on" : "off");
    }

    public void toggleEnabled() {
        ore.draw = !ore.draw;
        ConfigHandler.update(ore.oreName, ore.draw);
        updateTitle();
    }

    public OreInfo getOre() {
        return ore;
    }

    public void deleteOre(){

    }
}
