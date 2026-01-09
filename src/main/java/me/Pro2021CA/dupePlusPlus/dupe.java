package me.Pro2021CA.dupePlusPlus;

import io.papermc.paper.datacomponent.item.BundleContents;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.List;

import static me.Pro2021CA.dupePlusPlus.blacklist.blacklisteditems;

public class dupe implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player p) {
            Integer dupeamount;
            if (strings.length == 0) {
                dupeamount = 1;
            } else {
                dupeamount = Integer.parseInt(strings[0]);
            }
            ItemStack tool = new ItemStack(p.getInventory().getItemInMainHand().getType());
            tool.setLore(p.getInventory().getItemInMainHand().getLore());
            blacklisteditems = (List<ItemStack>) DupePlusPlus.plugin.getConfig().getList("blacklisted items");
            if (blacklisteditems.contains(tool)){
                p.sendMessage("You can't dupe this item");
                return true;
            }else if(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))) {
                p.sendMessage("You can't dupe this item!");
                return true;
            }
            if(p.getInventory().getItemInMainHand().getItemMeta() instanceof BlockStateMeta){
                BlockStateMeta im = (BlockStateMeta)p.getInventory().getItemInMainHand().getItemMeta();
                if(im.getBlockState() instanceof ShulkerBox){
                    ShulkerBox shulkerBox = (ShulkerBox) im.getBlockState();
                    for (int i = 0; i < shulkerBox.getInventory().getSize(); i++) {
                        if (shulkerBox.getInventory().getItem(i) != null){
                            if (shulkerBox.getInventory().getItem(i).getType() != Material.AIR){
                                ItemStack item = new ItemStack(shulkerBox.getInventory().getItem(i).getType());
                                item.setLore(shulkerBox.getInventory().getItem(i).getLore());
                                if (blacklisteditems.contains(item)) {
                                    p.sendMessage("you can't dupe this item");
                                    return true;
                                }else if(item.getType().toString().contains("BUNDLE")){
                                    BundleMeta bundlemetastuff = (BundleMeta) shulkerBox.getInventory().getItem(i).getItemMeta();
                                    List<ItemStack> bundles = bundlemetastuff.getItems();
                                    for (int e = 0; e < bundles.size(); e++){
                                        if (bundles.get(e).getType() != Material.AIR) {
                                            if (bundles.get(e) != null) {
                                                ItemStack Bundleitem = new ItemStack(bundles.get(e).getType());
                                                Bundleitem.setLore(bundles.get(e).getLore());
                                                if (blacklisteditems.contains(Bundleitem)){
                                                    p.sendMessage("You cant dupe this item!");
                                                    return true;
                                                }else if(Bundleitem.getType().toString().contains("BUNDLE")){
                                                    p.sendMessage("You can't dupe this item!");
                                                    return true;
                                                }else if(bundles.get(e).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))){
                                                    p.sendMessage("You can't dupe this item!");
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }else if(shulkerBox.getInventory().getItem(i).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))){
                                    p.sendMessage("You can't dupe this item!");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }else if(p.getInventory().getItemInMainHand().getType().toString().contains("BUNDLE")){
                BundleMeta bundleMeta = (BundleMeta) p.getInventory().getItemInMainHand().getItemMeta();
                List<ItemStack> bundle = bundleMeta.getItems();
                for (int i = 0; i < bundleMeta.getItems().size(); i++){
                    if (bundle.get(i).getType() != Material.AIR) {
                        if (bundle.get(i) != null) {
                            ItemStack itemStack = new ItemStack(bundle.get(i).getType());
                            itemStack.setLore(bundle.get(i).getLore());
                            if (blacklisteditems.contains(itemStack)){
                                p.sendMessage("You cant dupe this item!");
                                return true;
                            }else if(itemStack.getType().toString().contains("BUNDLE")){
                                p.sendMessage("You can't dupe this item");
                                return true;
                            }else if(bundle.get(i).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(DupePlusPlus.plugin, "dupeable"))) {
                                p.sendMessage("You can't dupe this item!");
                                return true;
                            }
                        }
                    }
                }
            }
            if (dupeamount > maxdupe.MaxDupe){
                p.sendMessage("You can only dupe " + maxdupe.MaxDupe + " times");
                return true;
            }
            for (int i = 0; i < dupeamount; i++) {
                p.give(p.getInventory().getItem(p.getInventory().getHeldItemSlot()));
            }
            p.sendMessage("Duped " + dupeamount + " times");
        }return true;
    }
}
