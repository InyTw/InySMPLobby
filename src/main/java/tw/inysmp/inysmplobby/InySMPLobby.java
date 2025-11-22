package tw.inysmp.inysmplobby;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;

import tw.inysmp.inysmplobby.commands.LobbyCommand;
import tw.inysmp.inysmplobby.commands.MenuCommand;
import tw.inysmp.inysmplobby.commands.InySMPLobbyCommand;
import tw.inysmp.inysmplobby.listeners.GuiListener;
import tw.inysmp.inysmplobby.listeners.PlayerEventListener;

import java.io.File;
import java.io.IOException;

public final class InySMPLobby extends JavaPlugin {

    private static InySMPLobby instance;

    private String pluginPrefix; 

    private File messageFile;
    private FileConfiguration messageConfig;
    
    private File adminFile;
    private FileConfiguration adminConfig; 
    
    private File waypointsFile; 
    private FileConfiguration waypointsConfig; 


    @Override
    public void onEnable() {
        instance = this;

        sendConsole("&6[&bIny&aSMP&eLobby&6] &2&lThe Plugin is loading now ... ");

        this.saveDefaultConfig();
        loadMessageFile("messages.yml"); 
        loadAdminFile("admin.yml");
        loadWaypointsFile("waypoints.yml"); // <-- 載入 Waypoints

        this.getCommand("lobby").setExecutor(new LobbyCommand());
        this.getCommand("menu").setExecutor(new MenuCommand());
        this.getCommand("smplobby").setExecutor(new InySMPLobbyCommand());

        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);


        sendConsole("&6[&bIny&aSMP&eLobby&6] &ev" + getDescription().getVersion() + " &a已啟動！");
    }

    @Override
    public void onDisable() {
        sendConsole("&6[&bIny&aSMP&eLobby&6] &ev" + getDescription().getVersion() + " &aClose！");
    }


    public static InySMPLobby getInstance() {
        return instance;
    }

    // ----------------------------------------------------
    // 配置檔載入方法
    // ----------------------------------------------------

    public void loadMessageFile(String fileName) {
        if (messageFile == null) {
            messageFile = new File(getDataFolder(), fileName);
        }
        if (!messageFile.exists()) {
            this.saveResource(fileName, false);
        }
        
        messageConfig = YamlConfiguration.loadConfiguration(messageFile);

        String rawPrefix = messageConfig.getString("prefix", "&6[&bIny&aSMP&eLobby&6] &r");
        this.pluginPrefix = ChatColor.translateAlternateColorCodes('&', rawPrefix);
    }
    
    public void loadAdminFile(String fileName) {
        if (adminFile == null) {
            adminFile = new File(getDataFolder(), fileName);
        }
        if (!adminFile.exists()) {
            this.saveResource(fileName, false);
        }
        adminConfig = YamlConfiguration.loadConfiguration(adminFile);
    }
    
    public void loadWaypointsFile(String fileName) {
        if (waypointsFile == null) {
            waypointsFile = new File(getDataFolder(), fileName);
        }
        if (!waypointsFile.exists()) {
            this.saveResource(fileName, false);
        }
        waypointsConfig = YamlConfiguration.loadConfiguration(waypointsFile);
    }
    
    public void saveWaypointsFile() {
        if (waypointsFile == null || waypointsConfig == null) return;
        try {
            waypointsConfig.save(waypointsFile);
        } catch (IOException e) {
            getLogger().severe("無法儲存 waypoints.yml: " + e.getMessage());
        }
    }

    // ----------------------------------------------------
    // 獲取配置方法
    // ----------------------------------------------------
    
    public FileConfiguration getAdminConfig() {
        return adminConfig;
    }
    
    public FileConfiguration getWaypointsConfig() {
        return waypointsConfig;
    }

    public String getMessage(String path) {
        if (!messageConfig.contains(path)) {
            return ChatColor.RED + "[InySMPLobby Error] 消息路徑錯誤: " + path;
        }
        
        if (messageConfig.isList(path)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < messageConfig.getStringList(path).size(); i++) {
                String line = messageConfig.getStringList(path).get(i);
                sb.append(processColors(line));
                if (i < messageConfig.getStringList(path).size() - 1) {
                    sb.append("\n");
                }
            }
            return sb.toString();
        }

        String message = messageConfig.getString(path);
        return processColors(message);
    }

    private String processColors(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replace("{prefix}", this.pluginPrefix);
        return message;
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }
    
    // 輔助 reload
    public void handleReload() {
        this.reloadConfig();
        this.loadMessageFile("messages.yml"); 
        this.loadAdminFile("admin.yml");
        this.loadWaypointsFile("waypoints.yml");
    }
}