package tw.inysmp.inysmplobby.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.inysmp.inysmplobby.InySMPLobby;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportGUI {

    /** 創建一個 ItemStack 項目 */
    public static ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // 設置顯示名稱和描述，並替換顏色代碼
        meta.setDisplayName(name.replace('&', '§'));
        if (lore != null) {
            meta.setLore(lore.stream().map(s -> s.replace('&', '§')).collect(Collectors.toList()));
        }
        
        item.setItemMeta(meta);
        return item;
    }

    /** 打開 GUI */
    public static void openGUI(Player player) {
        InySMPLobby plugin = InySMPLobby.getInstance();
        String title = plugin.getConfig().getString("gui-title", "§8伺服器選擇菜單");
        
        // 建立 3 行的 GUI
        Inventory gui = Bukkit.createInventory(player, 27, title);
        
        ConfigurationSection options = plugin.getConfig().getConfigurationSection("teleport-options");
        
        if (options != null) {
            for (String key : options.getKeys(false)) {
                ConfigurationSection itemSection = options.getConfigurationSection(key);
                
                Material material = Material.getMaterial(itemSection.getString("item", "STONE"));
                String name = itemSection.getString("name", "未定義項目");
                List<String> lore = itemSection.getStringList("lore");
                int slot = itemSection.getInt("slot", -1);
                
                if (slot != -1 && material != null) {
                    ItemStack item = createMenuItem(material, name, lore);
                    gui.setItem(slot, item);
                }
            }
        }
        
        player.openInventory(gui);
        // player.sendMessage(plugin.getMessage("gui-opened"));
    }
}