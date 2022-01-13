package com.fgtXray.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fgtXray.FgtXRay;
import com.fgtXray.Ident;
import com.fgtXray.config.ConfigHandler;
import com.fgtXray.reference.BlockInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OresSearch {
    public static final List<BlockInfo> searchList = new ArrayList<BlockInfo>(); // List of ores/blocks to search for.
    private static final Pattern TESTPTN = Pattern.compile("\\p{C}");

    // Used to check if a OreInfo already exists in the searchList
    private static boolean checkList(List<BlockInfo> temp, BlockInfo value, ItemStack stack) {
        for (BlockInfo info : temp) {
            if ((info.name.equals(value.name)) && info.getIdent().equals(Item.getIdFromItem(stack.getItem()), stack.getItemDamage())) {
                return true;
            }
        }
        return false;
    }

    // Takes a string of id:meta or oreName to add to our search list.
    public static void add(Ident ident, String name, int color) {
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

        BlockInfo existed = searchList.stream().filter(info -> info.getIdent().equals(id, meta)).findAny().orElse(null);
        if (existed != null) {
            String oldName = existed.name;
            existed.update(name, color);
            ConfigHandler.replace(oldName, existed);
        }
        else {
            Ident ident = new Ident(id, meta);
            searchList.add(new BlockInfo(name, ident, color, true));
            FgtXRay.postChat(String.format("successfully added %s.", oreIdent));
            ConfigHandler.add(name, oreIdent, color);
        }
    }

    public static void remove(String oreIdent) {

    }

    // Return the searchList, create it if needed.
    public static List<BlockInfo> fillDictionary() {
        if (searchList.isEmpty()) {
            searchList.clear();
            searchList.addAll(FgtXRay.blocks);
        }
        return searchList;
    }
}
