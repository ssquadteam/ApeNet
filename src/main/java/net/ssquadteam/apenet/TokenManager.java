package net.ssquadteam.apenet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TokenManager {
    private final ApeNet plugin;
    private final File tokenFile;
    private FileConfiguration tokenConfig;

    public TokenManager(ApeNet plugin) {
        this.plugin = plugin;
        this.tokenFile = new File(plugin.getDataFolder(), "token.yml");
        loadTokenConfig();
    }

    private void loadTokenConfig() {
        if (!tokenFile.exists()) {
            plugin.saveResource("token.yml", false);
        }
        tokenConfig = YamlConfiguration.loadConfiguration(tokenFile);
        if (!tokenConfig.contains("token")) {
            tokenConfig.set("token", UUID.randomUUID().toString());
            saveTokenConfig();
        }
    }

    private void saveTokenConfig() {
        try {
            tokenConfig.save(tokenFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save token.yml!");
        }
    }

    public String getToken() {
        return tokenConfig.getString("token");
    }

    public boolean validateToken(String token) {
        return getToken().equals(token);
    }
}