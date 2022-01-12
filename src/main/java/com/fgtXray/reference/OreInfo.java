// Structure for damn near everything.
package com.fgtXray.reference;

public class OreInfo {
    public String oreName; // Clean name of this ore
    public int id;         // Id of this block
    public int meta;       // Metadata value of this block. 0 otherwise.
    public int color;       // Color in 0xRRGGBB to draw.
    public boolean draw;   // Should we draw this ore?

    public OreInfo(String name, int id, int meta, int color, boolean draw) {
        this.oreName = name;
        this.id = id;
        this.meta = meta;
        this.color = color;
        this.draw = draw;
    }

    public void disable() {
        this.draw = false;
    }

    public void enable() {
        this.draw = true;
    }

    public void update(String oreName, int color) {
        this.oreName = oreName;
        this.color = color;
        this.draw = true;
    }
}
