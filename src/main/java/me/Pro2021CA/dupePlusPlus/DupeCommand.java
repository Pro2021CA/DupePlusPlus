package me.Pro2021CA.dupePlusPlus;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

import static me.Pro2021CA.dupePlusPlus.Blacklist.blacklisteditems;

public class DupeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p) {
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

            // get the item that will be checked for blacklist
            ItemStack tool = new ItemStack(p.getInventory().getItemInMainHand().getType());
            tool.setLore(p.getInventory().getItemInMainHand().getLore());

            // check if item is blacklisted
            if (blacklisteditems.contains(tool)){
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item"));
                return true;
            }else if(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))) {
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                return true;
            }

            // check if it's a shulker box
            if(p.getInventory().getItemInMainHand().getItemMeta() instanceof BlockStateMeta){
                BlockStateMeta im = (BlockStateMeta)p.getInventory().getItemInMainHand().getItemMeta();
                if(im.getBlockState() instanceof ShulkerBox){
                    ShulkerBox shulkerBox = (ShulkerBox) im.getBlockState();



                    // loop items in shulker
                    for (int i = 0; i < shulkerBox.getInventory().getSize(); i++) {
                        if (shulkerBox.getInventory().getItem(i) != null){
                            if (shulkerBox.getInventory().getItem(i).getType() != Material.AIR){


                                // check if item is blacklisted
                                ItemStack item = new ItemStack(shulkerBox.getInventory().getItem(i).getType());
                                item.setLore(shulkerBox.getInventory().getItem(i).getLore());
                                if (blacklisteditems.contains(item)) {
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "you can't dupe this item"));
                                    return true;

                                // check for bundle
                                }else if(item.getType().toString().contains("BUNDLE")){
                                    BundleMeta bundlemetastuff = (BundleMeta) shulkerBox.getInventory().getItem(i).getItemMeta();
                                    List<ItemStack> bundles = bundlemetastuff.getItems();


                                    // loop items in bundle
                                    for (int e = 0; e < bundles.size(); e++){
                                        if (bundles.get(e).getType() != Material.AIR) {
                                            if (bundles.get(e) != null) {

                                                // check if the item is blacklisted
                                                ItemStack Bundleitem = new ItemStack(bundles.get(e).getType());
                                                Bundleitem.setLore(bundles.get(e).getLore());
                                                if (blacklisteditems.contains(Bundleitem)){
                                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You cant dupe this item!"));
                                                    return true;

                                                // ban bundles inside of bundles
                                                }else if(Bundleitem.getType().toString().contains("BUNDLE")){
                                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                                                    return true;

                                                }else if(bundles.get(e).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))){
                                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }else if(shulkerBox.getInventory().getItem(i).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))){
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item!"));
                                    return true;
                                }
                            }
                        }
                    }
                }
            //  is held item bundle
            }else if(p.getInventory().getItemInMainHand().getType().toString().contains("BUNDLE")){

                // loop items in bundle
                BundleMeta bundleMeta = (BundleMeta) p.getInventory().getItemInMainHand().getItemMeta();
                List<ItemStack> bundle = bundleMeta.getItems();
                for (int i = 0; i < bundleMeta.getItems().size(); i++){
                    if (bundle.get(i).getType() != Material.AIR) {
                        if (bundle.get(i) != null) {

                            // check if it's blacklisted
                            ItemStack itemStack = new ItemStack(bundle.get(i).getType());
                            itemStack.setLore(bundle.get(i).getLore());
                            if (blacklisteditems.contains(itemStack)){
                                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You cant dupe this item!"));
                                return true;
                            }else if(itemStack.getType().toString().contains("BUNDLE")){
                                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can't dupe this item"));
                                return true;
                            }else if(bundle.get(i).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))) {
                                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix")) + "You can't dupe this item!");
                                return true;
                            }
                        }
                    }
                }
            }
            // check for maxdupe amount
            if (dupeamount > MaxDupe.MaxDupe){
                for (int i = 26 - MaxDupe.MaxDupe; i > 0; i--){
                    if (p.hasPermission("dupeplusplus.dupe." + i)){
                        if (i < dupeamount){
                            p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can only dupe " + i + " times"));
                            return true;
                        }
                        for (int e = 0; e < dupeamount; e++) {
                            p.give(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));
                        }
                        p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Duped " + dupeamount + " times"));
                        return true;
                    }
                }
                p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "You can only dupe " + MaxDupe.MaxDupe + " times"));
                return true;
            }
            for (int i = 0; i < dupeamount; i++) {
                p.getInventory().addItem(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));
            }
            p.sendMessage(MiniMessage.miniMessage().deserialize(DupePlusPlus.plugin.getConfig().getString("prefix") + "Duped " + dupeamount + " times"));
        }return true;
    }
}
