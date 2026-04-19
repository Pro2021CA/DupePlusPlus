package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public class DupeFunctions {
    public static boolean isBlacklisted(ItemStack item){
        // get the item that will be checked for blacklist
        ItemStack tool = new ItemStack(item.getType());
        tool.setLore(item.getLore());
        if(blacklisteditems.contains(tool)){
            return true;
        }
        return false;
    }

    public static boolean hasPDC(ItemStack item){
        if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))) {
            return true;
        }
        return false;
    }


    public static boolean hasEnchants(ItemStack item){
        if(blacklistedenchants == null){
            blacklistedenchants = new ArrayList<>();
        }
        for (Enchantment enchantment : item.getEnchantments().keySet()){
            if(blacklistedenchants.contains(enchantment)){
                return true;
            }
        }
        if(item.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta){
            for (Enchantment enchantment : enchantmentStorageMeta.getStoredEnchants().keySet()){
                if(blacklistedenchants.contains(enchantment)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBundle(ItemStack itemStack){
        if(itemStack.getItemMeta() instanceof BundleMeta){
            return true;
        }
        return false;
    }
}
