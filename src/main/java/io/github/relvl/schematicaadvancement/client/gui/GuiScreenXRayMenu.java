package io.github.relvl.schematicaadvancement.client.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.relvl.schematicaadvancement.ClientActionHandler;
import io.github.relvl.schematicaadvancement.ModInstance;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings({"ParameterNameDiffersFromOverriddenParameter", "ClassHasNoToStringMethod"})
public class GuiScreenXRayMenu extends GuiScreen {
    private static final int PAGE_SIZE = 14;

    private final Map<Integer, GuiBlockButton> oreButtons = new HashMap<>();

    private int pageCurrent;
    private int pageMax;

    @Override
    public void initGui() {
        getScreenButtons().clear();
        oreButtons.clear();

        int x = width / 2 - 100;
        int y = height / 2 - 100;

        int offsetFrom = PAGE_SIZE * pageCurrent;
        int offsetTo = Math.min(PAGE_SIZE * pageCurrent + PAGE_SIZE, ConfigHandler.blocks.size());
        List<BlockInfo> pageList = ConfigHandler.blocks.subList(offsetFrom, offsetTo);
        pageMax = (ConfigHandler.blocks.size() / PAGE_SIZE);

        int row = 0;
        int col = 0;
        int id = 1000;
        for (BlockInfo ore : pageList) {
            GuiBlockButton button = new GuiBlockButton(id, ore, x + col * 100, y + row * 20);
            oreButtons.put(id, button);
            getScreenButtons().add(button);
            row++;
            if (row >= PAGE_SIZE / 2) {
                row = 0;
                col++;
            }
            id++;
        }

        int texWidth = 218;

        getScreenButtons().add(new GuiButton(ClientActionHandler.Action.XRAY_SWITCH.getId(),
            width / 2 - texWidth / 2 - 30,
            height / 2 - 80,
            30,
            20,
            ConfigHandler.globalEnabled ? ModInstance.mcFormat("ON", "a") : ModInstance.mcFormat("OFF", "7")
        ));
        getScreenButtons().add(new GuiButton(ClientActionHandler.Action.ADD_BLOCK.getId(), width / 2 - texWidth / 2 - 30, height / 2 - 100, 30, 20, "+"));

        GuiButton aNextButton;
        GuiButton aPrevButton;

        getScreenButtons().add(aPrevButton = new GuiButton(-151, width / 2 - texWidth / 2 - 30, height / 2 + 52, 30, 20, "<"));
        getScreenButtons().add(aNextButton = new GuiButton(-150, width / 2 + texWidth / 2 + 6, height / 2 + 52, 30, 20, ">"));

        getScreenButtons().add(new GuiSlider(ClientActionHandler.Action.DISTANCE_CHANGED.getId(),
            width / 2 - 105,
            height / 2 + 80,
            texWidth - 2,
            "Distance",
            ConfigHandler.getRadiusIndex(),
            7,
            dis -> String.valueOf(ConfigHandler.getRadius())
        ));

        if (ConfigHandler.blocks.size() <= PAGE_SIZE) {
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
                GuiBlockButton oreButton = oreButtons.get(button.id);
                if (oreButton != null) {
                    oreButton.toggleEnabled();
                }
                return;
        }

        this.initGui();
    }

    @Override
    protected void keyTyped(char c, int keyCode) {
        super.keyTyped(c, keyCode);
        if ((keyCode == 1) || (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) || keyCode == ModInstance.KEY_MENU.getKeyCode()) {
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
        mc.renderEngine.bindTexture(new ResourceLocation("schematicaadvancement:textures/gui/Background.png"));
        drawTexturedQuadFit(width / 2 - 110, height / 2 - 110, 229, 193, 0);
        super.drawScreen(x, y, f);

        for (GuiButton button : getScreenButtons()) {
            if (button.func_146115_a()) { //func_146115_a() returns true if the button is being hovered
                GuiBlockButton oreButton = oreButtons.get(button.id);
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

                        ClientActionHandler.Action action = ClientActionHandler.Action.of(button.id);
                        if (action != null && !action.getTooltip().isEmpty()) {
                            this.func_146283_a(Arrays.asList(action.getTooltip()), x, y);
                        }
                    }
                }
                else {
                    this.func_146283_a(Arrays.asList(String.format("%s %s", oreButton.getInfo().name, ModInstance.mcFormat(oreButton.getInfo().getIdent().getIdentPair(), "3")),
                        ModInstance.mcFormat("LMB: Enable/Disable", "7"),
                        ModInstance.mcFormat("RMB: Edit", "7"),
                        ModInstance.mcFormat("Shift+RMB: Delete", "c")
                    ), x, y);
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
                    GuiBlockButton oreButton = oreButtons.get(button.id);
                    if (oreButton != null) {
                        if (isShiftKeyDown()) {
                            ConfigHandler.remove(oreButton.getInfo());
                            this.initGui();
                        }
                        else {
                            mc.displayGuiScreen(new GuiScreenBlockEdit(oreButton.getInfo()));
                        }
                    }

                    return;
                }
            }
        }
    }
}