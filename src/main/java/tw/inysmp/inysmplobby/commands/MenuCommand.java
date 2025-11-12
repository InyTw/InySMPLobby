package tw.inysmp.inysmplobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tw.inysmp.inysmplobby.utility.TeleportGUI;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c此指令只能由玩家執行。");
            return true;
        }

        Player player = (Player) sender;
        
        if (!player.hasPermission("inysmplobby.menu")) {
            player.sendMessage("§c您沒有權限執行此操作。");
            return true;
        }
        
        TeleportGUI.openGUI(player);
        return true;
    }
}