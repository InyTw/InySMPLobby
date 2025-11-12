package tw.inysmp.inysmplobby.listeners;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tw.inysmp.inysmplobby.InySMPLobby;
import tw.inysmp.inysmplobby.utility.LobbyUtility;
import tw.inysmp.inysmplobby.utility.TeleportGUI;

public class PlayerEventListener implements Listener {

    // 功能 2: 處理玩家第一次加入
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = InySMPLobby.getInstance().getConfig();

        // 1. 處理新玩家傳送
        if (!player.hasPlayedBefore() && config.getBoolean("teleport-on-first-join", true)) {
            LobbyUtility.teleportToLobby(player);
            // 由於 Utility 已經發送訊息，這裡省略重複發送。
        }

        // 2. 發放菜單物品 (指南針)
        if (config.getBoolean("menu-item.enabled", false)) {
            Material material = Material.getMaterial(config.getString("menu-item.material", "COMPASS"));
            String name = config.getString("menu-item.name", "§b§l伺服器傳送指南針");
            int slot = config.getInt("menu-item.slot", 0);
            
            if (material != null) {
                ItemStack menuItem = TeleportGUI.createMenuItem(material, name, config.getStringList("menu-item.lore"));
                
                // 僅在快捷列該位置為空時發放，避免覆蓋重要物品
                if (player.getInventory().getItem(slot) == null || player.getInventory().getItem(slot).getType() == Material.AIR) {
                    player.getInventory().setItem(slot, menuItem);
                }
            }
        }
    }

    // 功能 3 附加: 處理指南針點擊 (左鍵/右鍵)
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        FileConfiguration config = InySMPLobby.getInstance().getConfig();
        ItemStack item = event.getItem();

        // 確保是右鍵或左鍵點擊空氣/方塊，且菜單物品功能開啟
        if (config.getBoolean("menu-item.enabled", false) && 
            (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK ||
             action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) &&
            item != null) {

            Material menuMaterial = Material.getMaterial(config.getString("menu-item.material", "COMPASS"));
            String menuName = config.getString("menu-item.name", "§b§l伺服器傳送指南針");
            
            // 檢查拿著的物品是否與配置的菜單物品匹配
            if (item.getType() == menuMaterial && 
                item.getItemMeta() != null && 
                item.getItemMeta().hasDisplayName() && 
                item.getItemMeta().getDisplayName().equals(menuName)) {
                
                event.setCancelled(true); // 取消該物品的默認行為
                TeleportGUI.openGUI(player); // 開啟菜單
            }
        }
    }
}