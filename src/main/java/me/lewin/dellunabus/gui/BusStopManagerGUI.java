package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.Teleport;
import me.lewin.dellunabus.npc.BusNPC;
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

public class BusStopManagerGUI implements Listener {
    public Inventory getInventory(String stationName, Integer current){
        Inventory inv = Bukkit.getServer().createInventory(null, 54, "§x§0§0§b§3§b§6Bus Station 관리 페이지§2");

        FileConfiguration stationConfig = BusStationDataFile.getConfig(stationName);

        List<String> list = stationConfig.getStringList("list");

        int count = list.size();

        for (int i = 45 * (current - 1); i < 45 * current; i++){
            if (i >= count) break;

            FileConfiguration busConfig = BusDataFile.getConfig(list.get(i));

            ItemStack item = busConfig.getItemStack("icon");
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(list.get(i));

            List<String> lore = new ArrayList<>();

            lore.add("§7 좌클릭: 해당 좌표로 이동");
            lore.add("§7 쉬프트+좌클릭: 해당 버스 삭제");

            meta.setLore(lore);

            item.setItemMeta(meta);

            inv.addItem(item);
        }

        if (current != 1)
            inv.setItem(48, previous());

        inv.setItem(49, station(stationName, current));

        if (count > 45*current)
            inv.setItem(50, next());

        inv.setItem(53, IconDefault.iconBack());

        for (int i = 45; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack station(String stationName, Integer current){
        List<String> lore = new ArrayList<>();
        lore.add("§7 -------------------");
        lore.add(" §a" + current + " 페이지");
        lore.add("§7 -------------------");

        switch (stationName){
            case "장미역 버스정류장":
                return IconDefault.iconDefault(Material.RED_WOOL, "장미역 버스정류장", lore,1);
            case "동백역 버스정류장":
                return IconDefault.iconDefault(Material.RED_WOOL, "동백역 버스정류장", lore, 1);
            case "수국역 버스정류장":
                return IconDefault.iconDefault(Material.BLUE_WOOL, "수국역 버스정류장", lore, 1);
            case "물망초역 버스정류장":
                return IconDefault.iconDefault(Material.BLUE_WOOL, "물망초역 버스정류장", lore, 1);
            case "국화역 버스정류장":
                return IconDefault.iconDefault(Material.LIME_WOOL, "국화역 버스정류장", lore, 1);
            case "진달래역 버스정류장":
                return IconDefault.iconDefault(Material.LIME_WOOL, "진달래역 버스정류장", lore, 1);
            case "개나리역 버스정류장":
                return IconDefault.iconDefault(Material.YELLOW_WOOL, "개나리역 버스정류장", lore, 1);
            case "해바라기역 버스정류장":
                return IconDefault.iconDefault(Material.YELLOW_WOOL, "해바라기역 버스정류장", lore, 1);
        }
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, " ");
    }
    private ItemStack next(){
        return IconDefault.iconDefault(Material.ARROW, "다음 페이지", 1);
    }
    private ItemStack previous(){
        return IconDefault.iconDefault(Material.ARROW, "이전 페이지", 1);
    }

    @EventHandler
    public static void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus Station 관리 페이지§2")){
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getItemMeta().hasCustomModelData()) {
                if (event.getCurrentItem().getItemMeta().getCustomModelData() == 1){
                    switch ((event.getCurrentItem()).getType()){
                        case BARRIER:
                            player.openInventory(new BusStationManagerGUI().getInventory());
                            break;
                        case ARROW:
                            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("다음 페이지") ){
                                String str = event.getClickedInventory().getItem(49).getItemMeta().getLore().get(1);
                                int current = Integer.parseInt(str.replaceAll("[^\\d]", ""));
                                player.openInventory(new BusStopManagerGUI().getInventory(event.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), current + 1));
                            }
                            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("이전 페이지")){
                                String str = event.getClickedInventory().getItem(49).getItemMeta().getLore().get(1);
                                int current = Integer.parseInt(str.replaceAll("[^\\d]", ""));
                                player.openInventory(new BusStopManagerGUI().getInventory(event.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), current - 1));
                            }
                    }
                    return;
                }
            }

            switch (event.getClick()){
                case LEFT:
                    Teleport.teleportToBus(event.getCurrentItem().getItemMeta().getDisplayName(), player);
                    break;
                case SHIFT_LEFT:
                    String name = event.getCurrentItem().getItemMeta().getDisplayName();
                    BusNPC.removeNPC(name, player);
                    BusDataFile.removeDataFile(name);
                    String str = event.getClickedInventory().getItem(49).getItemMeta().getLore().get(1);
                    int current = Integer.parseInt(str.replaceAll("[^\\d]", ""));
                    player.openInventory(new BusStopManagerGUI().getInventory(event.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), current));
                    break;
            }
            return;
        }
    }
}
