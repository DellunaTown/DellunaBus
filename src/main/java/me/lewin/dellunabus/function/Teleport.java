package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Teleport {
    public static void teleportToBus(String busName, Player player) {
        FileConfiguration config = BusDataFile.getConfig(busName);

        Location location = config.getLocation("location");

        if (!(location.getChunk().isLoaded())){
            location.getChunk().load(true);
        }

        //플레이어 이동
        player.teleport(location);

        player.sendTitle(busName, config.getString("title"));

        //메세지 출력력
        player.sendMessage("샤랄라 뿅!");
    }

    public static void teleportToStation(String stationName, Player player) {
        FileConfiguration config = BusStationDataFile.getConfig(stationName);

        Location location = config.getLocation("location");

        if (!(location.getChunk().isLoaded())){
            location.getChunk().load(true);
        }

        //플레이어 이동
        player.teleport(location);

        player.sendTitle(stationName, "(" + config.getString("x") + ", " + config.getString("y") + ", " + config.getString("z") + ")");

        //메세지 출력력
        player.sendMessage("샤랄라 뿅!");
    }
}
