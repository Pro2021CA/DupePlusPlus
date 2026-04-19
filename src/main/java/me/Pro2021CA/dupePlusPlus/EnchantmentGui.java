package me.Pro2021CA.dupePlusPlus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Set;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklistedenchants;
import static me.Pro2021CA.dupePlusPlus.BlacklistGui.openBlacklistGui;

public class EnchantmentGui{
    public static void openEnchantmentGui(Player p){
        PaginatedGui gui = Gui.paginated()
                .title(MiniMessage.miniMessage().deserialize("<green>DupePlusPlus GUI"))
                .rows(6)
                .pageSize(45)
                .create();
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));
        gui.setItem(6, 1, ItemBuilder.from(Material.DIAMOND).setName("Normal GUI").lore(MiniMessage.miniMessage().deserialize("<gray>Click to open")).asGuiItem(event -> {
            openBlacklistGui(p);
        }));
        for (int i = 0; i < blacklistedenchants.size(); i++) {
            gui.addItem(ItemBuilder.from(Material.ENCHANTED_BOOK).enchant(blacklistedenchants.get(i)).asGuiItem(event -> {
                Enchantment enchantment = blacklistedenchants.get(event.getSlot());
                blacklistedenchants.remove(enchantment);
                DupePlusPlus.plugin.getConfig().set("blacklisted enchants", blacklistedenchants);
                DupePlusPlus.plugin.saveConfig();
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
            Set<Enchantment> enchantments = event.getCurrentItem().getEnchantments().keySet();
            for(Enchantment enchantment : enchantments){
                if(!blacklistedenchants.contains(enchantment)){
                    blacklistedenchants.add(enchantment);
                }
            }
            if(event.getCurrentItem().getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta){
                for(Enchantment enchantment : enchantmentStorageMeta.getStoredEnchants().keySet()){
                    if(!blacklistedenchants.contains(enchantment)){
                        blacklistedenchants.add(enchantment);
                    }
                }
            }
            DupePlusPlus.plugin.getConfig().set("blacklisted enchants", blacklistedenchants);
            DupePlusPlus.plugin.saveConfig();
            openEnchantmentGui(p);
        });
        gui.open(p);
    }
}
