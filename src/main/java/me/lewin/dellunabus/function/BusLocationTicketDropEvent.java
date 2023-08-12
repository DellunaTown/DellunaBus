package me.lewin.dellunabus.function;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BusLocationTicketDropEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerDropItemEvent event) {
        try {
            Player player = event.getPlayer();
            ItemStack item = event.getItemDrop().getItemStack();

            if (item.getType() == Material.PAPER) {
                if (item.getItemMeta().hasCustomModelData()) {
                    if (item.getItemMeta().getCustomModelData() == 1008) {
                        if (!(item.getItemMeta().getDisplayName().contains("§"))) return;

                        player.sendMessage("NPC 위치 변경권은 버릴 수 없습니다.");
                        event.setCancelled(true);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
