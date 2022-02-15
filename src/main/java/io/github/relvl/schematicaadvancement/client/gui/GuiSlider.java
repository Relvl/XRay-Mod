package io.github.relvl.schematicaadvancement.client.gui;

import java.util.Collections;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiSlider extends GuiButton {
    private final Function<Integer, String> valueTransformator;
    private final int maxValue;

    protected final String label;
    protected int value;
    protected boolean dragging;

    public GuiSlider(int id, int x, int y, int width, String label, int value, int maxValue, Function<Integer, String> valueTransformator) {
        super(id, x, y, width, 20, label);
        this.label = label;
        this.value = value;
        this.maxValue = maxValue;
        this.valueTransformator = valueTransformator;
    }

    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    /** Actually it's a render method... */
    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {
        int oldValue = value;
        value = moveTip(dragging, x, oldValue);
        this.displayString = label + ": " + valueTransformator.apply(value);
        drawTip(value, x, y);
        if (value != oldValue) {
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(mc.currentScreen, this, Collections.emptyList()));
        }
    }

    protected void drawTip(int value, int x, int y) {
        float color = isMouseOver(value, x, y) ? 1.0f : 0.75f;
        GL11.glColor4f(color, color, color, 1.0F);
        drawTexturedModalRect(xPosition + (int)(((float)value / maxValue) * (width - 8)), yPosition, 0, 66, 4, 20);
        drawTexturedModalRect(xPosition + (int)(((float)value / maxValue) * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
    }

    protected int moveTip(boolean dragging, int x, int oldValue) {
        if (dragging) {
            float position = (x - (xPosition + 4 - ((float)width / maxValue) / 2.0f)) / (width - 8);
            if (position < 0.0F) {
                position = 0.0F;
            }
            if (position > 1.0F) {
                position = 1.0F;
            }
            return (int)(position * maxValue);
        }
        return oldValue;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (!super.mousePressed(mc, x, y)) {
            return false;
        }
        return hasMousePressed(mc, x, y);
    }

    protected boolean hasMousePressed(Minecraft mc, int x, int y) {
        float position = (x - (this.xPosition + 4)) / (float)(this.width - 8);
        if (position < 0.0F) {
            position = 0.0F;
        }
        if (position > 1.0F) {
            position = 1.0F;
        }
        this.value = (int)(position * maxValue);
        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(mc.currentScreen, this, Collections.emptyList()));

        this.dragging = true;
        return true;
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }

    protected boolean isMouseOver(int value, int mouseX, int mouseY) {
        if (mouseY < yPosition || mouseY > yPosition + height) {
            return false;
        }
        int coord = xPosition + (int)((width - 8) * ((float)value / maxValue));
        return mouseX >= coord && mouseX <= coord + 8;
    }

    public int getValue() {
        return value;
    }
}