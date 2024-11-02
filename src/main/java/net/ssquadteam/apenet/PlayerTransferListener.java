package net.ssquadteam.apenet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class PlayerTransferListener implements Listener {
    private final ApeNet plugin;

    public PlayerTransferListener(ApeNet plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.isSecureTransferEnabled() || plugin.isFrontendServer()) {
            return;
        }

        Objective tokenObjective = event.getPlayer().getScoreboard().getObjective("apenetToken");
        if (tokenObjective != null) {
            String token = null;
            for (String entry : tokenObjective.getScoreboard().getEntries()) {
                Score score = tokenObjective.getScore(entry);
                if (score.isScoreSet()) {
                    token = entry;
                    break;
                }
            }

            if (token != null && plugin.getTokenManager().validateToken(token)) {
                // Token is valid, allow the player to join
                plugin.getLogger().info("Player " + event.getPlayer().getName() + " joined with a valid token.");
            } else {
                event.getPlayer().kickPlayer("Invalid transfer token!");
            }
            // Clean up the scoreboard
            event.getPlayer().getScoreboard().clearSlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        } else {
            // If no token is present, kick the player (as this is a backend server)
            event.getPlayer().kickPlayer("Direct access to this server is not allowed!");
        }
    }
}