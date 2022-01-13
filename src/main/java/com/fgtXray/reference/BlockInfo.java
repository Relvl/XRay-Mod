package com.fgtXray.reference;

import com.fgtXray.Ident;
import net.minecraft.block.Block;

public class BlockInfo {
    public String name;
    private Ident ident;
    public int color;
    public boolean enabled;

    public BlockInfo(String name, Ident ident, int color, boolean enabled) {
        this.name = name;
        this.ident = ident;
        this.color = color;
        this.enabled = enabled;
    }

    public BlockInfo(String name, Block block, int color, boolean enabled) {
        this.name = name;
        this.ident = new Ident(block);
        this.color = color;
        this.enabled = enabled;
    }

    public BlockInfo(Block block, int color, boolean enabled) {
        this.name = block.getLocalizedName();
        this.ident = new Ident(block);
        this.color = color;
        this.enabled = enabled;
    }

    public Ident getIdent() {
        return ident;
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public void update(String oreName, int color) {
        this.name = oreName;
        this.color = color;
        this.enabled = true;
    }
}
