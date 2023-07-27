package me.lewin.dellunabus.gui;

import me.lewin.dellunabus.IconDefault;
import me.lewin.dellunabus.npc.BusNPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class BusSkinSetGUI implements Listener {
    public Inventory getInventory(String name) {
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§x§0§0§b§3§b§6Bus NPC Skin 변경");

        inv.setItem(0, nameIcon(name));
        inv.setItem(3, skinSet());
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

    private ItemStack skinSet() {
        return IconDefault.iconDefault(Material.EMERALD, "NPC 스킨 변경");
    }

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().contains("§x§0§0§b§3§b§6Bus NPC Skin 변경")) {
                event.setCancelled(true);

                if (event.getClickedInventory() == event.getView().getBottomInventory()) return;
                if (event.getCurrentItem() == null) return;

                String npcName = event.getInventory().getItem(0).getItemMeta().getDisplayName();

                switch ((event.getCurrentItem()).getType()) {
                    case BARRIER:
                        player.openInventory(new BusSettingGUI().getInventory(npcName));
                        break;

                    case EMERALD:
                        BusNPC.updateNPC(npcName, player);
                        player.closeInventory();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
