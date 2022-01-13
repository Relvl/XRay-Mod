package io.github.relvl.schematicaadvancement.client.gui;

import io.github.relvl.schematicaadvancement.ModInstance;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import net.minecraft.client.gui.GuiButton;

/** @author Johnson on 13.01.2022 */
public class GuiBlockButton extends GuiButton {
    private final BlockInfo info;

    public GuiBlockButton(int id, BlockInfo info, int x, int y) {
        super(id, x, y, 100, 20, info.name);
        this.info = info;
        this.updateTitle();
    }

    private void updateTitle() {
        String name = info.name;
        if (name.length() > 12) {
            name = name.substring(0, 12);
        }
        displayString = name + ModInstance.mcFormat(": ", "7") + //
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
}
