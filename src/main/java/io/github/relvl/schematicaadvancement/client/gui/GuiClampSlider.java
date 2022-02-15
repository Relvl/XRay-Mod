package io.github.relvl.schematicaadvancement.client.gui;

import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiClampSlider extends GuiSlider {
    private int highValue;
    private boolean draggingHigh;

    public GuiClampSlider(int id, int x, int y, int width, String label, int lowValue, int highValue, int maxValue) {
        super(id, x, y, width, label, lowValue, maxValue, String::valueOf);
        this.highValue = highValue;
    }

    /** Actually it's a render method... */
    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {
        int oldValue = value;
        int oldHighValue = highValue;

        value = moveTip(dragging, x, oldValue);
        highValue = moveTip(draggingHigh, x, oldHighValue);

        if (highValue < value && dragging) {
            highValue = value;
        }
        if (highValue < value && draggingHigh) {
            value = highValue;
        }

        displayString = String.format("%s: [%d..%d]", label, value, highValue);

        drawTip(highValue, x, y);
        drawTip(value, x, y);

        if (value != oldValue || highValue != oldHighValue) {
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(mc.currentScreen, this, Collections.emptyList()));
        }
    }

    @Override
    public boolean hasMousePressed(Minecraft mc, int x, int y) {
        if (isMouseOver(value, x, y)) {
            this.dragging = true;
            return true;
        }
        if (isMouseOver(highValue, x, y)) {
            this.draggingHigh = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        super.mouseReleased(par1, par2);
        draggingHigh = false;
    }

    public int getHighValue() {
        return highValue;
    }
}