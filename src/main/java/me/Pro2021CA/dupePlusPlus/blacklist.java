package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class blacklist implements CommandExecutor {
    public static List<ItemStack> blacklisteditems;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p){
            if (strings.length == 0) {
                p.sendMessage("No arguments defined");
            }else if (strings[0].equals("list")){
                for (int i = 0; i < blacklisteditems.size(); i++){
                    p.sendMessage(blacklisteditems.get(i).getType().toString());
                }
            }else if (strings[0].equals("add")){
                if (p.getInventory().getItemInMainHand().getType() == Material.AIR || p.getInventory().getItemInMainHand() == null){
                    p.sendMessage("You cant blacklist air");
                    return true;
                }
                ItemStack itemStack = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
                itemStack.setDurability(itemStack.getType().getMaxDurability());
                Integer amount = itemStack.getAmount();
                Short old = itemStack.getDurability();
                ItemMeta oldmeta = itemStack.getItemMeta();
                ItemMeta meta = itemStack.getItemMeta();
                meta.setItemName("blacklisted item");
                itemStack.setItemMeta(meta);
                itemStack.setAmount(1);
                if (blacklisteditems.contains(itemStack)){
                    itemStack.setItemMeta(oldmeta);
                    itemStack.setDurability(old);
                    itemStack.setAmount(amount);
                    p.sendMessage("This item is already blacklisted");
                    return true;
                }
                blacklisteditems.add(itemStack);
                itemStack.setAmount(amount);
                p.sendMessage("Added your held item");
                itemStack.setItemMeta(oldmeta);
                itemStack.setDurability(old);
                DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
            }else{
                p.sendMessage("usage: /blacklist <list/add>");
            }
        }
        return true;
    }
}
