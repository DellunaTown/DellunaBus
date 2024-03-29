package me.lewin.dellunabus.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.DataFile.BusStationDataFile;
import me.lewin.dellunabus.function.BusMap;
import me.lewin.dellunabus.function.SetCloseStation;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class BusNPC {
    // NPC 생성
    public static void createNPC(String npcName, Player player) {
        try {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, npcName);

            FileConfiguration config = BusDataFile.getConfig(npcName);
            config.set("npcID", npc.getId());
            BusDataFile.saveDataFile(config, BusDataFile.getDataFile(npcName));

            // 플러그인 재시작 없이 유저 스킨을 바로 적용할 수 있도록 수정 [Dang_Di, 2023.07.23] ===
            String uuid = player.getUniqueId().toString().replace("-", "");
            JsonObject textureProperty = getGameProfile(uuid, player, "정류장 NPC 생성");
            String metadata = textureProperty != null ? textureProperty.get("value").getAsString() : null;
            String signature = textureProperty != null ? textureProperty.get("signature").getAsString() : null;
            // ---------------------------------------------------------------------------

            //npc.getOrAddTrait(SkinTrait.class).setSkinName(player.getName());
            if (metadata != null && signature != null) {
                npc.getOrAddTrait(SkinTrait.class).clearTexture();
                npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(npcName, signature, metadata);
            } else {
                player.sendMessage("정류장 NPC 생성에 실패했습니다. 관리자에게 문의해 주세요.");
                return;
            }
            // ===========================================================================
            npc.getOrAddTrait(LookClose.class).lookClose(true);
            npc.getOrAddTrait(Gravity.class).gravitate(true);

            npc.spawn(player.getLocation());

            // DynMap 마커 추가 [Dang_Di, 2023.06.26]
            // 수정 [Dang_Di, 2023.07.11]
            BusMap.createMarker(config.getInt("npcID"), npcName, player, null);
        }
        catch (Exception e)
        {
            player.sendMessage("정류장 NPC 생성에 실패했습니다. 관리자에게 문의해 주세요.");
            e.printStackTrace();
        }
    }

    // NPC 제거
    public static void removeNPC(String npcName, Player player){
        FileConfiguration config = BusDataFile.getConfig(npcName);

        if (!(config.getBoolean("npc"))) {
            player.sendMessage("npc가 존재하지 않습니다.");
            return;
        }

        NPC npc = CitizensAPI.getNPCRegistry().getById(config.getInt("npcID"));

        npc.destroy();

        // DynMap 마커 제거 [Dang_Di, 2023.06.26]
        BusMap.removeMarker(config.getInt("npcID"), player);
    }

    // NPC 위치 변경
    public static boolean updateNPCLocation(int npcID, Player player, FileConfiguration config, String npcName){
        try
        {
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);

            npc.despawn();
            npc.spawn(player.getLocation());

            // 데이터파일, DynMap에 저장된 위치 변경 -----------------------------------------
            // 기차역 데이터 파일 수정
            String station = SetCloseStation.setStation(player.getLocation());

            // 정류장 NPC가 이동하여 속해있는 기차역이 기존과 달라질 경우
            boolean isUpdate = false;
            if (!config.getString("station").equals(station)) {
                String oldStation = config.getString("station");    // 기존 기차역
                isUpdate = true;

                // 기존에 속했던 기차역 파일 list 수정 (정류장 삭제)
                FileConfiguration oldStationConfig = BusStationDataFile.getConfig(oldStation);
                List<String> oldList = oldStationConfig.getStringList("list");
                oldList.remove(npcName);
                oldStationConfig.set("list", oldList);
                BusStationDataFile.saveDataFile(oldStationConfig, BusStationDataFile.getDataFile(oldStation));

                // 새로 속하게된 기차역 파일 list 수정 (정류장 추가)
                FileConfiguration newStationConfig = BusStationDataFile.getConfig(station);
                List<String> newList = newStationConfig.getStringList("list");
                newList.add(npcName);
                newStationConfig.set("list", newList);
                BusStationDataFile.saveDataFile(newStationConfig, BusStationDataFile.getDataFile(station));
            }

            // 버스 데이터 파일 수정
            config.set("location", player.getLocation());
            if (isUpdate) {
                config.set("station", station);
            }
            BusDataFile.saveDataFile(config, BusDataFile.getDataFile(npcName));

            // DynMap 마커
            BusMap.removeMarker(config.getInt("npcID"), player);
            BusMap.createMarker(config.getInt("npcID"), npcName, player, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NPC 스킨 변경
    public static void updateNPC(String npcName, Player player) {
        try
        {
            FileConfiguration config = BusDataFile.getConfig(npcName);
            int npcId = config.getInt("npcID");
            if (!(config.getBoolean("npc"))) {
                player.sendMessage("npc가 존재하지 않습니다.");
                return;
            }

            // ---------------------------------------------------------------------------
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcId);
            String uuid = player.getUniqueId().toString().replace("-", "");
            JsonObject textureProperty = getGameProfile(uuid, player, "정류장 NPC 스킨 변경");
            String metadata = textureProperty != null ? textureProperty.get("value").getAsString() : null;
            String signature = textureProperty != null ? textureProperty.get("signature").getAsString() : null;
            // ---------------------------------------------------------------------------

            if (metadata != null && signature != null) {
                npc.getOrAddTrait(SkinTrait.class).clearTexture();
                npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(npcName, signature, metadata);
            } else {
                player.sendMessage("정류장 NPC 스킨 변경에 실패했습니다. 관리자에게 문의해 주세요.");
                return;
            }
            npc.getOrAddTrait(LookClose.class).lookClose(true);
            npc.getOrAddTrait(Gravity.class).gravitate(true);

            // ---------------------------------------------------------------------------
            player.sendMessage("정류장 NPC 스킨이 변경되었습니다.");
        }
        catch (Exception e)
        {
            player.sendMessage("정류장 NPC 스킨 변경에 실패했습니다. 관리자에게 문의해 주세요.");
            e.printStackTrace();
        }
    }

    // 플레이어 Profile에서 스킨 파싱
    public static JsonObject getGameProfile(String uuid, Player player, String msg) {
        try
        {
            URL mojang = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                    uuid + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(mojang.openStream());

            return new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
        }
        catch (Exception e)
        {
            player.sendMessage(msg + "에 실패했습니다. 관리자에게 문의해 주세요.");
            e.printStackTrace();

            return null;
        }

    }
}
