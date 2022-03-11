package me.lewin.dellunabus;

import me.lewin.dellunabus.function.BusCommandReload;
import me.lewin.dellunabus.gui.BusPayManagerGUI;
import me.lewin.dellunabus.gui.BusStationManagerGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]){
        Player player = (Player) sender;

        //플레이어 입력이 아닌경우 리턴
        if (!(sender instanceof Player)) return true;

        //오피가 아닌경우 리턴
        if (!(player.isOp())) return true;

        //명령어의 원소 개수가 0이 아닌경우 리턴
        if (args.length != 1) return true;

        switch (args[0]){
            case "station":
                player.openInventory(new BusStationManagerGUI().getInventory());
                break;
            case "reload":
                BusCommandReload.reload();
                player.sendMessage("reload complete");
                break;
            case "pay":
                player.openInventory(new BusPayManagerGUI().getInventory());
                break;
        }
        return true;
    }
}
