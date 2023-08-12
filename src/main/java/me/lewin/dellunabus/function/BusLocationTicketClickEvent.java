package me.lewin.dellunabus.function;

import me.lewin.dellunabus.DataFile.BusDataFile;
import me.lewin.dellunabus.npc.BusNPC;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BusLocationTicketClickEvent implements Listener {
    @EventHandler
    private void onPlayerUse(PlayerInteractEvent event){
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item.getType() == Material.PAPER){
            if (item.getItemMeta().hasCustomModelData()){
                if (item.getItemMeta().getCustomModelData() == 1008){
                    if (!(item.getItemMeta().getDisplayName().contains("§"))) return;

                    // NPC 존재 여부 검증
                    // lore : [0] uuid / [1] 정류장 NPC 이름
                    List<String> lore = item.getItemMeta().getLore();
                    String loreUuid = null;
                    String loreNpcName = null;
                    if (lore != null && lore.size() == 2) {
                        loreUuid = lore.get(0);
                        loreNpcName = lore.get(1);
                    }

                    FileConfiguration config = BusDataFile.getConfig(loreNpcName);

                    if (!(config.getBoolean("npc"))) {
                        player.sendMessage("npc가 존재하지 않습니다.");
                        return;
                    }

                    // --------------------------------------------------------------------------

                    // 위치 변경권 로어 검증
                    if (String.valueOf(loreUuid).length() < 1 || String.valueOf(loreNpcName).length() < 1) {
                        player.sendMessage("유효하지 않은 위치 변경권 입니다. 관리자에게 문의해 주세요.");
                        return;
                    }
                    // --------------------------------------------------------------------------

                    // 버스 정류장 설치자 검증 (uuid)
                    String configUuid = config.getString("uuid");

                    if (configUuid == null || !(configUuid.equals(loreUuid))) {
                        player.sendMessage("설치자만 위치를 변경할 수 있습니다.");
                        return;
                    }
                    // --------------------------------------------------------------------------

                    // NPC 위치 변경
                    if (BusNPC.updateNPCLocation(config.getInt("npcID"), player, config, loreNpcName)) {
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                        player.sendMessage("NPC의 위치가 변경되었습니다.");
                    }
                    else{
                        player.sendMessage("NPC 위치 변경 오류입니다. 관리자에게 문의해 주세요.");
                    }
                }
            }
        }
    }
}
