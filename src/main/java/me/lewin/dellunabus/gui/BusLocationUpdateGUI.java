package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.DataFile.PlayerDataFile;
import me.lewin.dellunabus.IconDefault;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BusLocationUpdateGUI implements Listener {
    public Inventory getInventory(String name) {
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6Bus NPC 위치 변경");

        inv.setItem(0, nameIcon(name));
        inv.setItem(3, locationSet());
        inv.setItem(5, IconDefault.iconBack());

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, IconDefault.iconNull());
        }

        return inv;
    }

    private ItemStack nameIcon(String name) {
        return IconDefault.iconDefault(Material.WHITE_STAINED_GLASS_PANE, name);
    }

    private ItemStack locationSet() {
        List<String> lore = new ArrayList<>();
        lore.add("§7 버스 1개 보유시 : 무료");
        lore.add("§7 버스 2개 보유시 : 3 은화");
        lore.add("§7 버스 3개 이상 보유시 : 5 은화");
        lore.add("§7 쉬프트 + 좌클릭");
        return IconDefault.iconDefault(Material.EMERALD, "버스 위치 변경", lore);
    }

    private static boolean isSilver(ItemStack item) {
        return (item.getType() == Material.BLUE_DYE && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 1000);
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus NPC 위치 변경")) {
                event.setCancelled(true);

                if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
                if (event.getCurrentItem() == null) return;

                String npcName = event.getInventory().getItem(0).getItemMeta().getDisplayName();

                switch ((event.getCurrentItem()).getType()) {
                    case BARRIER:
                        player.openInventory(new BusSettingGUI().getInventory(npcName));
                        break;

                    case EMERALD:
                        if (event.getClick() != ClickType.SHIFT_LEFT) return;

                        FileConfiguration playerConfig = PlayerDataFile.getConfig(player.getUniqueId().toString());
                        boolean hasMoney = false;

                        // 돈 검증
                        // 1개 보유 : 무료
                        // 2개 보유 : 3은화
                        // 3개 이상 보유 : 5은화
                        switch (playerConfig.getInt("buscount")) {
                            case 1:
                                hasMoney = true;
                                break;
                            case 2:
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null) {
                                        continue;
                                    }
                                    if (isSilver(item) && item.getAmount() >= 3) {
                                        item.setAmount(item.getAmount() - 3);
                                        hasMoney = true;
                                        break;
                                    }
                                }
                                break;
                            case 3:
                            case 4:
                            case 5:
                                for (ItemStack item : player.getInventory().getContents()) {
                                    if (item == null) {
                                        continue;
                                    }
                                    if (isSilver(item) && item.getAmount() >= 5) {
                                        item.setAmount(item.getAmount() - 5);
                                        hasMoney = true;
                                        break;
                                    }
                                }
                                break;
                            default:
                                player.sendMessage("버스 설치 개수 데이터베이스 기록 오류입니다. 관리자에게 문의해 주세요");
                                return;
                        }

                        if (!hasMoney) {
                            player.sendMessage("돈이 부족합니다.");
                            return;
                        }

                        // 이동권 지급
                        ItemStack item = new ItemStack(Material.PAPER);
                        ItemMeta meta = item.getItemMeta();
                        List<String> lore = new ArrayList<>();

                        lore.add(String.valueOf(player.getUniqueId()));
                        lore.add(npcName);
                        meta.setDisplayName("§x§0§0§b§3§b§6Bus NPC 위치 변경권");
                        meta.setLore(lore);
                        meta.setCustomModelData(1008);
                        item.setItemMeta(meta);

                        if (player.getInventory().firstEmpty() == -1) {
                            player.getWorld().dropItem(player.getLocation(), item);
                            player.sendMessage("인벤토리가 부족하여 해당 위치에 아이템을 드롭하였습니다.");
                        } else {
                            player.getInventory().addItem(item);
                        }
                        player.sendMessage("Bus NPC 위치 변경권이 지급되었습니다. 좌클릭 또는 우클릭 시 즉시 사용됩니다.");

                        player.closeInventory();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
