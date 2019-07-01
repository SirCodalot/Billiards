package me.codalot.billiards.setup;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@SuppressWarnings("all")
public enum KeyItem {

    POOL_BALL_SOLID(getItem(Material.LEATHER_BOOTS, ChatColor.GRAY + "Pool Ball", 10000001)),
    POOL_BALL_STRIPED(getItem(Material.LEATHER_BOOTS, ChatColor.GRAY + "Pool Ball", 10000002)),

    CUE(getItem(Material.BOW, ChatColor.GOLD + "Cue", 10000001));

    private ItemStack item;

    KeyItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getItem(Color color) {
        ItemStack item = getItem();
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setColor(color);

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getItem(Material material, String name, int model) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();


        meta.setDisplayName(name);
        meta.setCustomModelData(model);
        meta.addItemFlags(ItemFlag.values());

        item.setItemMeta(meta);
        return item;
    }
}
