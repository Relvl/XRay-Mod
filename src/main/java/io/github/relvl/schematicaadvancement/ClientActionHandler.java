package io.github.relvl.schematicaadvancement;

import java.util.Arrays;
import java.util.function.Consumer;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.relvl.schematicaadvancement.client.gui.GuiScreenBlockEdit;
import io.github.relvl.schematicaadvancement.client.gui.GuiSlider;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;

/** @author karpov-em on 14.02.2022 */
public class ClientActionHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onButtonAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        Action.of(event.button.id).getAction().accept(event);
    }

    public enum Action {
        DISTANCE_CHANGED("", event -> {
            GuiSlider slider = (GuiSlider)event.button;
            ConfigHandler.setRadiusIndex(slider.sliderValue);
        }),
        ADD_BLOCK("Add new block", event -> {
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBlockEdit());
        }),
        XRAY_SWITCH("Enables or disables X-Ray function", event -> {
            ConfigHandler.globalEnabled = !ConfigHandler.globalEnabled;
            event.gui.initGui();
        }),

        NONE("", event -> {});

        private final Consumer<GuiScreenEvent.ActionPerformedEvent.Post> action;
        private final String tooltip;

        Action(String tooltip, Consumer<GuiScreenEvent.ActionPerformedEvent.Post> action) {
            this.action = action;
            this.tooltip = tooltip;
        }

        public int getId() {
            return -this.ordinal() - 50;
        }

        public String getTooltip() {
            return tooltip;
        }

        public static Action of(int id) {
            return Arrays.stream(values()).filter(it -> it.getId() == id).findFirst().orElse(NONE);
        }

        public Consumer<GuiScreenEvent.ActionPerformedEvent.Post> getAction() {
            return action;
        }
    }
}
