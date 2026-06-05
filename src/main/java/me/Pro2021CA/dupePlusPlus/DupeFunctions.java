package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public class DupeFunctions {

    public static HashMap<Player, Instant> cooldown;


    // function to check all other smaller blacklist functions at once
    // check entire blacklist, works with bundles and with shulkers
    public static boolean blacklisted(ItemStack item){
        if(isInsideBlacklist(item)){
            return true;
        }
        if(hasPDC(item)){
            return true;
        }
        if(hasEnchants(item)){
            return true;
        }
        if(isBundle(item)){
            if(bundleBlacklist((BundleMeta) item.getItemMeta())){
                return true;
            }
        }
        if(isShulker(item)){
            // check if it contains blacklisted items
            if(shulkerBlacklist(item)){
                return true;
            }
        }
        return false;
    }

    // individual blacklist checks

    // check the default blacklist
    public static boolean isInsideBlacklist(ItemStack item){
        // get the item that will be checked for blacklist
        Material material = null;
        if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey("minehutcosmetics", "material"))){
            material = Material.valueOf(item.getPersistentDataContainer().get(new NamespacedKey("minehutcosmetics", "material"), PersistentDataType.STRING));
        }
        if(material == null){
            material = item.getType();
        }
        ItemStack tool = new ItemStack(material);
        tool.setLore(item.getLore());

        // check if it is inside the blacklist
        return blacklisteditems.contains(tool);
    }

    // check the pdc
    public static boolean hasPDC(ItemStack item){
        if(!DupePlusPlus.plugin.getConfig().getBoolean("checkdata")){
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"));
    }


    // check the enchants
    public static boolean hasEnchants(ItemStack item){
        if(!DupePlusPlus.plugin.getConfig().getBoolean("checkenchantments")){
            return false;
        }
        if(blacklistedenchants == null){
            blacklistedenchants = new ArrayList<>();
        }
        for(int i = 0; i< blacklistedenchants.size(); i++){
            Boolean aBoolean = true;
            for(String str : blacklistedenchants.get(i)){
                if(!item.getEnchantments().keySet().contains(Enchantment.getByName(str))){
                    aBoolean = false;
                    break;
                }
            }
            if(aBoolean){
                return true;
            }
        }

        // check for enchanted books
        if(item.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta){
            for(int i = 0; i< blacklistedenchants.size(); i++){
                Boolean aBoolean = true;
                for(String str : blacklistedenchants.get(i)){
                    if(!enchantmentStorageMeta.getStoredEnchants().keySet().contains(Enchantment.getByName(str))){
                        aBoolean = false;
                        break;
                    }
                }
                if(aBoolean){
                    return true;
                }
            }
        }
        return false;
    }

    // check all items inside a bundle for blacklisted items
    public static boolean bundleBlacklist(BundleMeta meta){
        for(ItemStack itemStack : meta.getItems()){
            if(blacklisted(itemStack)){
                return true;
            }
        }
        return false;
    }


    // check all items inside a shulker for blacklisted
    public static boolean shulkerBlacklist(ItemStack itemStack){
        BlockStateMeta blockmeta = (BlockStateMeta) itemStack.getItemMeta();
        ShulkerBox shulkerBox = (ShulkerBox) blockmeta.getBlockState();
        for(ItemStack item : shulkerBox.getInventory().getContents()){
            if(blacklisted(item)){
                return true;
            }
        }
        return false;
    }

    // is the item a bundle
    public static boolean isBundle(ItemStack itemStack){
        return itemStack.getItemMeta() instanceof BundleMeta;
    }

    // is the item a shulker
    public static boolean isShulker(ItemStack itemStack){
        if(itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta){
            return blockStateMeta.getBlockState() instanceof ShulkerBox;
        }
        return false;
    }


    // dupe the item and return whether the inventory is full
    public static boolean dupe(Player p, int i){
        for(int j = 0; j<i - 1; j++){
            ItemStack item = p.getInventory().getItemInMainHand();
            if(!p.getInventory().addItem(item).isEmpty()){
                return !DupePlusPlus.plugin.getConfig().getBoolean("inventoryfullmessage");
            }
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if(!p.getInventory().addItem(item).isEmpty()){
            return !DupePlusPlus.plugin.getConfig().getBoolean("inventoryfullmessage");
        }
        return true;
    }

    // check the permissions and maxdupe amount to make sure player can dupe this amount of times
    public static boolean canDupe(Player p, int dupeamount){
        if (dupeamount > MaxDupe.MaxDupe){
            for (int i = DupePlusPlus.plugin.getConfig().getInt("permissionmaxdupe"); i > 0; i--){
                if (p.hasPermission("dupeplusplus.dupe." + i)){
                    return i >= dupeamount;
                }
            }
            return false;
        }
        return true;
    }

    public static int maxDupeAmount(Player p){
        for (int i = DupePlusPlus.plugin.getConfig().getInt("permissionmaxdupe"); i > 0; i--){
            if (p.hasPermission("dupeplusplus.dupe." + i)){
                if(i > MaxDupe.MaxDupe){
                    return i;
                }else{
                    return MaxDupe.MaxDupe;
                }
            }
        }
        return MaxDupe.MaxDupe;
    }


    // set the cooldown of the player
    public static void setCooldown(Player p, Long seconds){
        if(cooldown == null){
            cooldown = new HashMap<>();
        }
        cooldown.put(p, Instant.now().plusSeconds(seconds));
    }

    // check if player is on cooldown
    public static boolean isOnCooldown(Player p){
        if(cooldown == null){
            cooldown = new HashMap<>();
        }
        if(cooldown.get(p) == null){
            return false;
        }
        return cooldown.get(p).isAfter(Instant.now());
    }


    // get the remaining cooldown for the player in seconds
    public static Long remainingCooldown(Player p){
        cooldown.get(p).minusSeconds(Instant.now().getEpochSecond());
        return cooldown.get(p).minusSeconds(Instant.now().getEpochSecond()).getEpochSecond();
    }


    // create the blacklisted.yml file

    private static File file;
    private static YamlConfiguration config;

    public static void setup(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "blacklisted.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public static YamlConfiguration getConfig(){
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
