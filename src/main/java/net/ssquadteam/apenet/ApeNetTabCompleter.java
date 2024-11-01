package net.ssquadteam.apenet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApeNetTabCompleter implements TabCompleter {

    private final ApeNet plugin;

    public ApeNetTabCompleter(ApeNet plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("send");
            completions.addAll(getServerNames());
            return filterCompletions(completions, args[0]);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("send")) {
            return filterCompletions(getOnlinePlayerNames(), args[1]);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("send")) {
            return filterCompletions(getServerNames(), args[2]);
        }

        return completions;
    }

    private List<String> getServerNames() {
        Set<String> serverNames = plugin.getConfig().getConfigurationSection("servers").getKeys(false);
        return new ArrayList<>(serverNames);
    }

    private List<String> getOnlinePlayerNames() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    private List<String> filterCompletions(List<String> completions, String prefix) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}