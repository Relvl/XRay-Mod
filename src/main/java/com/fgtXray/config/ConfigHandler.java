package com.fgtXray.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fgtXray.FgtXRay;
import com.fgtXray.Ident;
import com.fgtXray.reference.BlockInfo;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    private static final Pattern FORMAT_NAME_PTN = Pattern.compile("\\s+", Pattern.LITERAL);

    private static final int[] radiuses = {8, 16, 32, 48, 64, 80, 128, 256};
    private static int radiusIndex;

    private static Configuration config;

    public static void setup(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        radiusIndex = config.get(Configuration.CATEGORY_GENERAL, "radius", 0).getInt();

        for (String category : config.getCategoryNames()) {
            ConfigCategory cat = config.getCategory(category);
            if (category.startsWith("blocks.")) {
                FgtXRay.blocks.add(new BlockInfo( //
                    cat.get("name").getString(), //
                    new Ident(cat), //
                    cat.get("color").getInt(), //
                    cat.get("enabled").getBoolean(false) //
                ));
            }
            else if (!category.equals(Configuration.CATEGORY_GENERAL)) {
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
        config.get(Configuration.CATEGORY_GENERAL, "radius", 0).set(radiusIndex);
        config.save();
    }

    public static int getDistance() {
        return radiuses[radiusIndex];
    }

    public static String getDistanceTitle() {
        return String.format("D: %d", getDistance());
    }

    public static void add(String oreName, String ore, int color) {
        config.load();
        String formattedname = FORMAT_NAME_PTN.matcher(oreName).replaceAll(Matcher.quoteReplacement("")).toLowerCase();

        for (String category : config.getCategoryNames()) {
            if (category.startsWith("blocks.")) {
                if (config.get("blocks." + formattedname, "name", "").getString() == formattedname) {
                    FgtXRay.postChat(String.format("%s already exists. Please enter a different name. ", oreName));
                    return;
                }
            }
        }

        int oreId = Integer.parseInt(ore.split(":")[0]);
        // Don't do this if it does not exist... Stupid me
        int oreMeta = ore.contains(":") ? Integer.parseInt(ore.split(":")[1]) : 0;

        for (String category : config.getCategoryNames()) {
            if (category.startsWith("blocks.")) {
                config.get("blocks." + formattedname, "color", "").set(color);
                config.get("blocks." + formattedname, "enabled", "false").set(true);
                config.get("blocks." + formattedname, "id", "").set(oreId);
                config.get("blocks." + formattedname, "meta", "").set(oreMeta);
                config.get("blocks." + formattedname, "name", "").set(oreName);

            }
        }
        config.save();
    }

    public static void replace(String oldName, BlockInfo ore) {

    }

    // For updating single options
    public static void update(String key, boolean draw) {
        for (String category : config.getCategoryNames()) {
            String formattedname = FORMAT_NAME_PTN.matcher(key).replaceAll(Matcher.quoteReplacement("")).toLowerCase();
            String[] splitCat = category.split("\\.");

            if (splitCat.length == 2) {
                if ("blocks".equals(splitCat[0]) && splitCat[1].equals(formattedname)) {
                    config.get("blocks." + formattedname, "enabled", false).set(draw);
                }
            }
        }
        config.save();
    }

    public static void remove(String name) {

    }
}
