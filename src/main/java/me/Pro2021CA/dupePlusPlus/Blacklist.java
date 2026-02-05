package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.Pro2021CA.dupePlusPlus.BlacklistGui.openBlacklistGui;

public class Blacklist implements CommandExecutor {
    public static List<ItemStack> blacklisteditems;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p){

            // check if argument is set
            if (strings.length == 0) {
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "No arguments defined"));
                return true;

            // get list of blacklisted items
            }else if (strings[0].equals("list")){
                for (int i = 0; i < blacklisteditems.size(); i++){
                    openBlacklistGui(p);
                }

            // blacklist an item
            }else if (strings[0].equals("add")){
                if (p.getInventory().getItemInMainHand().getType() == Material.AIR || p.getInventory().getItemInMainHand() == null){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "You cant blacklist air"));
                    return true;
                }
                Material material = p.getInventory().getItemInMainHand().getType();
                ItemStack item = new ItemStack(material);
                item.setLore(p.getInventory().getItemInMainHand().getLore());

                // check if it's already blacklisted
                if (blacklisteditems.contains(item)){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "this is already blacklisted"));
                    return true;
                }

                // blacklist the item
                blacklisteditems.add(item);
                DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
                DupePlusPlus.plugin.saveConfig();


            }else if(strings[0].equals("remove")){
                Material material = p.getInventory().getItemInMainHand().getType();
                ItemStack item = new ItemStack(material);
                item.setLore(p.getInventory().getItemInMainHand().getLore());

                // check if item is blacklisted
                if(blacklisteditems.contains(item)){
                    blacklisteditems.remove(item);
                    DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
                    DupePlusPlus.plugin.saveConfig();
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "Removed from blacklist"));
                    return true;
                }else{
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "This item is not blacklisted"));
                    return true;
                }
            }
            else{
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().get("prefix") + "usage: /blacklist <list/add/remove>"));
            }
        }
        return true;
    }
}
