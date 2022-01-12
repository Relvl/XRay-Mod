package com.fgtXray.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fgtXray.FgtXRay;
import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.OreInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OresSearch {
    public static final List<OreInfo> searchList = new ArrayList<OreInfo>(); // List of ores/blocks to search for.
    private static final Pattern TESTPTN = Pattern.compile("\\p{C}");

    // Used to check if a OreInfo already exists in the searchList
    private static boolean checkList(List<OreInfo> temp, OreInfo value, ItemStack stack) {
        for (OreInfo oreCheck : temp) {
            if ((oreCheck.oreName == value.oreName) && (oreCheck.id == Item.getIdFromItem(stack.getItem())) && (oreCheck.meta == stack.getItemDamage())) {
                return true; // This ore already exists in the temp list. (Sometimes the OreDict returns duplicate entries, like gold twice)
            }
        }
        return false;
    }

    // Takes a string of id:meta or oreName to add to our search list.
    public static void add(String oreIdent, String name, int color) {
        oreIdent = TESTPTN.matcher(oreIdent).replaceAll("?");
        int id;
        int meta;

        // Hopefully a proper id:meta string.
        if (oreIdent.contains(":")) {
            String[] splitArray = oreIdent.split(":");

            if (splitArray.length != 2) {
                FgtXRay.postChat(String.format("%s is not a valid identifier. Try id:meta (example 1:0 for stone) or oreName (example oreDiamond or mossyStone)", oreIdent));
                return;
            }

            try {
                id = Integer.parseInt(splitArray[0]);
                meta = Integer.parseInt(splitArray[1]);
            }
            catch (NumberFormatException e) {
                FgtXRay.postChat(String.format("%s contains data other than numbers and the colon. Failed to add.", oreIdent));
                return;
            }

        }
        else {
            try {
                id = Integer.parseInt(oreIdent);
                meta = 0;
            }
            catch (NumberFormatException e) {
                FgtXRay.postChat("Doesn't support in-game additions to the ore dictionary yet.. Failed to add.");
                return;
            }
        }

        OreInfo existed = searchList.stream().filter(x -> x.id == id && x.meta == meta).findAny().orElse(null);
        if (existed != null) {
            String oldName = existed.oreName;
            existed.update(name, color);
            ConfigHandler.replace(oldName, existed);
        }
        else {
            searchList.add(new OreInfo(name, id, meta, color, true));
            FgtXRay.postChat(String.format("successfully added %s.", oreIdent));
            ConfigHandler.add(name, oreIdent, color);
        }
    }

    public static void remove(String oreIdent) {

    }

    // Return the searchList, create it if needed.
    public static List<OreInfo> get() {
        if (searchList.isEmpty()) {
            List<OreInfo> temp = new ArrayList(); // Temporary array of OreInfos to replace searchList
            Map<String, OreInfo> tempOredict = new HashMap<String, OreInfo>(); // Temporary oredict map to replace oredictOres

            // OreDictionary.getOres("string") adds the ore if it doesn't exist.
            // Here we check our oredictOres with the untouched oredict and delete any that dont exist already. This avoids polluting the oredict.
            for (String oreName : OreDictionary.getOreNames()) {
                if (FgtXRay.oredictOres.containsKey(oreName)) {
                    tempOredict.put(oreName, FgtXRay.oredictOres.get(oreName));
                    //System.out.println( String.foramt( "[Fgt XRay]: Found ore %s in dictionary, adding.", oreName ) );
                }
            }
            // Debug loop to notify of invalid and removed oreDict names.
            for (Map.Entry<String, OreInfo> entry : FgtXRay.oredictOres.entrySet()) {
                String key = entry.getKey();
                if (!tempOredict.containsKey(key)) {
                    System.out.println(String.format("[Fgt XRay] Ore %s doesn't exist in dictionary! Deleting.", key));
                }
            }
            FgtXRay.oredictOres.clear();
            FgtXRay.oredictOres.putAll(tempOredict);
            tempOredict.clear();

            // Now we can iterate over the clean oredictOres and get all the different types of oreName
            for (Map.Entry<String, OreInfo> entry : FgtXRay.oredictOres.entrySet()) {
                String key = entry.getKey(); // oreName string
                OreInfo value = entry.getValue(); // OreInfo class

                ArrayList<ItemStack> oreDictOres = OreDictionary.getOres(key); // Get an itemstack array of all the oredict ores for 'key'
                if (oreDictOres.size() < 1) {
                    System.out.println(String.format("[Fgt XRay] Ore %s doesn't exist! Skipping. (We shouldn't have this issue here! Please tell me about this!)", key));
                    continue;
                }
                for (int i = 0; i < oreDictOres.size(); i++) {
                    ItemStack oreItem = oreDictOres.get(i);
                    if (checkList(temp, value, oreItem)) {
                        System.out.println("[Fgt XRay] Duplicate ore found in Ore Dictionary!!! (" + key + ")");
                        continue;
                    }
                    temp.add(new OreInfo(value.oreName, Item.getIdFromItem(oreItem.getItem()), oreItem.getItemDamage(), value.color, value.draw));
                    System.out.println(String.format("[Fgt XRay] Adding OreInfo( %s, %d, %d, 0x%x, %b ) ",
                                                     value.oreName,
                                                     Item.getIdFromItem(oreItem.getItem()),
                                                     oreItem.getItemDamage(),
                                                     value.color,
                                                     value.draw
                    ));
                }
            }
            System.out.println("[Fgt XRay] --- Done populating searchList! --- ");
            System.out.println("[Fgt XRay] --- Adding custom blocks --- ");

            for (OreInfo ore : FgtXRay.customOres) //TODO: Check if custom already exists
            {
                System.out.println(String.format("[Fgt XRay] Adding OreInfo( %s, %d, %d, 0x%x, %b ) ", ore.oreName, ore.id, ore.meta, ore.color, ore.draw));
                temp.add(ore);
            }
            System.out.println("[Fgt XRay] --- Done adding custom blocks --- ");

            searchList.clear();
            searchList.addAll(temp);

        }
        return searchList;
    }
}
