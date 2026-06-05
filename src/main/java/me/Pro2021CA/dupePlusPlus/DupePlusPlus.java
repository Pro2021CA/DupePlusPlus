package me.Pro2021CA.dupePlusPlus;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;
import static me.Pro2021CA.dupePlusPlus.optionsCommand.registerOption;

public final class DupePlusPlus extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        DupeFunctions.setup(this);
        getCommand("blacklist").setExecutor(new Blacklist());
        getCommand("dupe").setExecutor(new DupeCommand());
        getCommand("nondupeable").setExecutor(new PersistenData());
        getCommand("maxdupe").setExecutor(new MaxDupe());
        getCommand("dupeconfig").setExecutor(new optionsCommand());
        MaxDupe.MaxDupe = getConfig().getInt("maxdupe");
        blacklisteditems = (List<ItemStack>) DupeFunctions.getConfig().getList("blacklisted items");
        blacklistedenchants = (List<List<String>>) DupeFunctions.getConfig().getList("blacklisted enchants");
        if(blacklisteditems == null){
            blacklisteditems = new ArrayList<>();
        }
        if(blacklistedenchants == null){
            blacklistedenchants = new ArrayList<>();
        }
        registerOption("prefix", Material.OAK_SIGN, "String");
        registerOption("checkenchantments", Material.ENCHANTED_BOOK, "Boolean");
        registerOption("checkdata", Material.COMMAND_BLOCK, "Boolean");
        registerOption("guititle", Material.ACACIA_SIGN, "String");
        registerOption("maxdupe", Material.BARRIER, "Integer");
        registerOption("permissionmaxdupe", Material.BEDROCK, "Integer");
        registerOption("inventoryfullmessage", Material.OAK_HANGING_SIGN, "Boolean");

        int pluginId = 31447;
        Metrics metrics = new Metrics(this, pluginId);
    }


    @Override
    public void onDisable() {
    }
}
