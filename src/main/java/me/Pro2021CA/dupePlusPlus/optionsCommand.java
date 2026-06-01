package me.Pro2021CA.dupePlusPlus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import it.unimi.dsi.fastutil.Hash;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class optionsCommand implements CommandExecutor {
    private static HashMap<String, Material> materialHashMap;
    public static HashMap<String, String> typehash;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(commandSender instanceof Player p){
            Gui gui = Gui.gui().title(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("guititle"))).rows(6).create();
            for(String string : DupePlusPlus.plugin.getConfig().getKeys(false)){
                if(materialHashMap.get(string) == null){
                    materialHashMap.put(string, Material.STICK);
                }
                gui.addItem(ItemBuilder.from(materialHashMap.get(string)).lore(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Value: " + DupePlusPlus.plugin.getConfig().get(string).toString())).name(MiniMessage.miniMessage().deserialize("<!italic><gray>" + string)).asGuiItem(event -> {
                    event.setCancelled(true);
                }));
            }
            gui.setItem(6, 5, ItemBuilder.from(Material.GREEN_CONCRETE).name(MiniMessage.miniMessage().deserialize("<!italic><green>Reload Config!")).lore(MiniMessage.miniMessage().deserialize("<!italic><dark_gray>Click to reload the config")).asGuiItem(e -> {
                e.setCancelled(true);
                DupePlusPlus.plugin.reloadConfig();
                gui.close(p);
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Reloaded config!"));
            }));
            gui.open(p);
        }

        return true;
    }

    public static void registerOption(String string, Material material, String type){
        if(materialHashMap == null){
            materialHashMap = new HashMap<>();
        }
        if(typehash == null){
            typehash = new HashMap<>();
        }
        materialHashMap.put(string, material);
        typehash.put(string, type);
    }
}
