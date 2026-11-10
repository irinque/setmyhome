package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.model.Home;
import java.util.List;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminListHomesCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public AdminListHomesCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.adminlisthomes")) {
            return true;
        }
        if (args.length != 1) {
            plugin.getMessageManager().send(commandSender, "adminlisthomes-usage");
            return true;
        }

        OfflinePlayer target = PlayerLookup.findKnownPlayer(args[0]);
        if (target == null) {
            plugin.getMessageManager().send(
                    commandSender,
                    "adminlisthomes-player-not-found",
                    Map.of("player", args[0])
            );
            return true;
        }

        List<Home> homes = plugin.getHomeManager().getHomes(target.getUniqueId());
        if (homes.isEmpty()) {
            plugin.getMessageManager().send(
                    commandSender,
                    "adminlisthomes-no-homes",
                    Map.of("player", getPlayerName(target, args[0]))
            );
            return true;
        }

        new ListHomesCommand(plugin).sendHomeList(
                commandSender,
                "adminlisthomes-header",
                "adminlisthomes-entry",
                homes,
                getPlayerName(target, args[0])
        );
        return true;
    }

    private String getPlayerName(OfflinePlayer target, String fallback) {
        return target.getName() == null ? fallback : target.getName();
    }
}
