package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.Teleport;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BusStationGUI implements Listener {
    public Inventory getInventory(String stationName, Integer current, Integer line){
        Inventory inv = Bukkit.getServer().createInventory(null, 54,  nameSet(line));

        FileConfiguration stationConfig = BusStationDataFile.getConfig(stationName);

        List<String> list = stationConfig.getStringList("list");

        int count = list.size();
        int index = 10;
        for (int i = 28 * (current - 1); i < 28 * current; i++){
            if (i >= count) break;

            FileConfiguration busConfig = BusDataFile.getConfig(list.get(i));

            ItemStack item = busConfig.getItemStack("icon");

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(list.get(i));
            item.setItemMeta(meta);

            inv.setItem(index, item);

            if (index % 9 == 7) index += 3;
            else index++;
        }

        inv.setItem(4, station(stationName, line, current));

        inv.setItem(46, previous_null());
        inv.setItem(52, next_null());

        if (current != 1)
            inv.setItem(46, previous());

        if (count > 28*current)
            inv.setItem(52, next());
        return inv;
    }

    private String nameSet(Integer line){
        switch (line){
            case 11:
            case 12:
                return "§f\uF808ꢘ";
            case 21:
            case 22:
                return "§f\uF808ꢗ";
            case 31:
            case 32:
                return "§f\uF808ꢙ";
            case 41:
            case 42:
                return "§f\uF808\uD803\uDE80";
        }
        return "null";
    }

    private ItemStack station(String stationName, Integer line, Integer current){
        List<String> lore = new ArrayList<>();


        String name = stationName;
        String page = "page " + current.toString();

        lore.add(page);
        switch (line){
            case 11:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1024);
            case 12:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1025);
            case 21:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1026);
            case 22:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1027);
            case 31:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1028);
            case 32:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1029);
            case 41:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1030);
            case 42:
                return IconDefault.iconDefault(Material.BONE, name, lore, 1031);
        }
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, " ");
    }
    private ItemStack next_null(){
        return IconDefault.iconDefault(Material.BONE, "§7[ §x§9§3§8§d§9§4§l다음 §7]", 1033);
    }
    private ItemStack previous_null(){
        return IconDefault.iconDefault(Material.BONE, "§7[ §x§9§3§8§d§9§4§l이전 §7]", 1035);
    }
    private ItemStack next(){
        return IconDefault.iconDefault(Material.BONE, "§7[ §6§l다음 §7]", 1032);
    }
    private ItemStack previous(){
        return IconDefault.iconDefault(Material.BONE, "§7[ §6§l이전 §7]", 1034);
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Boolean isBus = false;

        if (event.getView().getTitle().contains("§f\uF808ꢘ")) isBus = true;
        if (event.getView().getTitle().contains("§f\uF808ꢗ")) isBus = true;
        if (event.getView().getTitle().contains("§f\uF808ꢙ")) isBus = true;
        if (event.getView().getTitle().contains("§f\uF808\uD803\uDE80")) isBus = true;

        if (isBus) {
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getItemMeta().hasCustomModelData()) {
                Boolean icon = false;
                switch (event.getCurrentItem().getItemMeta().getCustomModelData()){
                    case 1032:
                    case 1033:
                    case 1034:
                    case 1035:
                    case 1024:
                    case 1025:
                    case 1026:
                    case 1027:
                    case 1028:
                    case 1029:
                    case 1030:
                    case 1031:
                        icon = true;
                }
                if (icon){
                    switch ((event.getCurrentItem()).getType()) {
                        case BONE:
                            if (event.getCurrentItem().getItemMeta().getCustomModelData() == 1032){
                                String str = event.getClickedInventory().getItem(4).getItemMeta().getLore().get(0);
                                int current = Integer.parseInt(str.replaceAll("[^\\d]", ""));
                                player.openInventory(new BusStationGUI().getInventory(event.getClickedInventory().getItem(4).getItemMeta().getDisplayName(), current + 1, lineset(event.getClickedInventory().getItem(4).getItemMeta().getCustomModelData())));
                            }
                            if (event.getCurrentItem().getItemMeta().getCustomModelData() == 1034){
                                String str = event.getClickedInventory().getItem(4).getItemMeta().getLore().get(0);
                                int current = Integer.parseInt(str.replaceAll("[^\\d]", ""));
                                player.openInventory(new BusStationGUI().getInventory(event.getClickedInventory().getItem(4).getItemMeta().getDisplayName(), current - 1, lineset(event.getClickedInventory().getItem(4).getItemMeta().getCustomModelData())));
                            }
                    }
                    return;
                }
            }
            Teleport.teleportToBus(event.getCurrentItem().getItemMeta().getDisplayName(), player);
        }
    }

    private Integer lineset(int customModelData) {
        switch (customModelData){
            case 1024:
                return 11;
            case 1025:
                return 12;
            case 1026:
                return 21;
            case 1027:
                return 22;
            case 1028:
                return 31;
            case 1029:
                return 32;
            case 1030:
                return 41;
            case 1031:
                return 42;
        }
        return 0;
    }
}
