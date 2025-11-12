package tw.inysmp.inysmplobby.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tw.inysmp.inysmplobby.InySMPLobby;

public class LobbyUtility {

    public static void teleportToLobby(Player player) {
        InySMPLobby plugin = InySMPLobby.getInstance();
        ConfigurationSection lobbyCfg = plugin.getConfig().getConfigurationSection("lobby-location");
        String messageKey = "lobby-location-not-set"; // 假設從 messages.yml 讀取

        if (lobbyCfg == null || !lobbyCfg.contains("world")) {
            // player.sendMessage(plugin.getMessage(messageKey));
            player.sendMessage("§c大廳位置尚未設定，請聯繫管理員！");
            return;
        }

        String worldName = lobbyCfg.getString("world");
        World world = Bukkit.getWorld(worldName);
        
        if (world == null) {
            // player.sendMessage(plugin.getMessage(messageKey));
            player.sendMessage("§c大廳世界不存在，請聯繫管理員！");
            return;
        }

        double x = lobbyCfg.getDouble("x");
        double y = lobbyCfg.getDouble("y");
        double z = lobbyCfg.getDouble("z");
        float yaw = (float) lobbyCfg.getDouble("yaw");
        float pitch = (float) lobbyCfg.getDouble("pitch");

        Location lobbyLoc = new Location(world, x, y, z, yaw, pitch);
        
        player.teleport(lobbyLoc);
        // player.sendMessage(plugin.getMessage("teleport-to-lobby"));
        player.sendMessage("§a已將您傳送到大廳！");
    }
}