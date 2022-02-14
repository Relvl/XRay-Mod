package io.github.relvl.schematicaadvancement.config;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.relvl.schematicaadvancement.reference.BlockInfo;
import io.github.relvl.schematicaadvancement.reference.Ident;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    private static final int[] radiuses = {8, 16, 32, 48, 64, 80, 128, 256};
    private static int radiusIndex;
    private static Configuration config;

    public static final List<BlockInfo> blocks = new ArrayList<>();
    public static boolean globalEnabled;

    public static void setup(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());

        if (config.getCategoryNames().isEmpty()) {
            DefaultConfig.create(config);
            config.save();
        }

        radiusIndex = config.get(Configuration.CATEGORY_GENERAL, "radiusIdx", 0).getInt();

        for (String category : config.getCategoryNames()) {
            ConfigCategory cat = config.getCategory(category);
            if (category.startsWith("blocks.")) {
                try {
                    String name = cat.get("name").getString();
                    int color = cat.get("color").getInt();
                    boolean enabled = cat.get("enabled").getBoolean();
                    Ident ident = new Ident(cat);
                    blocks.add(new BlockInfo(name, ident, color, enabled));
                }
                catch (Exception e) {
                    config.removeCategory(cat);
                }
            }
            else if (!category.equals(Configuration.CATEGORY_GENERAL) && !"blocks".equals(category)) {
                config.removeCategory(cat);
            }
        }

        config.save();
    }

    public static void changeDistance(int step) {
        if (step > 0) {
            radiusIndex++;
            if (radiusIndex >= radiuses.length) {
                radiusIndex = 0;
            }
        }
        else {
            radiusIndex--;
            if (radiusIndex < 0) {
                radiusIndex = radiuses.length - 1;
            }
        }
        config.get(Configuration.CATEGORY_GENERAL, "radiusIdx", 0).set(radiusIndex);
        config.save();
    }

    public static int getRadiusIndex() {
        return radiusIndex;
    }

    public static void setRadiusIndex(int index) {
        radiusIndex = index;
        config.get(Configuration.CATEGORY_GENERAL, "radiusIdx", 0).set(radiusIndex);
        config.save();
    }

    public static int getRadius() {
        return radiuses[radiusIndex];
    }

    public static void addBlock(String name, Ident ident, int color) {
        BlockInfo info = blocks.stream().filter(bi -> bi.getIdent().equals(ident)).findAny().orElse(null);
        if (info == null) {
            info = new BlockInfo(name, ident, color, true);
            blocks.add(info);
        }
        else {
            info.update(name, color);
        }
        update(info);
    }

    public static void update(BlockInfo info) {
        String categoryName = "blocks." + info.getIdent().getIdentPair();
        for (String category : config.getCategoryNames()) {
            if (category.startsWith("blocks.")) {
                config.get(categoryName, "name", "").set(info.name);
                config.get(categoryName, "color", -1).set(info.color);
                config.get(categoryName, "enabled", false).set(info.enabled);
                config.save();
                break;
            }
        }
    }

    public static void remove(BlockInfo info) {
        ConfigCategory cat = config.getCategory("blocks." + info.getIdent().getIdentPair());
        config.removeCategory(cat);
        config.save();
        blocks.remove(info);
    }
}
