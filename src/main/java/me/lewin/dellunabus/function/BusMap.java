package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import net.citizensnpcs.api.npc.NPC;

import java.io.*;

public class BusMap {

    // static LinkedHashMap<String, LinkedHashMap> dynMap = new LinkedHashMap<String, LinkedHashMap>();           // dynMap 데이터 전문
    // static LinkedHashMap<String, LinkedHashMap> setsMap = new LinkedHashMap<String, LinkedHashMap>();          // dynMap 데이터의 sets
    // static LinkedHashMap<String, LinkedHashMap> busSetMap = new LinkedHashMap<String, LinkedHashMap>();        // sets 데이터의 busSet
    // public static File busFile;
    public static final DynmapAPI DYNMAP_API  = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");

    // Method ===============================================================

    // 데이터 갱신
    //public static void initData() throws FileNotFoundException {
    //    busFile = getDynMapDataFile();
        // dynMap = new Yaml().load(new FileReader(busFile));
        // setsMap = dynMap.get("sets");
        // busSetMap = setsMap.get("busSet");
    //}

    // DynMap 데이터 불러오기
    //public static File getDynMapDataFile() {
    //    return new File("plugins\\dynmap", "markers.yml");
    //}

    // busSet 반환
    public static MarkerSet getBusMarkerSet() {

        return (DYNMAP_API != null ? DYNMAP_API.getMarkerAPI().getMarkerSet("busSet") : null);
    }

    // DynMap의 데이터 파일에 busSet 데이터 존재 여부
    public static boolean isBusSetEmpty() {
        //initData();
        //return (busSetMap == null || busSetMap.toString().replaceAll(" ", "").length() < 3);
        MarkerSet markerSet = getBusMarkerSet(); // busSet
        return (markerSet == null || markerSet.getMarkers().size() < 1);
    }

    // 버스정류장 아이콘 반환
    public static MarkerIcon getBusMarkerIcon() {
        return (DYNMAP_API != null ? DYNMAP_API.getMarkerAPI().getMarkerIcon("sign") : null);
    }

    // DynMap의 busSet과 데이터 생성
    public static void createSet(Player player) {
        try
        {
            /*
            // dynmap set 생성 커맨드
            // id : 고유 id
            // label : 보여지는 Set 이름
            // hide : true : 체크 해제 (디폴트 숨김으로 설정)
            // prio : 다른 세트와의 우선 순위
            // minzoom : 특정 줌 레벨이 선택될 때까지 세트 내의 마커를 숨겨짐
            player.performCommand("dmarker addset id:busSet label:\"Bus\" hide:true prio:2 minzoom:0");
            */

            // busSet이 없을 경우 생성
            MarkerSet markerSet = getBusMarkerSet(); // busSet
            if (markerSet == null && DYNMAP_API != null) {
                // busSet이 null일 경우 생성 (id, label, icon, 영구저장 (false시 서버 재시작할 경우 저장 안됨))
                markerSet = DYNMAP_API.getMarkerAPI().createMarkerSet("busSet", "Bus", null, true);
                player.sendMessage("버스셋을 생성합니다.");
                // 다른 세트와의 우선 순위
                markerSet.setLayerPriority(2);
                // 숨김
                markerSet.setHideByDefault(true);
            }

            // ------------------------------------------------------------------------------------

            // 버스정류장 데이터 불러온 후 마커 생성
            int cnt = 0;                    // 생성 성공한 정류장 수
            boolean created;                // 생성 성공 여부

            if (markerSet != null) {
                for (File file : BusDataFile.getDataFiles()) {
                    created = false;
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    Location tempLocation = (Location) config.get("location");
                    /*
                    // 마커 고유 ID는 버스 데이터의 npcID 로 대체 후 생성
                    player.performCommand("dmarker add id:" + config.get("npcID") +
                            " \" " + file.getName().substring(0, file.getName().length() - 4) + "\" icon:sign set:busSet x:" +
                            tempLocation.getX() + " y:" + tempLocation.getY() + " z:" + tempLocation.getZ() + " world:world");
                    */
                    // 마커 고유 ID는 버스 데이터의 npcID 로 대체, 마지막 Parameter 영구저장 여부 (false시 서버 재시작할 경우 저장 안됨)
                    Marker marker = markerSet.findMarker(String.valueOf(config.get("npcID")));
                    if (marker == null && tempLocation != null) {
                        markerSet.createMarker(String.valueOf(config.get("npcID")), file.getName().substring(0, file.getName().length() - 4),
                                "world", tempLocation.getX(), tempLocation.getY(), tempLocation.getZ(),
                                getBusMarkerIcon(), true);
                        created = true;
                    }

                    if (created)
                        cnt++;
                }
            }

            player.sendMessage(cnt + "개의 버스정류장 마커를 신규 생성했습니다.");
        }
        catch (Exception e)
        {
            player.sendMessage("버스정류장 마커 생성시 오류가 발견되었습니다.");

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            player.sendMessage(sw.toString());

            e.printStackTrace();
        }
    }

    // DynMap의 busSet 데이터 제거
    public static void deleteSet(Player player) {
        try
        {
            /*
            // dynmap set 제거 커맨드
            player.performCommand("dmarker deleteset id:busSet");
            */

            // 버스정류장 데이터 불러온 후 마커 제거 ----------------------------------------------------------------
            MarkerSet markerSet = getBusMarkerSet(); // busSet
            int cnt = 0;                    // 제거 성공한 정류장 수
            boolean removed;                // 제거 성공 여부

            if (markerSet != null) {
                for (File file : BusDataFile.getDataFiles()) {
                    removed = false;
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                    // 마커 고유 ID는 버스 데이터의 npcID 로 대체
                    Marker marker = markerSet.findMarker(String.valueOf(config.get("npcID")));
                    if (marker != null) {
                        removed = true;
                        marker.deleteMarker();
                    }

                    if (removed)
                        cnt++;
                }
            }

            player.sendMessage(cnt + "개의 버스정류장 마커를 제거했습니다.");
        }
        catch (Exception e)
        {
            player.sendMessage("버스정류장 마커 제거시 오류가 발견되었습니다.");

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            player.sendMessage(sw.toString());

            e.printStackTrace();
        }

        //player.sendMessage("버스정류장 마커를 모두 제거했습니다. 웹지도에는 새로고침 후 적용됩니다.");
    }

    // 버스정류장 생성시 마커 생성
    // 마커 이름을 수정하려는 경우를 제외하고, 마지막 파라미터는 null로 받습니다. [Dang_Di, 2023.07.11]
    public static void createMarker(int npcID, String npcName, Player player, NPC npc) {
        try
        {
            /*
            // 마커 고유 ID는 버스 데이터의 npcID 로 대체 후 생성
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "dmarker add id:" + npcID + " \" " + npcName +
                            "\" icon:sign set:busSet " +
                            " x:" + player.getLocation().getX() + " y:" + player.getLocation().getY() +
                            " z:" + player.getLocation().getZ() + " world:world");
             */

            // busSet이 없을 경우 생성
            MarkerSet markerSet = getBusMarkerSet(); // busSet
            if (markerSet == null && DYNMAP_API != null) {
                // busSet이 null일 경우 생성 (id, label, icon, 영구저장 (false시 서버 재시작할 경우 저장 안됨))
                markerSet = DYNMAP_API.getMarkerAPI().createMarkerSet("busSet", "Bus", null, true);
                player.sendMessage("버스셋을 생성합니다.");

                // 다른 세트와의 우선 순위
                markerSet.setLayerPriority(2);
                // 숨김
                markerSet.setHideByDefault(true);
            }

            // ------------------------------------------------------------------------------------

            boolean created = false;
            if (markerSet != null) {
                // 마커 고유 ID는 버스 데이터의 npcID 로 대체, 마지막 Parameter 영구저장 여부 (false시 서버 재시작할 경우 저장 안됨)
                Marker marker = markerSet.findMarker(String.valueOf(npcID));
                if (marker == null) {
                    double locX = player.getLocation().getX();
                    double locY = player.getLocation().getY();
                    double locZ = player.getLocation().getZ();

                    if (npc != null) {
                        locX = npc.getStoredLocation().getX();
                        locY = npc.getStoredLocation().getY();
                        locZ = npc.getStoredLocation().getZ();
                    }

                    markerSet.createMarker(String.valueOf(npcID), npcName,
                            "world", locX, locY, locZ, getBusMarkerIcon(), true);
                    created = true;
                }
            }

            if (!created)
                player.sendMessage("버스정류장이 웹맵에 생성되지 않았습니다. 관리자에게 문의해 주세요.");

        }
        catch (Exception e)
        {
            player.sendMessage("버스정류장이 웹맵에 생성될 때 오류가 발생했습니다. 관리자에게 문의해 주세요.");

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            player.sendMessage(sw.toString());

            e.printStackTrace();
        }
    }


    // 버스정류장 제거시 마커 제거
    public static void removeMarker(int npcID, Player player) {
        try
        {
            /*
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "dmarker delete id:" + npcID +" set:busSet");
             */

            // 버스정류장 데이터 불러온 후 마커 제거 ----------------------------------------------------------------
            MarkerSet markerSet = getBusMarkerSet(); // busSet
            boolean removed = false;        // 제거 성공 여부

            if (markerSet != null) {
                // 마커 고유 ID는 버스 데이터의 npcID 로 대체
                Marker marker = markerSet.findMarker(String.valueOf(npcID));
                if (marker != null) {
                    removed = true;
                    marker.deleteMarker();
                }
            }

            if (!removed)
                player.sendMessage("버스정류장이 웹맵에서 제거되지 않았습니다. 관리자에게 문의해 주세요.");
        }
        catch (Exception e)
        {
            player.sendMessage("버스정류장이 웹맵에서 제거될 때 오류가 발생했습니다. 관리자에게 문의해 주세요.");

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            player.sendMessage(sw.toString());

            e.printStackTrace();
        }
    }

}
