package me.Pro2021CA.dupePlusPlus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.BlacklistGui.openBlacklistGui;

public class EnchantmentGui{

    private static Map<Enchantment, Integer> enchantmentIntegerMap;
    private static List<String> enchantments1;

    public static void openEnchantmentGui(Player p){
        PaginatedGui gui = Gui.paginated()
                .title(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("guititle")))
                .rows(6)
                .pageSize(45)
                .create();
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));
        gui.setItem(6, 1, ItemBuilder.from(Material.DIAMOND).setName("Normal GUI").lore(MiniMessage.miniMessage().deserialize("<gray>Click to open")).asGuiItem(event -> {
            openBlacklistGui(p);
        }));
        for (int i = 0; i < blacklistedenchants.size(); i++) {
            enchantmentIntegerMap = new HashMap<>();
            for(String str : blacklistedenchants.get(i)){
                enchantmentIntegerMap.put(Enchantment.getByName(str), 1);
            }
            gui.addItem(ItemBuilder.from(Material.ENCHANTED_BOOK).enchant(enchantmentIntegerMap).asGuiItem(event -> {
                if(!p.hasPermission("dupeplusplus.edit")){
                    return;
                }
                List<String> enchantment = blacklistedenchants.get(event.getSlot());
                blacklistedenchants.remove(enchantment);
                DupeFunctions.getConfig().set("blacklisted enchants", blacklistedenchants);
                DupeFunctions.save();
                event.setCancelled(true);
                openEnchantmentGui(p);
            }));
        }
        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
            if (event.getSlot() == 51 || event.getSlot() == 47 || event.getSlot() == 45){
                return;
            }
            if (event.getCurrentItem() == null){
                return;
            }
            if(!event.getWhoClicked().hasPermission("dupeplusplus.edit")){
                return;
            }
            Set<Enchantment> enchantments = event.getCurrentItem().getEnchantments().keySet();
            enchantments1 = new ArrayList<>();
            for(Enchantment enchantment : enchantments){
                if(!enchantments1.contains(enchantment.getName())){
                    enchantments1.add(enchantment.getName());
                }
            }
            if(event.getCurrentItem().getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta){
                for(Enchantment enchantment : enchantmentStorageMeta.getStoredEnchants().keySet()){
                    if(!enchantments1.contains(enchantment.getName())){
                        enchantments1.add(enchantment.getName());
                    }
                }
            }
            if(blacklistedenchants.contains(enchantments1)){
                return;
            }
            blacklistedenchants.add(enchantments1);
            DupeFunctions.getConfig().set("blacklisted enchants", blacklistedenchants);
            DupeFunctions.save();
            openEnchantmentGui(p);
        });
        gui.open(p);
    }
}
