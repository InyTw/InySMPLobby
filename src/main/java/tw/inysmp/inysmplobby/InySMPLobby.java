package tw.inysmp.inysmplobby;

import org.bukkit.plugin.java.JavaPlugin;
import tw.inysmp.inysmplobby.commands.LobbyCommand;
import tw.inysmp.inysmplobby.commands.MenuCommand;
import tw.inysmp.inysmplobby.listeners.GuiListener;
import tw.inysmp.inysmplobby.listeners.PlayerEventListener;

public final class InySMPLobby extends JavaPlugin {

    private static InySMPLobby instance;

    @Override
    public void onEnable() {
        instance = this;
        
        // 載入配置檔和訊息檔
        this.saveDefaultConfig(); // config.yml
        // 載入 messages.yml (通常需要額外類別處理，這裡假設簡單實現)
        // 例如: CustomConfigManager.load(this, "messages.yml"); 
        
        // 註冊指令 (功能 1, 3)
        this.getCommand("lobby").setExecutor(new LobbyCommand());
        this.getCommand("teleportmenu").setExecutor(new MenuCommand());

        // 註冊事件 (功能 2, 3 - GUI, 指南針)
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);

        getLogger().info("InySMPLobby v" + getDescription().getVersion() + " 已啟動！");
    }

    @Override
    public void onDisable() {
        getLogger().info("InySMPLobby 已關閉！");
    }

    public static InySMPLobby getInstance() {
        return instance;
    }

    // 建議在此處添加訊息讀取方法，但為了簡潔，暫時使用硬編碼訊息。
}