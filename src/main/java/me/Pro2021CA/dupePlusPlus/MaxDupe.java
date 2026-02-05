package me.Pro2021CA.dupePlusPlus;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MaxDupe implements CommandExecutor {
    public static int MaxDupe;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        MaxDupe = Integer.parseInt(strings[0]);
        if (commandSender instanceof Player p){
            p.sendMessage(DupePlusPlus.plugin.getConfig().getString("prefix") + "Max dupe set");
            DupePlusPlus.plugin.getConfig().set("maxdupe", Integer.parseInt(strings[0]));
            DupePlusPlus.plugin.saveConfig();
        }
        return true;
    }
}
