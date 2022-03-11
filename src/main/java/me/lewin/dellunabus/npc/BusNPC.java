package me.lewin.dellunabus.npc;

import me.lewin.dellunabus.DataFile.BusDataFile;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class BusNPC {
    public static void createNPC(String npcName, Player player) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, npcName);

        FileConfiguration config = BusDataFile.getConfig(npcName);
        config.set("npcID", npc.getId());
        BusDataFile.saveDataFile(config, BusDataFile.getDataFile(npcName));

        npc.getOrAddTrait(SkinTrait.class).setSkinName(player.getName());
        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.getOrAddTrait(Gravity.class).gravitate(true);

        npc.spawn(player.getLocation());
    }

    public static void removeNPC(String npcName, Player player){
        FileConfiguration config = BusDataFile.getConfig(npcName);

        if (!(config.getBoolean("npc"))) {
            player.sendMessage("npc가 존재하지 않습니다.");
            return;
        }

        NPC npc = CitizensAPI.getNPCRegistry().getById(config.getInt("npcID"));

        npc.destroy();
    }
}
