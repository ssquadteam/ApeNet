package net.ssquadteam.apenet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ApeNet extends JavaPlugin {
    private FileConfiguration config;
    private TokenManager tokenManager;
    private boolean secureTransferEnabled;
    private boolean isFrontendServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        if (secureTransferEnabled) {
            tokenManager = new TokenManager(this);
            if (!isFrontendServer) {
                getServer().getPluginManager().registerEvents(new PlayerTransferListener(this), this);
            }
        }
        registerCommands();
        logStatus();
    }

    @Override
    public void onDisable() {
        getLogger().info("ApeNet has been disabled!");
    }

    public void loadConfig() {
        reloadConfig();
        config = getConfig();
        secureTransferEnabled = config.getBoolean("secure_transfer", false);
        isFrontendServer = secureTransferEnabled && config.getBoolean("frontend_server", false);
    }

    private void registerCommands() {
        String commandName = config.getString("command", "apenet");
        TransferCommand transferCommand = new TransferCommand(this);
        ApeNetTabCompleter tabCompleter = new ApeNetTabCompleter(this);
        
        getCommand(commandName).setExecutor(transferCommand);
        getCommand(commandName).setTabCompleter(tabCompleter);
    }

    private void logStatus() {
        if (secureTransferEnabled) {
            getLogger().info("ApeNet enabled with secure transfer.");
            getLogger().info("This is a " + (isFrontendServer ? "frontend" : "backend") + " server.");
        } else {
            getLogger().info("ApeNet enabled without secure transfer.");
            getLogger().info("Frontend/backend distinction is not applicable in this mode.");
        }
    }

    public void reload() {
        loadConfig();
        registerCommands();
        getLogger().info("ApeNet configuration reloaded!");
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public boolean isSecureTransferEnabled() {
        return secureTransferEnabled;
    }

    public boolean isFrontendServer() {
        return isFrontendServer;
    }
}