package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.model.Home;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public HomeCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.home")) {
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            plugin.getMessageManager().send(commandSender, "error-player-only");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        Home home = plugin.getHomeManager().getHome(player.getUniqueId(), args[0]);
        if (home == null) {
            plugin.getMessageManager().send(player, "error-home-not-found");
            return true;
        }

        plugin.getHomeTeleportManager().teleport(player, home);
        return true;
    }
}
