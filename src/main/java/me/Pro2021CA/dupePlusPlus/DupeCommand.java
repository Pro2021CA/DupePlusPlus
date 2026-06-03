package me.Pro2021CA.dupePlusPlus;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.NotNull;


import java.util.List;

import static me.Pro2021CA.dupePlusPlus.DupeFunctions.*;

public class DupeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p) {

            if(isOnCooldown(p)){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You're on cooldown for " + remainingCooldown(p) + " seconds"));
                return true;
            }

            // check for arguments
            Integer dupeamount;
            if (strings.length == 0) {
                dupeamount = 1;
            } else {
                dupeamount = Integer.parseInt(strings[0]);
            }

            // make sure player is holding something
            if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe air, duh"));
                return true;
            }

            ItemStack itemStack = p.getInventory().getItemInMainHand();

            // check if item is blacklisted
            if (isBlacklisted(itemStack)){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item"));
                return true;
            }else if(hasPDC(itemStack)) {
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                return true;
            }
            if(hasEnchants(itemStack)){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                return true;
            }


            // check if it's a shulker box
            if(p.getInventory().getItemInMainHand().getItemMeta() instanceof BlockStateMeta im){
                if(im.getBlockState() instanceof ShulkerBox shulkerBox){
                    // loop items in shulker
                    for (int i = 0; i < shulkerBox.getInventory().getSize(); i++) {
                        if (shulkerBox.getInventory().getItem(i) != null){
                            if (shulkerBox.getInventory().getItem(i).getType() != Material.AIR){


                                // check if item is blacklisted
                                ItemStack item = shulkerBox.getInventory().getItem(i);
                                if (isBlacklisted(item)) {
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "you can't dupe this item"));
                                    return true;

                                // check for bundle
                                }else if(isBundle(item)){
                                    BundleMeta bundlemetastuff = (BundleMeta) shulkerBox.getInventory().getItem(i).getItemMeta();
                                    if(dupeBlacklist(bundlemetastuff)){
                                        p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "you can't dupe this item"));
                                        return true;
                                    }

                                }else if(hasPDC(item)){
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                                    return true;
                                }else if(hasEnchants(item)){
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                                    return true;
                                }
                            }
                        }
                    }
                }
            //  is held item bundle
            }else if(isBundle(itemStack)){

                // loop items in bundle
                BundleMeta bundleMeta = (BundleMeta) p.getInventory().getItemInMainHand().getItemMeta();
                if(dupeBlacklist(bundleMeta)){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "you can't dupe this item"));
                    return true;
                }
            }
            // check for maxdupe amount
            if(canDupe(p, dupeamount)){
                if(dupe(p, dupeamount) == true){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Duped " + dupeamount + " times"));

                }else{
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Not enough inventory space!"));
                }
                setCooldown(p, Long.parseLong(DupePlusPlus.plugin.getConfig().get("cooldown").toString()));
            }else{
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this many times!"));
            }
        }return true;
    }
}
