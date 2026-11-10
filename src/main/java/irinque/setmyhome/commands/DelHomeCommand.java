package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public DelHomeCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.delhome")) {
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            plugin.getMessageManager().send(commandSender, "error-player-only");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        if (!plugin.getHomeManager().deleteHome(player.getUniqueId(), args[0])) {
            plugin.getMessageManager().send(player, "error-home-not-found");
            return true;
        }

        plugin.getMessageManager().send(player, "delhome-success", Map.of("name", args[0]));
        return true;
    }
}
