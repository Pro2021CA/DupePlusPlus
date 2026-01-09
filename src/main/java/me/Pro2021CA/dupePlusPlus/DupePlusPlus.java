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
            blacklisteditems = (List<Material>) getConfig().getList("blacklisted items");
        } else {
            blacklisteditems = new ArrayList<Material>();
            // Plugin startup logic
        }
        getCommand("blacklist").setExecutor(new blacklist());
        getCommand("dupe").setExecutor(new dupe());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        plugin.getConfig().set("blacklisted items", blacklisteditems);
        saveConfig();
    }
}
