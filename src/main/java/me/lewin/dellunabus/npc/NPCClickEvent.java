package me.lewin.dellunabus.npc;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.function.Teleport;
import me.lewin.dellunabus.gui.BusSettingGUI;
import me.lewin.dellunabus.gui.BusStationGUI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickEvent implements Listener {
    @EventHandler
    public static void onInteractNPCEvent(NPCRightClickEvent event){
        Player player = event.getClicker();

        String clickedNPCName = event.getNPC().getName();

        if (BusStationDataFile.getDataFile(clickedNPCName).canRead()) {
                player.openInventory(new BusStationGUI().getInventory(event.getNPC().getName(), 1, lineset(clickedNPCName)));
                return;
        }

        if (BusDataFile.getDataFile(clickedNPCName).canRead()){
            FileConfiguration busConfig = BusDataFile.getConfig(clickedNPCName);
            if (player.isSneaking()){
                if (player.isOp() || player.getUniqueId().toString().equals(busConfig.getString("uuid"))){
                    player.openInventory(new BusSettingGUI().getInventory(event.getNPC().getName()));
                    return;
                }
            }
            Teleport.teleportToStation(busConfig.getString("station"), player);
        }
    }

    private static Integer lineset(String clickedNPCName) {
        switch (clickedNPCName){
            case "장미역 버스정류장":
                return 11;
            case "동백역 버스정류장":
                return 12;
            case "수국역 버스정류장":
                return 21;
            case "물망초역 버스정류장":
                return 22;
            case "국화역 버스정류장":
                return 31;
            case "진달래역 버스정류장":
                return 32;
            case "개나리역 버스정류장":
                return 41;
            case "해바라기역 버스정류장":
                return 42;
        }
        return 0;
    }
}
