package me.lewin.dellunabus.DataFile;

import me.lewin.dellunabus.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BusDataFile {
    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);

    public static void creatDataFile(String stationName, String busName, Player player){

        FileConfiguration playerConfig = PlayerDataFile.getConfig(player.getUniqueId().toString());
        playerConfig.set("buscount", playerConfig.getInt("buscount") + 1);
        PlayerDataFile.saveDataFile(playerConfig, PlayerDataFile.getDataFile(player.getUniqueId().toString()));

        FileConfiguration stationConfig = BusStationDataFile.getConfig(stationName);
        List<String> list = stationConfig.getStringList("list");
        list.add(busName);
        stationConfig.set("list", list);
        BusStationDataFile.saveDataFile(stationConfig, BusStationDataFile.getDataFile(stationName));

        FileConfiguration config = getConfig(busName);
        config.set("location", player.getLocation());
        config.set("title", "[" + busName + "]에 오신것을 환영합니다.");
        config.set("npc", true);
        config.set("npcID", 0);
        config.set("station", stationName);
        config.set("uuid", player.getUniqueId().toString());
        ItemStack item = new ItemStack(Material.STONE);
        config.set("icon", item);
        config.set("paid", Boolean.valueOf(true));
        //추후에 기능 추가 시 이용할 항목
        config.set("separate", Boolean.valueOf(false));

        saveDataFile(config, getDataFile(busName));
    }

    public static File[] getDataFiles() {
        return new File(PLUGIN.getDataFolder() + "\\bus").listFiles();
    }

    public static File getDataFile(String busName) {
        return new File(PLUGIN.getDataFolder() + "\\bus", busName + ".dat");
    }

    public static FileConfiguration getConfig(String busName) {
        return YamlConfiguration.loadConfiguration(getDataFile(busName));
    }

    public static void saveDataFile(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }

    public static void removeDataFile(String busName){
        FileConfiguration stationConfig = BusStationDataFile.getConfig(getConfig(busName).getString("station"));
        List<String> list = stationConfig.getStringList("list");
        list.remove(busName);
        stationConfig.set("list", list);
        BusStationDataFile.saveDataFile(stationConfig, BusStationDataFile.getDataFile(getConfig(busName).getString("station")));

        FileConfiguration playerConfig = PlayerDataFile.getConfig(getConfig(busName).getString("uuid"));
        playerConfig.set("buscount", playerConfig.getInt("buscount") - 1);
        PlayerDataFile.saveDataFile(playerConfig, PlayerDataFile.getDataFile(getConfig(busName).getString("uuid")));

        getDataFile(busName).delete();
    }
}
