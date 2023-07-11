package me.lewin.dellunabus;

import me.lewin.dellunabus.DataFile.PlayerDataFile;
import me.lewin.dellunabus.function.BusCreateTicketClickEvent;
import me.lewin.dellunabus.gui.*;
import me.lewin.dellunabus.npc.NPCClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Plugin plugin = JavaPlugin.getPlugin(Main.class);

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.getCommand("bus").setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new NPCClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BusCreateTicketClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BusCreateGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusStationManagerGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusStopManagerGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusStationGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusSettingGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusNameSetGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusTitleSetGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusIconSetGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusPayManagerGUI(), this);
        Bukkit.getPluginManager().registerEvents(new BusMapManagerGUI(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDataFile(), this);
    }
}
