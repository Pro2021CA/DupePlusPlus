package me.Pro2021CA.dupePlusPlus;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static me.Pro2021CA.dupePlusPlus.blacklist.blacklisteditems;

public final class DupePlusPlus extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        if (getConfig().getList("blacklisted items") != null) {
            blacklisteditems = (List<ItemStack>) getConfig().getList("blacklisted items");
        } else {
            blacklisteditems = new ArrayList<ItemStack>();
            getConfig().set("blacklisted items", blacklisteditems);
            saveConfig();
            // Plugin startup logic
        }
        if (getConfig().getInt("maxdupe") == 0){
            getConfig().set("maxdupe", 1);
            maxdupe.MaxDupe = 1;
        }else{
            maxdupe.MaxDupe = getConfig().getInt("maxdupe");
        }
        getCommand("blacklist").setExecutor(new blacklist());
        getCommand("dupe").setExecutor(new dupe());
        getCommand("nondupeable").setExecutor(new persistendata());
        getCommand("maxdupe").setExecutor(new maxdupe());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        plugin.getConfig().set("blacklisted items", blacklisteditems);
        plugin.getConfig().set("maxdupe", maxdupe.MaxDupe);
        saveConfig();
    }
}
