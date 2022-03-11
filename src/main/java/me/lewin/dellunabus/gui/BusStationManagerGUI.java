package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.Teleport;
import me.lewin.dellunabus.npc.BusNPC;
import me.lewin.dellunabus.npc.BusStationNPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BusStationManagerGUI implements Listener {
    public Inventory getInventory(){
        //인벤토리 객체 생성
        Inventory inv = Bukkit.getServer().createInventory(null, 27, "§x§0§0§b§3§b§6Bus Station 관리 페이지§1");

        //정류장 설정
        inv.setItem(1, station("장미역 버스정류장"));
        inv.setItem(19, station("동백역 버스정류장"));

        inv.setItem(3, station("수국역 버스정류장"));
        inv.setItem(21, station("물망초역 버스정류장"));

        inv.setItem(5, station("국화역 버스정류장"));
        inv.setItem(23, station("진달래역 버스정류장"));

        inv.setItem(7, station("개나리역 버스정류장"));
        inv.setItem(25, station("해바라기역 버스정류장"));

        //빈곳 iconNull로 채우기
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        //인벤토리 반환
        return inv;
    }

    private ItemStack station(String name){
        if (!(BusStationDataFile.getDataFile(name).canRead())){
            List<String> lore = new ArrayList<>();
            lore.add("§7 클릭 시 기차역 좌표가 설정됩니다");
            return IconDefault.iconDefault(Material.COAL_BLOCK, name, lore);
        }

        List<String> lore = new ArrayList<>();
        lore.add("§7 좌클릭: 정류장 GUI 오픈");
        lore.add("§7 우클릭: 설정 삭제");
        lore.add("§7 휠버튼: 좌표로 텔레포트");
        lore.add("§7 쉬프트+좌클릭: npc 생성");
        lore.add("§7 쉬프트+우클릭: npc 삭제");
        lore.add("§7 -------------------");
        lore.add("§7 X: " + BusStationDataFile.getConfig(name).getString("x"));
        lore.add("§7 Y: " + BusStationDataFile.getConfig(name).getString("y"));
        lore.add("§7 Z: " + BusStationDataFile.getConfig(name).getString("z"));
        return IconDefault.iconDefault(Material.EMERALD_BLOCK, name, lore, BusStationDataFile.getConfig(name).getBoolean("npc"));
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus Station 관리 페이지§1")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            String stationName = event.getCurrentItem().getItemMeta().getDisplayName();

            switch ((event.getCurrentItem()).getType()) {
                case COAL_BLOCK:
                    BusStationDataFile.creatDataFile(stationName, player);
                    break;
                case EMERALD_BLOCK:
                    switch (event.getClick()) {
                        case MIDDLE:
                            Teleport.teleportToStation(stationName, player);
                            break;
                        case LEFT:
                            player.openInventory(new BusStopManagerGUI().getInventory(stationName, 1));
                            return;
                        case RIGHT:
                            BusStationNPC.removeNPC(stationName);
                            BusStationDataFile.removeDataFile(stationName);
                            break;
                        case SHIFT_LEFT:
                            BusStationNPC.createNPC(stationName, player);
                            break;
                        case SHIFT_RIGHT:
                            BusStationNPC.removeNPC(stationName);
                            break;
                    }
                    break;
            }
            player.openInventory(new BusStationManagerGUI().getInventory());
            return;
        }
    }
}
