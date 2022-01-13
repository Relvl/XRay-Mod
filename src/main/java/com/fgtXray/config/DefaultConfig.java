package com.fgtXray.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fgtXray.reference.BlockInfo;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class DefaultConfig {
    private static final Pattern NAMES_PTN = Pattern.compile("\\s+");
    private static final List<BlockInfo> defaults = new ArrayList<BlockInfo>() {{
        add(new BlockInfo(Blocks.diamond_ore, 0x8888FF, false));
        add(new BlockInfo(Blocks.diamond_block, 0x8888FF, false));
        add(new BlockInfo(Blocks.emerald_ore, 0x008810, true));
        add(new BlockInfo(Blocks.gold_ore, 0xFFFF00, false));
        add(new BlockInfo(Blocks.gold_block, 0xFFFF00, false));
        add(new BlockInfo(Blocks.redstone_ore, 0xFF0000, false));
        add(new BlockInfo(Blocks.iron_ore, 0xAA7525, false));
        add(new BlockInfo(Blocks.quartz_ore, 0x8888FF, false));
        add(new BlockInfo(Blocks.coal_ore, 0x000000, false));
        add(new BlockInfo(Blocks.redstone_wire, 0xFF0000, false));
        add(new BlockInfo(Blocks.chest, 0xFF00FF, true));
    }};

    public static void create(Configuration config) {
        config.get(Configuration.CATEGORY_GENERAL, "radius", 0);
        for (BlockInfo ore : defaults) {
            String categoryName = "blocks." + ore.getIdent().getPair();
            config.get(categoryName, "name", "SOMETHINGBROKE").set(ore.name);
            config.get(categoryName, "color", -1).set(ore.color);
            config.get(categoryName, "enabled", false).set(ore.enabled);
        }
    }
}
