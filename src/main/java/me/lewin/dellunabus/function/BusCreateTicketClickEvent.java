package me.lewin.dellunabus.function;

import me.lewin.dellunabus.gui.BusCreateGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BusCreateTicketClickEvent implements Listener {
    @EventHandler
    private void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.getType() == Material.PAPER){
            if (item.getItemMeta().hasCustomModelData()){
                if (item.getItemMeta().getCustomModelData() == 1004){
                    if (item.getItemMeta().getDisplayName().contains("CBT")) return;
                    if (!(item.getItemMeta().getDisplayName().contains("§"))) return;
                    player.openInventory(new BusCreateGUI().getInventory());
                }
            }
        }
        return;
    }
}
