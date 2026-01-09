package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class blacklist implements CommandExecutor {
    public static List<Material> blacklisteditems;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p){
            if (strings.length == 0) {
                p.sendMessage("No arguments defined");
                return true;
            }else if (strings[0].equals("list")){
                for (int i = 0; i < blacklisteditems.size(); i++){
                    p.sendMessage(blacklisteditems.get(i).toString());
                }
            }else if (strings[0].equals("add")){
                if (p.getInventory().getItemInMainHand().getType() == Material.AIR || p.getInventory().getItemInMainHand() == null){
                    p.sendMessage("You cant blacklist air");
                    return true;
                }
                Material material = p.getInventory().getItemInMainHand().getType();
                blacklisteditems.add(material);
                DupePlusPlus.plugin.getConfig().set("blacklisted items", blacklisteditems);
                DupePlusPlus.plugin.getConfig().createSection("how");
                DupePlusPlus.plugin.saveConfig();
            }else{
                p.sendMessage("usage: /blacklist <list/add>");
            }
        }
        return true;
    }
}
