package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.function.BusMap;

import me.lewin.dellunabus.IconDefault;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;

public class BusMapManagerGUI implements Listener {

    public Inventory getInventory(){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6Bus Map 관리 페이지");

        inv.setItem(2, mapIcon());
        inv.setItem(6, removeIcon());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack mapIcon(){
        return IconDefault.iconDefault(Material.PAPER, "버스정류장 마커 생성");
    }

    private ItemStack removeIcon(){
        List<String> lore = new ArrayList<>();
        lore.add("§7 쉬프트+좌클릭");
        return IconDefault.iconDefault(Material.REDSTONE, "버스정류장 마커 모두 제거", lore);
    }


    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {

        try {
            Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus Map 관리 페이지")) {
                event.setCancelled(true);
                if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
                if (event.getCurrentItem() == null) return;

                switch ((event.getCurrentItem()).getType()) {
                    // DynMap의 busSet과 데이터 생성
                    case PAPER:
                        //if (!BusMap.isBusSetEmpty())
                        //{
                        //    // busSet이 비어있지 않은 경우
                        //    player.sendMessage("기존 버스정류장 마커를 제거해 주세요!");
                        //}
                        //else
                        //{
                        //    // 생성
                        //    BusMap.createSet(player);
                        //}

                        // 생성
                        BusMap.createSet(player);

                        break;

                    // DynMap의 busSet 데이터 제거
                    case REDSTONE:
                        if (event.getClick() != ClickType.SHIFT_LEFT) return;

                        // ==================================================================
                        // DynMap 데이터 파일이 존재하지 않을 경우
                        //if (!BusMap.isExistsFile())
                        //{
                        //    player.sendMessage("DynMap 데이터 파일이 존재하지 않습니다.");
                        //    return;
                        //}

                        if (BusMap.isBusSetEmpty())
                        {
                            // busSet이 비어있는 경우
                            player.sendMessage("제거할 버스정류장 마커가 없습니다.");
                        }
                        else
                        {
                            // 제거
                            BusMap.deleteSet(player);
                        }

                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


