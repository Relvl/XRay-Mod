package io.github.relvl.schematicaadvancement.client.gui;

import io.github.relvl.schematicaadvancement.ModInstance;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

/** @author Johnson on 13.01.2022 */
public class GuiBlockButton extends GuiButton {
    public static RenderItem drawItems = new RenderItem();

    private final BlockInfo info;
    private String _displayString;

    public GuiBlockButton(int id, BlockInfo info, int x, int y) {
        super(id, x, y, 100, 20, null);
        this.info = info;
        this.updateTitle();
    }

    private void updateTitle() {
        String name = info.name;
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }
        _displayString = name + ModInstance.mcFormat(": ", "7") + //
                         (info.enabled ? ModInstance.mcFormat("on", "a") : ModInstance.mcFormat("off", "7"));
    }

    public void toggleEnabled() {
        info.enabled = !info.enabled;
        ConfigHandler.update(info);
        updateTitle();
    }

    public BlockInfo getInfo() {
        return info;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        super.drawButton(mc, x, y);
        ItemStack is = info.getIdent().getItemStack();
        if (is != null) {
            int padding = (height - 16) / 2;
            RenderHelper.enableGUIStandardItemLighting();
            drawItems.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, is, xPosition + padding, yPosition + padding);

            this.drawString(mc.fontRenderer, _displayString, this.xPosition + 16 + padding * 2, this.yPosition + (this.height - 8) / 2, 16777120);
        }
    }
}
