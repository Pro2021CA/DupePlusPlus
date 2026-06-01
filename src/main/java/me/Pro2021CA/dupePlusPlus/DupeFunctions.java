package me.Pro2021CA.dupePlusPlus;

import jdk.jfr.Timespan;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public class DupeFunctions {

    public static HashMap<Player, Instant> cooldown;
    private static List<String> enchantlist;

    public static boolean isBlacklisted(ItemStack item){
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
        return blacklisteditems.contains(tool);
    }

    public static boolean hasPDC(ItemStack item){
        if(!DupePlusPlus.plugin.getConfig().getBoolean("checkdata")){
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"));
    }


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

    public static boolean isBundle(ItemStack itemStack){
        return itemStack.getItemMeta() instanceof BundleMeta;
    }

    public static boolean dupe(Player p, int i){
        for(int j = 0; j<i - 1; j++){
            if(!p.getInventory().addItem(p.getInventory().getItemInMainHand()).isEmpty()){
                return !DupePlusPlus.plugin.getConfig().getBoolean("inventoryfullmessage");
            }
        }
        if(!p.getInventory().addItem(p.getInventory().getItemInMainHand()).isEmpty()){
            return !DupePlusPlus.plugin.getConfig().getBoolean("inventoryfullmessage");
        }
        return true;
    }
    public static boolean canDupe(Player p, int dupeamount){
        if (dupeamount > MaxDupe.MaxDupe){
            for (int i = DupePlusPlus.plugin.getConfig().getInt("permissionmaxdupe") + 1; i > 0; i--){
                if (p.hasPermission("dupeplusplus.dupe." + i)){
                    return i > dupeamount;
                }
            }
            return false;
        }
        return true;
    }


    public static void setCooldown(Player p, Long seconds){
        if(cooldown == null){
            cooldown = new HashMap<>();
        }
        cooldown.put(p, Instant.now().plusSeconds(seconds));
        System.out.println(Instant.now().plusSeconds(seconds));
    }
    public static boolean isOnCooldown(Player p){
        if(cooldown == null){
            cooldown = new HashMap<>();
        }
        if(cooldown.get(p) == null){
            return false;
        }
        return cooldown.get(p).isAfter(Instant.now());
    }
    public static Long remainingCooldown(Player p){
        cooldown.get(p).minusSeconds(Instant.now().getEpochSecond());
        return cooldown.get(p).minusSeconds(Instant.now().getEpochSecond()).getEpochSecond();
    }


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
