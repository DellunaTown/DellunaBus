package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.npc.BusNPC;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BusPay {
    public static void pay(String name, Player player){
        int i = 0;
        for (ItemStack item : player.getInventory().getContents()){
            if (item == null) { continue; }
            if (isSilver(item)){
                item.setAmount(item.getAmount() - 1);
                i++;
            }
            if (isCopper(item) && item.getAmount() >= 3){
                item.setAmount(item.getAmount() - 3);
                i++;
            }
            if (i == 1){
                break;
            }
        }

        if (i < 1) {
            player.sendMessage("돈이 부족합니다");
            return;
        }
        FileConfiguration config = BusDataFile.getConfig(name);
        config.set("paid", true);
        BusDataFile.saveDataFile(config, BusDataFile.getDataFile(name));
        player.sendMessage("연장되었습니다");
    }

    private static boolean isSilver(ItemStack item){
        if (item.getType() == Material.BLUE_DYE){
            if (item.getItemMeta().hasCustomModelData()){
                if (item.getItemMeta().getCustomModelData() == 1000){
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isCopper(ItemStack item){
        if (item.getType() == Material.GREEN_DYE){
            if (item.getItemMeta().hasCustomModelData()){
                if (item.getItemMeta().getCustomModelData() == 1000){
                    return true;
                }
            }
        }
        return false;
    }

    public static void resetPay(Player player){
        for (File file : BusDataFile.getDataFiles()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("paid", false);
            BusDataFile.saveDataFile(config, file);
        }
        player.sendMessage("리셋되었습니다.");
    }

    public static void remove(Player player) {
        for (File file : BusDataFile.getDataFiles()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!(config.getBoolean("paid"))){
                player.sendMessage(file.getName().substring(0, file.getName().length() - 4));
                BusNPC.removeNPC(file.getName().substring(0, file.getName().length() - 4), player);
                BusDataFile.removeDataFile(file.getName().substring(0, file.getName().length() - 4));
            }
        }
    }
}
