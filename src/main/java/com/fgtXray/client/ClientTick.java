package com.fgtXray.client;

import java.util.ArrayList;
import java.util.List;

import com.fgtXray.FgtXRay;
import com.fgtXray.reference.BlockInfo;
import com.fgtXray.reference.OreInfo;
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

                    List<BlockInfo> temp = new ArrayList<BlockInfo>();
                    int radius = FgtXRay.distNumbers[FgtXRay.distIndex]; // Get the radius around the player to search.
                    int px = FgtXRay.localPlyX;
                    int py = FgtXRay.localPlyY;
                    int pz = FgtXRay.localPlyZ;
                    for (int y = Math.max(0, py - 96); y < py + 32; y++) {
                        for (int x = px - radius; x < px + radius; x++) {
                            for (int z = pz - radius; z < pz + radius; z++) {
                                int id = Block.getIdFromBlock(mc.theWorld.getBlock(x, y, z));
                                int meta = mc.theWorld.getBlockMetadata(x, y, z);

                                if (mc.theWorld.getBlock(x, y, z).hasTileEntity()) {
                                    meta = 0;
                                }

                                // Now we're actually checking if the current x,y,z block is in our searchList.
                                for (OreInfo ore : OresSearch.searchList) {
                                    // Dont check meta if its -1 (custom)
                                    if ((ore.draw) && (id == ore.id) && (meta == ore.meta)) {
                                        temp.add(new BlockInfo(x, y, z, ore.color)); // Add this block to the temp list
                                        break; // Found a match, move on to the next block.
                                    }
                                }
                            }
                        }
                    }
                    RenderTick.ores.clear();
                    RenderTick.ores.addAll(temp); // Add all our found blocks to the RenderTick.ores list. To be use by RenderTick when drawing.
                    nextTimeMs = System.currentTimeMillis() + delayMs; // Update the delay.
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
