package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusPayDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.BusPay;
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

public class BusPayManagerGUI implements Listener {
    public Inventory getInventory(){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6Bus Pay 관리 페이지");

        inv.setItem(2, busIcon());

        inv.setItem(6, resetIcon());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack busIcon(){
        List<String> lore = new ArrayList<>();
        lore.add("§7 쉬프트+좌클릭");
        return IconDefault.iconDefault(Material.DIAMOND, "미연장 버스 삭제", lore);
    }

    private ItemStack resetIcon(){
        List<String> lore = new ArrayList<>();
        lore.add("§7 마지막 리셋 날짜 : " + BusPayDataFile.getConfig().getString("date"));
        return IconDefault.iconDefault(Material.REDSTONE, "버스 리셋", lore);
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus Pay 관리 페이지")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            switch ((event.getCurrentItem()).getType()) {
                case DIAMOND:
                    if (event.getClick() != ClickType.SHIFT_LEFT) return;
                    BusPay.remove(player);
                    break;
                case REDSTONE:
                    if (!(BusPayDataFile.getDataFile().canRead())){
                        BusPayDataFile.creatDataFile();
                    }
                    BusPay.resetPay(player);
                    FileConfiguration payconfig = BusPayDataFile.getConfig();
                    payconfig.set("date", java.time.LocalDate.now().toString());
                    BusPayDataFile.saveDataFile(payconfig, BusPayDataFile.getDataFile());
                    player.openInventory(new BusPayManagerGUI().getInventory());
                    break;
            }
            return;
        }
    }
}
