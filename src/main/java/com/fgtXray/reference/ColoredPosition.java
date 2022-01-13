// This class (structure?) is for holding the blocks x,y,z and color to draw.
// This gets copied and accessed by RenderTick to draw the boxes around found ores/blocks.

package com.fgtXray.reference;

public class ColoredPosition {
    public int x;
    public int y;
    public int z;
    public int color;

    public ColoredPosition(int bx, int by, int bz, int c) {
        this.x = bx;
        this.y = by;
        this.z = bz;
        this.color = c;
    }
}
