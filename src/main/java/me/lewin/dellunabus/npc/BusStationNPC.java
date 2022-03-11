package me.lewin.dellunabus.npc;

import me.lewin.dellunabus.DataFile.BusStationDataFile;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class BusStationNPC {
    public static void createNPC(String stationName, Player player) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, stationName);

        FileConfiguration config = BusStationDataFile.getConfig(stationName);
        config.set("npc", true);
        config.set("npcID", npc.getId());
        BusStationDataFile.saveDataFile(config, BusStationDataFile.getDataFile(stationName));

        //npc 스킨 설정 (버스 정류장 몽키씨)
        //https://minesk.in/5f4653448a7046efbd1f5a1a9ce2d46d
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent("5f4653448a7046efbd1f5a1a9ce2d46d", "ljv12AxP2iE7Vcdo78rCT1+ARCtRWEN0RidTghFsnM9zWV4//joGzdo0tZ+RQD02QLy5E3gSmGiHcZYItIHpyYYjm/Y8vpI8SiXn/PQVulUetVczW9hcLuqB5HgQdaiDfFrfJYKKW6tfZU1XKaza3+ZgQqsY9XmY9rqh08xjulAH5Ids0Cgszn4hRe6cBjsQuVslPkKmeNvAfiUgnIgSkW0wsVOkQ9YS0OkPV2NVv5eyuV9xQnsV2E8VFw3CYLATibQ/H6JHRkjiFTC1eTo/ZlH/HlS/0owME/Rr13WvRv+0hjC6j6IaK57WCrshIGEsOLnEZ7Bd7OVN3Vsm0Yrehjivcmo/1h1Phgbe6t4MLCPpUgAAu3KYghWT0YaZkyN7ELqAHUCMxpx+IVCbr+mESthEITY3aZaqek+JYfQbWBXgiiFqE4pvyh8NF21N9BjkZi0mEsap1RRagi8ip3d+I0hiql2D2vevGbV4yYlc5EqzvYHdXKy6YYiyjkvoZ49v5wvZBsWVoHcno7aJ62TOhuLC2h+W3kjHmGIbhzKGbXUseLeqopXXj/6fNY9xpVAUszQb+Hq1AGE5z+BKhbMaAD7hORoVLNszZ/KnFFQ0TMlXzrdgRxN8kqPNMa0omamp+8a0BGXYFdScgevt0Pmr0cepsDLbx87puD9Q7CtcMmI=", "ewogICJ0aW1lc3RhbXAiIDogMTYyODEzMTg5NTgwNCwKICAicHJvZmlsZUlkIiA6ICI0ZGU4MjQxNjJkZTU0MzU5YWFlMDBmMzQ1ZmMyZTY0MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYXRoYW5fS2luZyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ODNjZDJmYjA0MjkwNjk0OGU1OWM5Zjk0Y2ZiNDlmZmQ2ZWViNmMyMDVjMDQxMDRlMzM3ZTU0OTRjMjUzM2JmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
        npc.getOrAddTrait(LookClose.class).lookClose(true);

        npc.spawn(player.getLocation());
    }

    public static void removeNPC(String stationName){
        FileConfiguration config = BusStationDataFile.getConfig(stationName);

        NPC npc = CitizensAPI.getNPCRegistry().getById(config.getInt("npcID"));

        npc.destroy();

        config.set("npc", false);
        config.set("npcID", 0);

        BusStationDataFile.saveDataFile(config, BusStationDataFile.getDataFile(stationName));
    }
}
