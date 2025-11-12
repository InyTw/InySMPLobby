package tw.inysmp.inysmplobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.inysmp.inysmplobby.utility.LobbyUtility;

public class LobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c此指令只能由玩家執行。");
            return true;
        }

        Player player = (Player) sender;
        LobbyUtility.teleportToLobby(player);
        
        return true;
    }
}