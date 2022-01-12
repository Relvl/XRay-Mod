package com.fgtXray.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiEditOre extends GuiScreen {
    GuiTextField oreName; // Human readable name
    GuiTextField oreIdent; // oredict oreName or id:meta

    /** Called when the gui should be (re)created. */
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(97, 2, 2, 100, 20, "Delete")); // Delete button
        this.buttonList.add(new GuiButton(98, this.width - 102, this.height - 22, 100, 20, "Save")); // Save button
        this.buttonList.add(new GuiButton(99, 2, this.height - 22, 100, 20, "Cancel")); // Cancel button
        oreName = new GuiTextField(this.fontRendererObj, 104, this.height - 22, 200, 20);
    }

    /** Called on left click of GuiButton */
    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 98: // Save
                break;
            case 99: // Cancel
                mc.thePlayer.closeScreen();
                break;
            default:
                break;
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);
        if (oreName.isFocused()) {
            oreName.textboxKeyTyped(par1, par2);
        }
        else if (par2 == 1) // Close on esc
        {
            mc.thePlayer.closeScreen();
        }
    }

    /** Don't pause the game in single player. */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        oreName.updateCursorCounter();
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();  // Draws the opaque black background
        oreName.drawTextBox();
        super.drawScreen(x, y, f);
    }

    @Override
    public void mouseClicked(int x, int y, int mouse) {
        super.mouseClicked(x, y, mouse);
        oreName.mouseClicked(x, y, mouse);
        // Right clicked
        if (mouse == 1) {
            for (int i = 0; i < this.buttonList.size(); i++) {
                GuiButton button = (GuiButton)this.buttonList.get(i);
                //func_146115_a() returns true if the button is being hovered
                if (button.func_146115_a()) {
                    /* TODO: Allow editing of ores
                     * if( button.id == 99 ){ */
                }
            }
        }
    }
}
