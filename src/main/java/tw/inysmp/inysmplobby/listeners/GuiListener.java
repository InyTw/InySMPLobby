package tw.inysmp.inysmplobby.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.inysmp.inysmplobby.InySMPLobby;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String guiTitle = InySMPLobby.getInstance().getConfig().getString("gui-title", "伺服器選擇菜單");
        
        // 檢查是否為我們的 GUI
        if (event.getView().getTitle().equals(guiTitle)) {
            event.setCancelled(true); // 防止玩家拿走物品
            
            if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.CHEST) return;

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            
            if (clickedItem == null || clickedItem.getItemMeta() == null) return;

            ItemMeta meta = clickedItem.getItemMeta();
            String clickedName = meta.getDisplayName();

            ConfigurationSection options = InySMPLobby.getInstance().getConfig().getConfigurationSection("teleport-options");
            
            if (options != null) {
                for (String key : options.getKeys(false)) {
                    ConfigurationSection itemSection = options.getConfigurationSection(key);
                    
                    // 檢查顯示名稱是否匹配
                    if (clickedName.equals(itemSection.getString("name"))) {
                        String command = itemSection.getString("command");
                        if (command != null && !command.isEmpty()) {
                            player.closeInventory(); // 關閉 GUI
                            // 執行指令 (通常是 BungeeCord 的 /server <name> 或其他)
                            Bukkit.dispatchCommand(player, command);
                            return;
                        }
                    }
                }
            }
        }
    }
}