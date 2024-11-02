package net.ssquadteam.apenet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TransferCommand implements CommandExecutor {

    private final ApeNet plugin;

    public TransferCommand(ApeNet plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <reload|send|servername>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("apenet.reload")) {
                sender.sendMessage("You don't have permission to reload the plugin.");
                return true;
            }
            plugin.reload();
            sender.sendMessage("ApeNet configuration reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("send")) {
            if (!sender.hasPermission("apenet.send")) {
                sender.sendMessage("You don't have permission to send other players.");
                return true;
            }
            if (args.length != 3) {
                sender.sendMessage("Usage: /" + label + " send <player> <servername>");
                return true;
            }
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage("Player not found or not online.");
                return true;
            }
            transferPlayer(targetPlayer, args[2], sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        transferPlayer(player, args[0], sender);
        return true;
    }

    private void transferPlayer(Player player, String serverName, CommandSender sender) {
        String host = plugin.getConfig().getString("servers." + serverName + ".host");
        int port = plugin.getConfig().getInt("servers." + serverName + ".port", 25565);

        if (host == null) {
            sender.sendMessage("Server not found in configuration.");
            return;
        }

        try {
            if (plugin.isSecureTransferEnabled() && plugin.isFrontendServer()) {
                String token = plugin.getTokenManager().getToken();
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                player.getScoreboard().registerNewObjective("apenetToken", "dummy", "ApeNet Token");
                player.getScoreboard().getObjective("apenetToken").getScore(token).setScore(1);
            }

            player.transfer(host, port);
            sender.sendMessage("Transferring " + player.getName() + " to " + serverName + "...");
            if (sender != player) {
                player.sendMessage("You are being transferred to " + serverName + "...");
            }
        } catch (IllegalStateException e) {
            sender.sendMessage("Unable to transfer " + player.getName() + " at this time. Please try again later.");
        }
    }
}