package com.fgtXray.client;

import java.util.ArrayList;
import java.util.List;

import com.fgtXray.FgtXRay;
import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.BlockInfo;
import com.fgtXray.reference.ColoredPosition;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ClientTick implements Runnable {
    private final Minecraft mc = Minecraft.getMinecraft();
    private long nextTimeMs = System.currentTimeMillis();
    private final int delayMs = 200;
    private Thread thread;

    @SubscribeEvent
    public void tickEnd(TickEvent.ClientTickEvent event) {
        if ((event.phase == TickEvent.Phase.END) && (mc.thePlayer != null)) {
            FgtXRay.localPlyX = MathHelper.floor_double(mc.thePlayer.posX);
            FgtXRay.localPlyY = MathHelper.floor_double(mc.thePlayer.posY);
            FgtXRay.localPlyZ = MathHelper.floor_double(mc.thePlayer.posZ);

            if (FgtXRay.drawOres && ((this.thread == null) || !this.thread.isAlive()) && ((mc.theWorld != null))) {
                this.thread = new Thread(this);
                this.thread.setDaemon(false);
                this.thread.setPriority(Thread.MAX_PRIORITY);
                this.thread.start();
            }
        }
    }

    /** Our thread code for finding ores near the player. */
    @Override
    public void run() {
        try {
            // Check the internal interrupt flag. Exit thread if set.
            while (!this.thread.isInterrupted()) {
                if (FgtXRay.drawOres && !OresSearch.searchList.isEmpty() && (mc != null) && (mc.theWorld != null) && (mc.thePlayer != null)) {
                    // Delay to avoid spamming ore updates.
                    if (nextTimeMs > System.currentTimeMillis()) {
                        continue;
                    }

                    List<ColoredPosition> temp = new ArrayList<ColoredPosition>();
                    int radius = ConfigHandler.getDistance();
                    int px = FgtXRay.localPlyX;
                    int py = FgtXRay.localPlyY;
                    int pz = FgtXRay.localPlyZ;
                    for (int y = Math.max(0, py - 96); y < py + 32; y++) {
                        for (int x = px - radius; x < px + radius; x++) {
                            for (int z = pz - radius; z < pz + radius; z++) {
                                int id = Block.getIdFromBlock(mc.theWorld.getBlock(x, y, z));
                                int meta = mc.theWorld.getBlockMetadata(x, y, z);

                                for (BlockInfo ore : OresSearch.searchList) {
                                    if (ore.enabled && ore.getIdent().equals(id, meta)) {
                                        temp.add(new ColoredPosition(x, y, z, ore.color));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    RenderTick.ores.clear();
                    RenderTick.ores.addAll(temp);
                    nextTimeMs = System.currentTimeMillis() + delayMs;
                }
                else {
                    this.thread.interrupt(); // Kill the thread if we turn off xray or the player/world object becomes null.
                }
            }
            //System.out.println(" --- Thread Exited Cleanly! ");
            this.thread = null;
        }
        catch (Exception exc) {
            System.out.println(" ClientTick Thread Interrupted!!! " + exc); // This shouldn't get called.
        }
    }
}
