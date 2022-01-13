package com.fgtXray;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.ConfigCategory;

public class Ident {
    private final int id;
    private final int meta;
    private final String pair;

    public Ident() {
        id = 0;
        meta = 0;
        pair = "0:0";
    }

    public Ident(int id, int meta) {
        this.id = id;
        this.meta = meta;
        this.pair = id + ":" + meta;
    }

    public Ident(ConfigCategory category) {
        String ident = category.getName();
        String[] p = ident.split(":");
        this.id = Integer.parseInt(p[0]);
        this.meta = Integer.parseInt(p[1]);
        this.pair = id + ":" + meta;
    }

    public Ident(Block block) {
        id = Block.getIdFromBlock(block);
        meta = 0;
        pair = id + ":0";
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    public String getPair() {
        return pair;
    }

    public boolean equals(int id, int meta) {
        return this.id == id && this.meta == meta;
    }

    @Override
    public String toString() {
        return pair;
    }
}
