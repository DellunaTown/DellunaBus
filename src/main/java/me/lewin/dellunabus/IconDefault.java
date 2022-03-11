package me.lewin.dellunabus;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class IconDefault {
    //자주쓰는 아이콘 설정
    public static ItemStack iconNull(){
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, " ", 2);
    }
    public static ItemStack iconBack() { return IconDefault.iconDefault(Material.BARRIER, "§c뒤로가기", 1); }


    //아이콘 설정
    public static ItemStack iconDefault(Material material) {
        return new ItemStack(material);
    }

    //이름이 포함된 아이콘 설정
    public static ItemStack iconDefault(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);

        item.setItemMeta(meta);
        return item;
    }

    //이름과 로어가 포함된 아이콘 설정
    public static ItemStack iconDefault(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    //이름과 로어와 인첸트가 포함된 아이콘 설정
    public static ItemStack iconDefault(Material material, String displayName, List<String> lore, Boolean enchent) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        if (enchent) meta.addEnchant(Enchantment.DURABILITY, 1, true);

        item.setItemMeta(meta);
        return item;
    }


    //이름과 커스텀모델값이 포함된 아이콘 설정
    public static ItemStack iconDefault(Material material, String displayName, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
        return item;
    }

    //이름과 로어와 커스텀모델값이 포함된 아이콘 설정
    public static ItemStack iconDefault(Material material, String displayName, List<String> lore, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
        return item;
    }
}