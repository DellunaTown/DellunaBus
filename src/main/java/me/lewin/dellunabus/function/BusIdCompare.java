package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusIdCompare {
    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);

    // NPC ID가 서로 다른 데이터 추출
    public static void idCompare(Player player){
        try
        {

            // 1. NPC ID가 서로 다른 데이터 추출 (errList) ---------------------------
            int cnt = 0;
            List<String> errList = new ArrayList<>();
            for (File file : BusDataFile.getDataFiles()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                NPC npc = CitizensAPI.getNPCRegistry().getById(config.getInt("npcID"));

                if (npc == null) {
                    String busName = file.getName().substring(0, file.getName().length() - 4);
                    errList.add(busName);
                    player.sendMessage(busName);

                    cnt++;
                }
            }

            FileConfiguration listConfig = getConfig();
            listConfig.set("errNpcList", errList);
            saveDataFile(listConfig, getDataFile());

            player.sendMessage("ID가 다른 데이터 " + cnt + " 개 발견");
            // -------------------------------------------------------------------


            // 2. 시티즌을 기준으로 버스 NPC ID 변경와 함께 웹맵 ID 변경 처리-------------
            for (NPC tempNpc : CitizensAPI.getNPCRegistry()) {
                for (String npcname : errList) {
                    if (tempNpc.getName().equals(npcname)) {
                        int citizenId = tempNpc.getId();

                        File file = BusDataFile.getDataFile(npcname);
                        FileConfiguration config = BusDataFile.getConfig(npcname);

                        // ID 변경
                        config.set("npcID", citizenId);
                        BusDataFile.saveDataFile(config, file);
                    }
                }
            }

            player.sendMessage(cnt + " 개의 버스 정류장 데이터 파일의 NPC ID를 갱신했습니다.");

            if (cnt > 0) {
                player.sendMessage("웹 맵은 /bus map 명령어로 직접 갱신해주시고");
                player.sendMessage("디버그를 한 번 더 진행해 ID가 다른 데이터 개수를 확인해 주세요.");
                player.sendMessage("데이터 개수가 0개일 경우에만 미연장 버스 삭제를 진행해주세요.");
            }

            if (cnt == 0)
                player.sendMessage("미연장 버스 삭제를 진행해도 좋습니다.");

            // -------------------------------------------------------------------

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void creatDataFile(){
        FileConfiguration config = getConfig();

        config.set("errNpcList", new ArrayList<String>());

        saveDataFile(config, getDataFile());
    }

    public static File getDataFile() {
        return new File(PLUGIN.getDataFolder(), "CompareList.dat");
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
