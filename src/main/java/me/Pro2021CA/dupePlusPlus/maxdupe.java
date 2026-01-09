package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class maxdupe implements CommandExecutor {
    public static int MaxDupe;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        MaxDupe = Integer.parseInt(strings[0]);
        if (commandSender instanceof Player p){
            p.sendMessage("Max dupe set");
        }
        return true;
    }
}
