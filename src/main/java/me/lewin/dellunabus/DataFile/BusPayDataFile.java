package me.lewin.dellunabus.DataFile;

import me.lewin.dellunabus.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BusPayDataFile {
    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);

    public static void creatDataFile(){
        FileConfiguration config = getConfig();

        config.set("date", java.time.LocalDate.now().toString());

        saveDataFile(config, getDataFile());
    }

    public static File getDataFile() {
        return new File(PLUGIN.getDataFolder(), "PayDate.dat");
    }

    public static FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getDataFile());
    }

    public static void saveDataFile(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }
}
