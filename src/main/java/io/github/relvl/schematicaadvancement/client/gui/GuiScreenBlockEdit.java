package io.github.relvl.schematicaadvancement.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import io.github.relvl.schematicaadvancement.reference.Ident;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class GuiScreenBlockEdit extends GuiScreen {
    private static final String IDENT_PLACEHOLDER = "ID:META";

    private GuiTextField oreName;
    private GuiTextField oreIdent;

    private GuiSlider rSlider;
    private GuiSlider gSlider;
    private GuiSlider bSlider;
    private GuiButton addButton;

    private boolean oreNameCleared;
    private boolean oreIdentCleared;

    private String initialOreName = "";
    private String initialOreIdent = "";
    private int initialOreColor = 0;

    public GuiScreenBlockEdit() {
        this.initialOreIdent = IDENT_PLACEHOLDER;
        this.initialOreName = "Name of block";
        this.initialOreColor = 0x00FF00;
    }

    public GuiScreenBlockEdit(BlockInfo ore) {
        this.initialOreIdent = ore.getIdent().getIdentPair();
        this.initialOreName = ore.name;
        this.initialOreColor = ore.color;
    }

    @Override
    public void initGui() {
        String saveButtonTitle = initialOreIdent.equals(IDENT_PLACEHOLDER) ? "Add" : "Save";

        getScreenButtons().add(addButton = new GuiButton(98, width / 2 + 5, height / 2 + 58, 108, 20, saveButtonTitle));
        getScreenButtons().add(new GuiButton(99, width / 2 - 108, height / 2 + 58, 108, 20, "Cancel"));
        getScreenButtons().add(rSlider = new GuiSlider(1, width / 2 - 108, height / 2 - 63, "Red", 0, 255));
        getScreenButtons().add(gSlider = new GuiSlider(2, width / 2 - 108, height / 2 - 40, "Green", 0, 255));
        getScreenButtons().add(bSlider = new GuiSlider(3, width / 2 - 108, height / 2 - 17, "Blue", 0, 255));

        oreName = new GuiTextField(fontRendererObj, width / 2 - 108, height / 2 + 8, 220, 20);
        oreIdent = new GuiTextField(fontRendererObj, width / 2 - 108, height / 2 + 32, 220, 20);

        oreName.setText(initialOreName);
        oreIdent.setText(initialOreIdent);
        setColorToSlider(initialOreColor);
    }

    private List<GuiButton> getScreenButtons() {
        //noinspection unchecked
        return this.buttonList;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 98: // Add
                try {
                    String[] pair = oreIdent.getText().split(":");
                    int id = Integer.parseInt(pair[0]);
                    int meta = Integer.parseInt(pair[1]);
                    Ident ident = new Ident(id, meta);
                    ConfigHandler.addBlock(oreName.getText(), ident, getSliderColor());
                    mc.displayGuiScreen(new GuiScreenXRayMenu());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 99: // Cancel
                mc.displayGuiScreen(new GuiScreenXRayMenu());
                break;
            default:
                break;
        }
    }

    private int getSliderColor() {
        int color = (int)(rSlider.sliderValue * 255);
        color = (color << 8) + (int)(gSlider.sliderValue * 255);
        color = (color << 8) + (int)(bSlider.sliderValue * 255);
        return color;
    }

    private void setColorToSlider(int color) {
        rSlider.sliderValue = (color >> 16 & 0xFF) / 255.0F;
        gSlider.sliderValue = (color >> 8 & 0xFF) / 255.0F;
        bSlider.sliderValue = (color >> 0 & 0xFF) / 255.0F;
    }

    @Override
    protected void keyTyped(char charTyped, int keyCode) {
        super.keyTyped(charTyped, keyCode);
        if (oreName.isFocused()) {
            oreName.textboxKeyTyped(charTyped, keyCode);
            if (keyCode == 15) {
                oreName.setFocused(false);
                if (!oreIdentCleared) {
                    oreIdent.setText("");
                }
                oreIdent.setFocused(true);
            }
        }
        else if (oreIdent.isFocused()) {
            oreIdent.textboxKeyTyped(charTyped, keyCode);
            if (keyCode == 28) {
                actionPerformed(addButton);
            }
        }
        else {
            switch (keyCode) {
                case 15:
                    if (!oreNameCleared) {
                        oreName.setText("");
                    }
                    oreName.setFocused(true);
                    break;
                case 1: // Exit on escape
                    mc.displayGuiScreen(new GuiScreenXRayMenu());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        oreName.updateCursorCounter();
        oreIdent.updateCursorCounter();
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(new ResourceLocation("schematicaadvancement:textures/gui/oreAddBackground.png"));
        drawTexturedModalRect(width / 2 - 125, height / 2 - 95, 0, 0, 256, 205);

        FontRenderer fr = mc.fontRenderer;
        fr.drawString("Add an Ore", width / 2 - 108, height / 2 - 80, 0x404040);

        oreName.drawTextBox();
        oreIdent.drawTextBox();
        super.drawScreen(x, y, f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(rSlider.sliderValue, gSlider.sliderValue, bSlider.sliderValue);
        GL11.glVertex2d(width / 2 + 46, height / 2 - 63); // TL
        GL11.glVertex2d(width / 2 + 46, height / 2 + 3); // BL
        GL11.glVertex2d(width / 2 + 113, height / 2 + 3); // BR
        GL11.glVertex2d(width / 2 + 113, height / 2 - 63); // TR
        GL11.glEnd();
    }

    @Override
    public void mouseClicked(int x, int y, int mouse) {
        super.mouseClicked(x, y, mouse);
        oreName.mouseClicked(x, y, mouse);
        oreIdent.mouseClicked(x, y, mouse);

        if (oreName.isFocused() && !oreNameCleared && "Name of block".equals(oreName.getText())) {
            oreName.setText("");
            oreNameCleared = true;
        }
        if (oreIdent.isFocused() && !oreIdentCleared && IDENT_PLACEHOLDER.equals(oreIdent.getText())) {
            oreIdent.setText("");
            oreIdentCleared = true;
        }

        if (!oreName.isFocused() && oreNameCleared && oreName.getText().isEmpty()) {
            oreNameCleared = false;
            oreName.setText("Name of block");
        }
        if (!oreIdent.isFocused() && oreIdentCleared && oreIdent.getText().isEmpty()) {
            oreIdentCleared = false;
            oreIdent.setText(IDENT_PLACEHOLDER);
        }
    }
}
