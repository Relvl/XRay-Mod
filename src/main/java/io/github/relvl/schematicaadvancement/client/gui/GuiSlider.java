package io.github.relvl.schematicaadvancement.client.gui;

import java.util.Collections;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiSlider extends GuiButton {
    private final String label;

    public int sliderValue;
    private final int sliderMaxValue;

    private boolean dragging;
    private final Function<Integer, String> valueTransformator;

    public GuiSlider(int id, int x, int y, int width, String label, int startingValue, int maxValue, Function<Integer, String> valueTransformator) {
        super(id, x, y, width, 20, label);
        this.label = label;
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
        this.valueTransformator = valueTransformator;
    }

    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    /** Actually its a render method... */
    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {
        float position = (float)this.sliderValue / this.sliderMaxValue;
        if (this.dragging) {
            position = (x - (this.xPosition + 4 - ((float)this.width / this.sliderMaxValue) / 2.0f)) / (this.width - 8);
            if (position < 0.0F) {
                position = 0.0F;
            }
            if (position > 1.0F) {
                position = 1.0F;
            }
            int oldValue = this.sliderValue;
            this.sliderValue = (int)(position * sliderMaxValue);
            if (this.sliderValue != oldValue) {
                MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(mc.currentScreen, this, Collections.emptyList()));
            }
            position = (float)this.sliderValue / this.sliderMaxValue;
        }

        this.displayString = label + ": " + valueTransformator.apply((int)(position * sliderMaxValue));

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.xPosition + (int)(position * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int)(position * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (super.mousePressed(mc, x, y)) {
            float position = (x - (this.xPosition + 4)) / (float)(this.width - 8);
            if (position < 0.0F) {
                position = 0.0F;
            }
            if (position > 1.0F) {
                position = 1.0F;
            }
            this.sliderValue = (int)(position * sliderMaxValue);
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(mc.currentScreen, this, Collections.emptyList()));

            this.dragging = true;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }
}