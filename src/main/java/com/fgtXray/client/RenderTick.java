package com.fgtXray.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.fgtXray.FgtXRay;
import com.fgtXray.reference.ColoredPosition;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class RenderTick {
    private final Minecraft mc = Minecraft.getMinecraft();
    public static List<ColoredPosition> ores = new ArrayList();

    /** Called when drawing the world. */
    @SubscribeEvent
    public void onRenderEvent(RenderWorldLastEvent event) {
        if (mc.theWorld != null && FgtXRay.drawOres) {
            float partialTicks = event.partialTicks;
            float playerX = (float)mc.thePlayer.posX;
            float playerY = (float)mc.thePlayer.posY;
            float playerZ = (float)mc.thePlayer.posZ;
            float diffX = (float)mc.thePlayer.prevPosX;
            float diffY = (float)mc.thePlayer.prevPosY;
            float diffZ = (float)mc.thePlayer.prevPosZ;
            float dx = diffX + (playerX - diffX) * partialTicks;
            float dy = diffY + (playerY - diffY) * partialTicks;
            float dz = diffZ + (playerZ - diffZ) * partialTicks;
            drawOres(dx, dy, dz);
        }
    }

    private static void drawOres(float px, float py, float pz) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1f);
        Tessellator tes = Tessellator.instance;

        // todo! java.util.ConcurrentModificationException
        for (ColoredPosition info : new ArrayList<ColoredPosition>(ores)) {
            int bx = info.x;
            int by = info.y;
            int bz = info.z;
            float f0 = 0.0f;
            float f1 = 1.0f;

            tes.startDrawing(GL11.GL_LINES);
            tes.setColorRGBA_I(info.color, 255);
            tes.setBrightness(200);

            // Bottom
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f1);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f0);

            // Top
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f0);
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f0);

            // Corners
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f1); // Top Left
            tes.addVertex(bx - px + f1, by - py + f0, bz - pz + f0);
            tes.addVertex(bx - px + f1, by - py + f1, bz - pz + f0); // Bottom Left
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f1);
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f1); // Top Right
            tes.addVertex(bx - px + f0, by - py + f0, bz - pz + f0);
            tes.addVertex(bx - px + f0, by - py + f1, bz - pz + f0); // Bottom Right

            tes.draw();
        }

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
