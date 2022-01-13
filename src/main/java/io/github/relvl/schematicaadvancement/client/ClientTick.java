package io.github.relvl.schematicaadvancement.client;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.github.relvl.schematicaadvancement.config.ConfigHandler;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import io.github.relvl.schematicaadvancement.reference.ColoredPosition;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ClientTick implements Runnable {
    private static final int DELAY = 200;

    private volatile int localPlyX;
    private volatile int localPlyY;
    private volatile int localPlyZ;

    private final Minecraft mc = Minecraft.getMinecraft();
    private long nextTimeMs = System.currentTimeMillis();
    private Thread thread;

    @SubscribeEvent
    public void tickEnd(TickEvent.ClientTickEvent event) {
        if ((event.phase == TickEvent.Phase.END) && (mc.thePlayer != null)) {
            localPlyX = MathHelper.floor_double(mc.thePlayer.posX);
            localPlyY = MathHelper.floor_double(mc.thePlayer.posY);
            localPlyZ = MathHelper.floor_double(mc.thePlayer.posZ);

            if (ConfigHandler.globalEnabled && ((this.thread == null) || !this.thread.isAlive()) && ((mc.theWorld != null))) {
                this.thread = new Thread(this);
                this.thread.setDaemon(false);
                this.thread.setPriority(Thread.NORM_PRIORITY);
                this.thread.start();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!this.thread.isInterrupted()) {
                if (ConfigHandler.globalEnabled && !ConfigHandler.blocks.isEmpty() && (mc != null) && (mc.theWorld != null) && (mc.thePlayer != null)) {
                    if (nextTimeMs > System.currentTimeMillis()) {
                        Thread.sleep(1);
                        continue;
                    }

                    List<ColoredPosition> temp = new ArrayList<ColoredPosition>();
                    int radius = ConfigHandler.getRadius();
                    int px = localPlyX;
                    int py = localPlyY;
                    int pz = localPlyZ;
                    for (int y = Math.max(0, py - 96); y < py + 32; y++) {
                        for (int x = px - radius; x < px + radius; x++) {
                            for (int z = pz - radius; z < pz + radius; z++) {
                                int id = Block.getIdFromBlock(mc.theWorld.getBlock(x, y, z));
                                int meta = mc.theWorld.getBlockMetadata(x, y, z);

                                for (BlockInfo ore : ConfigHandler.blocks) {
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

                    nextTimeMs = System.currentTimeMillis() + DELAY;
                }
                else {
                    this.thread.interrupt();
                }
            }
            this.thread = null;
        }
        catch (Exception exc) {
            System.out.println(" ClientTick Thread Interrupted!!! " + exc); // This shouldn't get called.
        }
    }
}
