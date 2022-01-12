package com.fgtXray.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fgtXray.FgtXRay;
import com.fgtXray.reference.OreInfo;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    private static final Pattern FORMAT_NAME_PTN = Pattern.compile("\\s+", Pattern.LITERAL);
    private static Configuration config; // Save the config file handle for use later.
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void setup(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        FgtXRay.distIndex = config.get(Configuration.CATEGORY_GENERAL, "searchdist", 0).getInt(); // Get our search distance.

        for (String category : config.getCategoryNames()) // Iterate through each category in our config file.
        {
            ConfigCategory cat = config.getCategory(category);

            if (category.startsWith("oredict.")) // Dont iterate over the base category and make sure were on the oredict category.
            {
                String dictName = cat.get("dictname").getString();
                String guiName = cat.get("guiname").getString();
                int id = cat.get("id").getInt();
                int meta = cat.get("meta").getInt();
                int color = cat.get("color").getInt();
                boolean enabled = cat.get("enabled").getBoolean(false);

                FgtXRay.oredictOres.put(dictName, new OreInfo(guiName, id, meta, color, enabled));

            }
            else if (category.startsWith("customores.")) {
                String name = cat.get("name").getString();
                int id = cat.get("id").getInt();
                int meta = cat.get("meta").getInt();
                int color = cat.get("color").getInt();
                boolean enabled = cat.get("enabled").getBoolean(false);

                FgtXRay.customOres.add(new OreInfo(name, id, meta, color, enabled));
            }
        }
        config.save();
    }

    public static void add(String oreName, String ore, int color) {
        config.load();
        String formattedname = FORMAT_NAME_PTN.matcher(oreName).replaceAll(Matcher.quoteReplacement("")).toLowerCase();

        // check if entry exists
        for (String category : config.getCategoryNames()) {
            if (category.startsWith("customores.")) {
                if (config.get("customores." + formattedname, "name", "").getString() == formattedname) {
                    FgtXRay.postChat(String.format("%s already exists. Please enter a different name. ", oreName));
                    return;
                }
            }
        }

        int oreId = Integer.parseInt(ore.split(":")[0]);
        // Don't do this if it does not exist... Stupid me
        int oreMeta = ore.contains(":") ? Integer.parseInt(ore.split(":")[1]) : 0;

        for (String category : config.getCategoryNames()) {
            if (category.startsWith("customores.")) {
                config.get("customores." + formattedname, "color", "").set(color);
                config.get("customores." + formattedname, "enabled", "false").set(true);
                config.get("customores." + formattedname, "id", "").set(oreId);
                config.get("customores." + formattedname, "meta", "").set(oreMeta);
                config.get("customores." + formattedname, "name", "").set(oreName);

            }
        }
        config.save();
    }

    public static void replace(String oldName, OreInfo ore){

    }

    // For updating single options
    public static void update(String key, boolean draw) {
        // Save the new render distance.
        if ("searchdist".equals(key)) {
            config.get(Configuration.CATEGORY_GENERAL, "searchdist", 0).set(FgtXRay.distIndex);
            config.save();
            return;
        }

        // Figure out if this is a custom or dictionary ore.
        for (String category : config.getCategoryNames()) {
            String formattedname = FORMAT_NAME_PTN.matcher(key).replaceAll(Matcher.quoteReplacement("")).toLowerCase();
            String[] splitCat = category.split("\\.");

            if (splitCat.length == 2) {
                // Check if the current iteration is the correct category (oredict.emerald)
                if ("oredict".equals(splitCat[0]) && splitCat[1].equals(formattedname)) {
                    config.get("oredict." + formattedname, "enabled", false).set(draw);
                }
                else if ("customores".equals(splitCat[0]) && splitCat[1].equals(formattedname)) {
                    config.get("customores." + formattedname, "enabled", false).set(draw);
                }
            }
        }
        config.save();
    }

    public static void remove(String name) {

    }
}
