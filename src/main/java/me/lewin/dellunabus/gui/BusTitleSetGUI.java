package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.IconDefault;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
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

import java.util.List;

public class BusTitleSetGUI implements Listener {
    public Inventory getInventory(String name){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§b");
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
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§b")) {

            if (event.getCurrentItem() == null) return;
            if (!(event.getCurrentItem().getType() == Material.PAPER && event.getCurrentItem().getItemMeta().hasCustomModelData() && event.getCurrentItem().getItemMeta().getCustomModelData() == 101))
                event.setCancelled(true);

            String name = event.getInventory().getItem(0).getItemMeta().getDisplayName();

            switch ((event.getCurrentItem()).getType()) {
                case BARRIER:
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
            }

            return;
        }
    }

    @EventHandler
    private void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§b")) {
            Player player = (Player) event.getPlayer();
            String npcName = event.getInventory().getItem(0).getItemMeta().getDisplayName();
            ItemStack item = event.getInventory().getItem(4);
            if (item == null) return;

            if (item.getType() == Material.PAPER){
                if (item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 101){
                    String title = item.getItemMeta().getDisplayName();

                    FileConfiguration busConfig = BusDataFile.getConfig(npcName);

                    if (busConfig.getString("title").equals(title)){
                        player.sendMessage("기존과 동일한 문구입니다.");
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                            return;
                        }
                        player.getInventory().addItem(item);
                        return;
                    }

                    busConfig.set("title", title);
                    BusDataFile.saveDataFile(busConfig, BusDataFile.getDataFile(npcName));

                    player.sendMessage("변경되었습니다.");

                    if (item.getAmount() > 1){
                        player.sendMessage("변경권은 1개만 받겠습니다.");
                        item.setAmount(item.getAmount() - 1);
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                            return;
                        }
                        player.getInventory().addItem(item);
                        return;
                    }
                    return;
                }
            }
            return;
        }
    }
}
