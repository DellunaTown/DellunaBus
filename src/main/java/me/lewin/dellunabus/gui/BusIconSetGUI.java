package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.IconDefault;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BusIconSetGUI implements Listener {
    public Inventory getInventory(String name){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§c");
        inv.setItem(0, nameIcon(name));
        inv.setItem(8, IconDefault.iconBack());

        for (int i = 0; i < inv.getSize(); i++) {
            if (i == 4) continue;
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }
    private ItemStack nameIcon(String name){
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, name);
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§c")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            String name = event.getInventory().getItem(0).getItemMeta().getDisplayName();

            switch ((event.getCurrentItem()).getType()) {
                case BARRIER:
                    event.setCancelled(true);
                    ItemStack item = event.getClickedInventory().getItem(4);
                    if (item != null){
                        event.getClickedInventory().clear(4);
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                            return;
                        }
                        player.getInventory().addItem(item);
                    }
                    player.openInventory(new BusSettingGUI().getInventory(name));
                    return;
                case WHITE_STAINED_GLASS_PANE:
                    if (event.getCurrentItem().getItemMeta().hasCustomModelData() && event.getCurrentItem().getItemMeta().getCustomModelData() == 2){
                        event.setCancelled(true);
                        return;
                    }
            }
            return;
        }
    }

    @EventHandler
    private void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§c")) {
            Player player = (Player) event.getPlayer();
            String npcName = event.getInventory().getItem(0).getItemMeta().getDisplayName();
            ItemStack item = event.getInventory().getItem(4);
            if (item == null) return;

            FileConfiguration busConfig = BusDataFile.getConfig(npcName);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(npcName);
            List<String> list = new ArrayList<>();
            meta.setLore(list);
            item.setItemMeta(meta);
            item.setAmount(1);
            busConfig.set("icon", item);
            BusDataFile.saveDataFile(busConfig, BusDataFile.getDataFile(npcName));

            player.sendMessage("변경되었습니다.");

            return;
        }
    }
}
