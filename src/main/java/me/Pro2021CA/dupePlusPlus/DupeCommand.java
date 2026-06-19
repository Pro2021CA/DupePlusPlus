package me.Pro2021CA.dupePlusPlus;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import static me.Pro2021CA.dupePlusPlus.DupeFunctions.*;

public class DupeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p) {


            // check whether the player can dupe again
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
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe air, duh"));
                return true;
            }


            // check if item is blacklisted
            if(blacklisted(p.getInventory().getItemInMainHand())){
                if(DupePlusPlus.plugin.getConfig().getBoolean("blacklist")){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item"));
                    return true;
                }
            }else if(!DupePlusPlus.plugin.getConfig().getBoolean("blacklist")){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item"));
                return true;
            }

            // check for maxdupe amount
            if(canDupe(p, dupeamount)){

                // dupe and send inventory full message if needed
                if(dupe(p, dupeamount) == true){
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Duped " + dupeamount + " times"));

                }else{
                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Not enough inventory space!"));
                }


                // set the cooldown of the command
                setCooldown(p, Long.parseLong(DupePlusPlus.plugin.getConfig().get("cooldown").toString()));


            }else{
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can only dupe " + maxDupeAmount(p) + " times!"));
            }
        }return true;
    }
}
