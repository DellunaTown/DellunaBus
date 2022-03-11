package me.lewin.dellunabus.DataFile;

import me.lewin.dellunabus.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusStationDataFile {
    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);

    public static void creatDataFile(String stationName, Player player){
        FileConfiguration config = getConfig(stationName);
        Location location = player.getLocation();

        config.set("name", stationName);

        config.set("location", location);
        config.set("x", (int) location.getX());
        config.set("y", (int) location.getY());
        config.set("z", (int) location.getZ());

        config.set("npc", "null");
        config.set("npcID", 0);

        List<String> list = new ArrayList<>();
        config.set("list", list);

        saveDataFile(config, getDataFile(stationName));
    }

    public static File getDataFile(String stationName) {
        return new File(PLUGIN.getDataFolder() + "\\station", stationName + ".dat");
    }

    public static File[] getDataFiles() {
        return new File(PLUGIN.getDataFolder() + "\\station").listFiles();
    }

    public static FileConfiguration getConfig(String stationName) {
        return YamlConfiguration.loadConfiguration(getDataFile(stationName));
    }

    public static void saveDataFile(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }

    public static void removeDataFile(String stationName){
        getDataFile(stationName).delete();
    }
}
