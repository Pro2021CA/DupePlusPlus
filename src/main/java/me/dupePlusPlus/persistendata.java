package me.Pro2021CA.dupePlusPlus;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class persistendata implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p){
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            NamespacedKey key = new NamespacedKey(DupePlusPlus.plugin, "dupeable");
            itemStack.editPersistentDataContainer(data -> data.set(key, PersistentDataType.BOOLEAN, true));
        }
        return true;
    }
}
