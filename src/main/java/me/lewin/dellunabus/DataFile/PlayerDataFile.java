package me.lewin.dellunabus.DataFile;

import me.lewin.dellunabus.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PlayerDataFile implements Listener {
    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!(getDataFile(player.getUniqueId().toString()).canRead())){
            creatDataFile(player);
        }
    }

    private void creatDataFile(Player player){
        String uuid = player.getUniqueId().toString();
        FileConfiguration config = getConfig(uuid);

        config.set("buscount", 0);

        saveDataFile(config, getDataFile(uuid));
    }

    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);

    public static File getDataFile(String uuid) {
        return new File(PLUGIN.getDataFolder() + "\\player", uuid + ".dat");
    }

    public static FileConfiguration getConfig(String uuid) {
        return YamlConfiguration.loadConfiguration(getDataFile(uuid));
    }

    public static void saveDataFile(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }
}