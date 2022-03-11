package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.BusPay;
import me.lewin.dellunabus.npc.BusNPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BusSettingGUI implements Listener {
    public Inventory getInventory(String name){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6       ˚₊· Delluna Bus Set ₊·§1");

        inv.setItem(1, nameSet());
        inv.setItem(2, iconSet());
        inv.setItem(3, textSet());

        inv.setItem(5, pay(name));
        inv.setItem(7, remove());
        inv.setItem(8, iconName(name));

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack iconName(String name){
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, name);
    }

    private ItemStack nameSet(){
        return IconDefault.iconDefault(Material.NAME_TAG, "버스 이름 설정");
    }
    private ItemStack iconSet(){
        return IconDefault.iconDefault(Material.PLAYER_HEAD, "버스 아이콘 설정");
    }
    private ItemStack textSet(){
        return IconDefault.iconDefault(Material.OAK_SIGN, "버스 문구 설정");
    }
    private ItemStack remove(){
        List<String> lore = new ArrayList<>();
        lore.add("§7 쉬프트 + 좌클릭");
        return IconDefault.iconDefault(Material.BARRIER, "버스 삭제", lore);
    }
    private ItemStack pay(String name){
        FileConfiguration config = BusDataFile.getConfig(name);
        if (config.getBoolean("paid"))
            return IconDefault.iconDefault(Material.LIME_WOOL, "버스 연장하기");
        else
            return IconDefault.iconDefault(Material.RED_WOOL, "버스 연장하기");
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6       ˚₊· Delluna Bus Set ₊·§1")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            String name = event.getInventory().getItem(8).getItemMeta().getDisplayName();

            switch ((event.getCurrentItem()).getType()) {
                case NAME_TAG:
                    player.openInventory(new BusNameSetGUI().getInventory(name));
                    break;
                case OAK_SIGN:
                    player.openInventory(new BusTitleSetGUI().getInventory(name));
                    break;
                case PLAYER_HEAD:
                    player.openInventory(new BusIconSetGUI().getInventory(name));
                    break;
                case LIME_WOOL:
                    player.sendMessage("이미 연장하셨습니다.");
                    break;
                case RED_WOOL:
                    BusPay.pay(name, player);
                    player.openInventory(new BusSettingGUI().getInventory(name));
                    break;
                case BARRIER:
                    if (event.getClick() != ClickType.SHIFT_LEFT) return;
                    BusNPC.removeNPC(name, player);
                    BusDataFile.removeDataFile(name);
                    player.closeInventory();
                    return;
            }
            return;
        }
    }
}
