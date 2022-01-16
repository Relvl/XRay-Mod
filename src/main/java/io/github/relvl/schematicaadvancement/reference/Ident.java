package io.github.relvl.schematicaadvancement.reference;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.ConfigCategory;

public class Ident {
    private final int id;
    private final int meta;
    private final String identPair;

    public Ident() {
        id = 0;
        meta = 0;
        identPair = "0:0";
    }

    public Ident(int id, int meta) {
        this.id = id;
        this.meta = meta;
        this.identPair = id + ":" + meta;
    }

    public Ident(ConfigCategory category) {
        String[] p = category.getName().split(":");
        this.id = Integer.parseInt(p[0]);
        this.meta = Integer.parseInt(p[1]);
        this.identPair = id + ":" + meta;
    }

    public Ident(Block block) {
        this.id = Block.getIdFromBlock(block);
        this.meta = 0;
        this.identPair = id + ":0";
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    public String getIdentPair() {
        return identPair;
    }

    public boolean equals(int id, int meta) {
        return this.id == id && (this.meta == -1 || this.meta == meta);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        Ident ident = (Ident)obj;
        return id == ident.id && meta == ident.meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, meta);
    }

    @Override
    public String toString() {
        return identPair;
    }
}
