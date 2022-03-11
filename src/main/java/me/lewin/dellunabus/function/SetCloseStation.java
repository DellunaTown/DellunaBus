package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusStationDataFile;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetCloseStation {
    public static String setStation(Location location){
        List<Integer> distanceList = new ArrayList<>();
        List<String> StationList = new ArrayList<>();

        for (File station : BusStationDataFile.getDataFiles()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(station);
            Integer distance = (int) location.distance(config.getLocation("location"));
            distanceList.add(distance);
            StationList.add(station.getName());
        }
        int min = distanceList.stream().min(Integer::compareTo).orElse(-1);
        String name = StationList.get(distanceList.indexOf(min));
        return name.substring(0,name.length() - 4);
    }
}
