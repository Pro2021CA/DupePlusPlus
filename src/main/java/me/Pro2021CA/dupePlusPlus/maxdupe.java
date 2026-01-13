package me.Pro2021CA.dupePlusPlus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class maxdupe implements CommandExecutor {
    public static int MaxDupe;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 0){
            PluginLogger pluginLogger = new PluginLogger(DupePlusPlus.plugin);
            pluginLogger.log(Level.WARNING, "NO ARGUMENTS FOUND! PLEASE ADD AN ARGUMENT");
            return true;
        }
        if (commandSender instanceof Player p){
            p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Max dupe set"));
        }
        DupePlusPlus.plugin.getConfig().set("maxdupe", Integer.parseInt(strings[0]));
        DupePlusPlus.plugin.saveConfig();
        MaxDupe = Integer.parseInt(strings[0]);
        return true;
    }
}
