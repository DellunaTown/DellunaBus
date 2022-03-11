package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.PlayerDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.SetCloseStation;
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

public class BusCreateGUI implements Listener {
    public Inventory getInventory() {
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§1");

        inv.setItem(4, create());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack create(){
        return IconDefault.iconDefault(Material.DIAMOND, "현재 위치에 설치하기");
    }

    private static boolean isSilver(ItemStack item){
        if (item.getType() == Material.BLUE_DYE){
            if (item.getItemMeta().hasCustomModelData()){
                if (item.getItemMeta().getCustomModelData() == 1000){
                    return true;
                }
            }
        }
        return false;
    }
    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§1")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
            if (event.getCurrentItem() == null) return;

            switch ((event.getCurrentItem()).getType()) {
                case DIAMOND:
                    FileConfiguration playerConfig = PlayerDataFile.getConfig(player.getUniqueId().toString());
                    Boolean hasMoney = false;
                    switch (playerConfig.getInt("buscount")){
                        case 0:
                            hasMoney = true;
                            break;
                        case 1:
                            for (ItemStack item : player.getInventory().getContents()){
                                if (item == null) { continue; }
                                if (isSilver(item) && item.getAmount() >= 10){
                                    item.setAmount(item.getAmount() - 10);
                                    hasMoney = true;
                                    break;
                                }
                            }
                            break;
                        case 2:
                            for (ItemStack item : player.getInventory().getContents()){
                                if (item == null) { continue; }
                                if (isSilver(item) && item.getAmount() >= 30){
                                    item.setAmount(item.getAmount() - 30);
                                    hasMoney = true;
                                    break;
                                }
                            }
                            break;
                        case 3:
                            player.sendMessage("1인당 버스정류장 설치 제한은 최대 3개입니다.");
                            return;
                        default:
                            player.sendMessage("버스 설치 개수 데이터베이스 기록 오류입니다. 관리자에게 문의해주세요");
                            return;
                    }

                    if (!hasMoney) {
                        player.sendMessage("돈이 부족합니다.");
                        return;
                    }

                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

                    String busName = player.getName() + "(" +(int) player.getLocation().getX() + ", " +(int) player.getLocation().getY() + ", " + (int) player.getLocation().getZ() + ")";
                    String station = SetCloseStation.setStation(player.getLocation());

                    BusDataFile.creatDataFile(station, busName, player);
                    BusNPC.createNPC(busName, player);

                    player.closeInventory();
                    return;
            }
        }
    }
}
