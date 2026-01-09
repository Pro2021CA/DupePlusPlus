package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.List;

import static me.Pro2021CA.dupePlusPlus.blacklist.blacklisteditems;

public class dupe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p) {
            Integer dupeamount;
            if (strings.length == 0) {
                dupeamount = 1;
            } else {
                dupeamount = Integer.parseInt(strings[0]);
            }
            ItemStack tool = p.getInventory().getItemInMainHand();
            Integer amount = tool.getAmount();
            tool.setAmount(1);
            ItemMeta oldmeta = tool.getItemMeta();
            ItemMeta meta = tool.getItemMeta();
            meta.setItemName("blacklisted item");
            tool.setItemMeta(meta);
            Short durability = tool.getDurability();
            tool.setDurability(tool.getType().getMaxDurability());
            blacklisteditems = (List<ItemStack>) DupePlusPlus.plugin.getConfig().getList("blacklisted items");
            if (blacklisteditems.contains(tool)){
                tool.setItemMeta(oldmeta);
                tool.setAmount(amount);
                tool.setDurability(durability);
                p.sendMessage("You can't dupe this item");
                return true;
            }
            tool.setItemMeta(oldmeta);
            tool.setDurability(durability);
            tool.setAmount(amount);
            for (int i = 0; i < dupeamount; i++) {
                p.give(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));
            }
            p.sendMessage("Duped " + dupeamount + " times");
        }return true;
    }
}
