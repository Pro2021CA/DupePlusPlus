package me.Pro2021CA.dupePlusPlus;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public final class DupePlusPlus extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("blacklist").setExecutor(new Blacklist());
        getCommand("dupe").setExecutor(new DupeCommand());
        getCommand("nondupeable").setExecutor(new PersistenData());
        getCommand("maxdupe").setExecutor(new MaxDupe());
        MaxDupe.MaxDupe = getConfig().getInt("maxdupe");
        blacklisteditems = (List<ItemStack>) DupePlusPlus.plugin.getConfig().getList("blacklisted items");
    }
    @Override
    public void onDisable() {
    }
}
