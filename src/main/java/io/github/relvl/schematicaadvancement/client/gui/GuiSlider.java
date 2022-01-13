package io.github.relvl.schematicaadvancement.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiSlider extends GuiButton {
    private final float sliderMaxValue;
    private final String label;
    public float sliderValue;
    private boolean dragging;

    public GuiSlider(int id, int x, int y, String label, float startingValue, float maxValue) {
        super(id, x, y, 150, 20, label);
        this.label = label;
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
    }

    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.dragging) {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }
            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }
        }

        this.displayString = label + ": " + (int)(sliderValue * sliderMaxValue);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }
            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }
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