package net.ssquadteam.apenet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ApeNet extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        registerCommands();
        getLogger().info("ApeNet has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ApeNet has been disabled!");
    }

    public void loadConfig() {
        reloadConfig();
        config = getConfig();
    }

    private void registerCommands() {
        String commandName = config.getString("command", "apenet");
        TransferCommand transferCommand = new TransferCommand(this);
        ApeNetTabCompleter tabCompleter = new ApeNetTabCompleter(this);
        
        getCommand(commandName).setExecutor(transferCommand);
        getCommand(commandName).setTabCompleter(tabCompleter);
    }

    public void reload() {
        loadConfig();
        registerCommands();
        getLogger().info("ApeNet configuration reloaded!");
    }
}