package me.Pro2021CA.dupePlusPlus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public class BlacklistGui {
    static void openBlacklistGui(Player p){
        PaginatedGui gui = Gui.paginated()
                .title(MiniMessage.miniMessage().deserialize("<green>DupePlusPlus GUI"))
                .rows(6)
                .pageSize(45)
                .create();
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> gui.next()));

        for (int i = 0; i < blacklisteditems.size(); i++) {
            gui.addItem(ItemBuilder.from(blacklisteditems.get(i)).asGuiItem(event -> {
                ItemStack item = new ItemStack(event.getCurrentItem().getType());
                item.setLore(event.getCurrentItem().getLore());
                blacklisteditems.remove(item);
                DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
                DupePlusPlus.plugin.saveConfig();
                event.setCancelled(true);
                openBlacklistGui(p);
            }));
        }
        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
            if (event.getSlot() == 51 || event.getSlot() == 47){
                return;
            }
            if (event.getCurrentItem() == null){
                return;
            }
            Material material = event.getCurrentItem().getType();
            ItemStack item = new ItemStack(material);
            item.setLore(event.getCurrentItem().getLore());
            if (blacklisteditems.contains(item)) {
                return;
            }

            blacklisteditems.add(item);
            DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
            DupePlusPlus.plugin.saveConfig();
            openBlacklistGui(p);
        });
        gui.open(p);
    }
}
