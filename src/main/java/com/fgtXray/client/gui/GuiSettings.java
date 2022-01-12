package com.fgtXray.client.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fgtXray.FgtXRay;
import com.fgtXray.client.OresSearch;
import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.OreInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public class GuiSettings extends GuiScreen {
    private static final int PAGE_SIZE = 10; // 14 actually

    private final Map<Integer, GuiOreButton> oreButtons = new HashMap<Integer, GuiOreButton>();

    private GuiButton aNextButton;
    private GuiButton aPrevButton;

    private int pageCurrent;
    private int pageMax;
    public boolean called;

    @Override
    public void initGui() {
        getScreenButtons().clear();
        oreButtons.clear();

        int x = width / 2 - 100;
        int y = height / 2 - 100;

        int offsetFrom = PAGE_SIZE * pageCurrent;
        int offsetTo = Math.min(PAGE_SIZE * pageCurrent + PAGE_SIZE, OresSearch.searchList.size());
        List<OreInfo> pageList = OresSearch.searchList.subList(offsetFrom, offsetTo);
        pageMax = (OresSearch.searchList.size() / PAGE_SIZE);

        int row = 0;
        int col = 0;
        int id = 1000;
        for (OreInfo ore : pageList) {
            GuiOreButton button = new GuiOreButton(id, ore, x + col * 100, y + row * 20);
            oreButtons.put(id, button);
            getScreenButtons().add(button);
            row++;
            if (row >= PAGE_SIZE / 2) {
                row = 0;
                col++;
            }
            id++;
        }

        getScreenButtons().add(new GuiButton(97, width / 2 - 67, height / 2 + 52, 55, 20, "Add Ore"));
        getScreenButtons().add(new GuiButton(98, width / 2 - 10, height / 2 + 52, 82, 20, "Distance: " + FgtXRay.distStrings[FgtXRay.distIndex]));
        getScreenButtons().add(aNextButton = new GuiButton(-150, width / 2 + 75, height / 2 + 52, 30, 20, ">"));
        getScreenButtons().add(aPrevButton = new GuiButton(-151, width / 2 - 100, height / 2 + 52, 30, 20, "<"));

        if (OresSearch.searchList.size() <= PAGE_SIZE) {
            aNextButton.enabled = false;
            aPrevButton.enabled = false;
        }
        if (pageCurrent == 0) {
            aPrevButton.enabled = false;
        }
        if (pageCurrent == pageMax) {
            aNextButton.enabled = false;
        }
    }

    private List<GuiButton> getScreenButtons() {
        //noinspection unchecked
        return this.buttonList;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        // Called on left click of GuiButton
        switch (button.id) {
            case 98: // Distance
                if (FgtXRay.distIndex < FgtXRay.distNumbers.length - 1) {
                    FgtXRay.distIndex++;
                }
                else {
                    FgtXRay.distIndex = 0;
                }
                ConfigHandler.update("searchdist", false);
                break;

            case 97: // New Ore
                mc.displayGuiScreen(new GuiNewOre());
                break;

            case -150: // Next page
                if (pageCurrent < pageMax) {
                    pageCurrent++;
                }
                break;

            case -151: // Prev page
                if (pageCurrent > 0) {
                    pageCurrent--;
                }
                break;

            default:
                GuiOreButton oreButton = oreButtons.get(button.id);
                if (oreButton != null) {
                    oreButton.toggleEnabled();
                }
                break;
        }

        this.initGui();
    }

    @Override
    protected void keyTyped(char c, int keyCode) {
        super.keyTyped(c, keyCode);
        if ((keyCode == 1) || (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) || keyCode == FgtXRay.keyBind_keys[FgtXRay.keyIndex_showXrayMenu].getKeyCode()) {
            // Close on esc, inventory key or keybind
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // this should be moved to some sort of utility package but fuck it :).
    // this removes the stupid power of 2 rule that comes with minecraft.
    private static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0, 1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1, 0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(new ResourceLocation("fgtxray:textures/gui/Background.png"));
        drawTexturedQuadFit(width / 2 - 110, height / 2 - 110, 229, 193, 0);
        super.drawScreen(x, y, f);

        for (GuiButton button : getScreenButtons()) {
            if (button.func_146115_a()) { //func_146115_a() returns true if the button is being hovered
                GuiOreButton oreButton = oreButtons.get(button.id);
                if (oreButton == null) {
                    if (button.enabled) {
                        switch (button.id) {
                            case -150:
                                this.func_146283_a(Arrays.asList("Next page"), x, y);
                                break;
                            case -151:
                                this.func_146283_a(Arrays.asList("Previous page"), x, y);
                                break;
                        }
                    }
                }
                else {
                    this.func_146283_a(Arrays.asList(oreButton.getOre().oreName, "§7LMB: enable/disable§r", "§7RMB: Edit§r", "§7Shift+RMB: Delete§r"), x, y);
                }
                break;
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouse) {
        super.mouseClicked(x, y, mouse);
        if (mouse == 1) {
            // Right clicked
            for (GuiButton button : getScreenButtons()) {
                if (button.func_146115_a()) { //func_146115_a() returns true if the button is being hovered

                    if (button.id == 98) { // distance
                        if (FgtXRay.distIndex > 0) {
                            FgtXRay.distIndex--;
                        }
                        else {
                            FgtXRay.distIndex = FgtXRay.distNumbers.length - 1;
                        }
                        ConfigHandler.update("searchdist", false);
                        this.initGui();
                        return;
                    }

                    GuiOreButton oreButton = oreButtons.get(button.id);
                    if (oreButton != null) {
                        if (isShiftKeyDown()) {
                            oreButton.deleteOre();
                            this.initGui();
                        }
                        else {
                            mc.displayGuiScreen(new GuiNewOre(oreButton.getOre()));
                        }
                    }

                    return;
                }
            }
        }
    }
}