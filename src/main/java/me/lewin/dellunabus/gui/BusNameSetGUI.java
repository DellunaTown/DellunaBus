package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.function.BusMap;
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

public class BusNameSetGUI implements Listener {
    public Inventory getInventory(String name){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§a");
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
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§a")) {
            if (event.getCurrentItem() == null) return;
            if (!(event.getCurrentItem().getType() == Material.NAME_TAG && event.getCurrentItem().getItemMeta().hasCustomModelData() && event.getCurrentItem().getItemMeta().getCustomModelData() == 1))
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
        if (event.getView().getTitle().contains("§x§0§0§b§3§b§6        ˚₊· Delluna   Bus  ₊·§a")) {
            Player player = (Player) event.getPlayer();
            String npcName = event.getInventory().getItem(0).getItemMeta().getDisplayName();
            ItemStack item = event.getInventory().getItem(4);
            if (item == null) return;

            if (item.getType() == Material.NAME_TAG){
                if (item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 1){
                    String name = item.getItemMeta().getDisplayName();

                    if (BusDataFile.getDataFile(name).canRead()){
                        player.sendMessage("이미 존재하는 이름입니다.");
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                            return;
                        }
                        player.getInventory().addItem(item);
                        return;
                    }

                    if (!(name.matches ("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|\\s]*"))) {
                        player.sendMessage("버스 이름은 숫자, 영어, 한글만 가능합니다.");
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                            return;
                        }
                        player.getInventory().addItem(item);
                        return;
                    }

                    FileConfiguration busConfig = BusDataFile.getConfig(npcName);
                    BusDataFile.saveDataFile(busConfig, BusDataFile.getDataFile(name));
                    BusDataFile.getDataFile(npcName).delete();

                    FileConfiguration stationConfig = BusStationDataFile.getConfig(busConfig.getString("station"));
                    List<String> list = stationConfig.getStringList("list");
                    Integer index = list.indexOf(npcName);
                    list.set(index, name);
                    stationConfig.set("list", list);
                    BusStationDataFile.saveDataFile(stationConfig, BusStationDataFile.getDataFile(busConfig.getString("station")));

                    NPC npc = CitizensAPI.getNPCRegistry().getById(busConfig.getInt("npcID"));
                    npc.setName(name);

                    // DynMap 마커 이름 수정 (제거 후 생성) [Dang_Di, 2023.07.11]
                    BusMap.removeMarker(busConfig.getInt("npcID"), player);
                    BusMap.createMarker(busConfig.getInt("npcID"), name, player, npc);

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
                    }
                }
            }
        }
    }
}
