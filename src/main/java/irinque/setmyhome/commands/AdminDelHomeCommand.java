package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminDelHomeCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public AdminDelHomeCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.admindelhome")) {
            return true;
        }
        if (args.length != 2) {
            plugin.getMessageManager().send(commandSender, "admindelhome-usage");
            return true;
        }

        OfflinePlayer target = PlayerLookup.findKnownPlayer(args[0]);
        if (target == null) {
            plugin.getMessageManager().send(
                    commandSender,
                    "admindelhome-player-not-found",
                    Map.of("player", args[0])
            );
            return true;
        }

        if (plugin.getHomeManager().getHomes(target.getUniqueId()).isEmpty()) {
            plugin.getMessageManager().send(
                    commandSender,
                    "admindelhome-no-homes",
                    Map.of("player", getPlayerName(target, args[0]))
            );
            return true;
        }
        if (!plugin.getHomeManager().deleteHome(target.getUniqueId(), args[1])) {
            plugin.getMessageManager().send(
                    commandSender,
                    "admindelhome-home-not-found",
                    Map.of("player", getPlayerName(target, args[0]), "home", args[1])
            );
            return true;
        }

        plugin.getMessageManager().send(
                commandSender,
                "admindelhome-success",
                Map.of("home", args[1], "player", getPlayerName(target, args[0]))
        );
        return true;
    }

    private String getPlayerName(OfflinePlayer target, String fallback) {
        return target.getName() == null ? fallback : target.getName();
    }
}
