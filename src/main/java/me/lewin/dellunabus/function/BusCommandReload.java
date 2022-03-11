package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BusCommandReload {
    public static void reload() {
        List<String> list11 = new ArrayList<>();
        List<String> list12 = new ArrayList<>();
        List<String> list21 = new ArrayList<>();
        List<String> list22 = new ArrayList<>();
        List<String> list31 = new ArrayList<>();
        List<String> list32 = new ArrayList<>();
        List<String> list41 = new ArrayList<>();
        List<String> list42 = new ArrayList<>();

        for (File file : BusDataFile.getDataFiles()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            switch (config.getString("station")){
                case "장미역 버스정류장":
                    list11.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "동백역 버스정류장":
                    list12.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "수국역 버스정류장":
                    list21.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "물망초역 버스정류장":
                    list22.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "국화역 버스정류장":
                    list31.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "진달래역 버스정류장":
                    list32.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "개나리역 버스정류장":
                    list41.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
                case "해바라기역 버스정류장":
                    list42.add(file.getName().substring(0, file.getName().length() -  4));
                    break;
            }

            for (File file2 : BusStationDataFile.getDataFiles()){
                FileConfiguration config2 = YamlConfiguration.loadConfiguration(file2);
                switch (file2.getName()){
                    case "장미역 버스정류장.dat":
                        config2.set("list", list11);
                        break;
                    case "동백역 버스정류장.dat":
                        config2.set("list", list12);
                        break;
                    case "수국역 버스정류장.dat":
                        config2.set("list", list21);
                        break;
                    case "물망초역 버스정류장.dat":
                        config2.set("list", list22);
                        break;
                    case "국화역 버스정류장.dat":
                        config2.set("list", list31);
                        break;
                    case "진달래역 버스정류장.dat":
                        config2.set("list", list32);
                        break;
                    case "개나리역 버스정류장.dat":
                        config2.set("list", list41);
                        break;
                    case "해바라기역 버스정류장.dat":
                        config2.set("list", list42);
                        break;
                }

                BusStationDataFile.saveDataFile(config2, file2);
            }
        }
    }
}
