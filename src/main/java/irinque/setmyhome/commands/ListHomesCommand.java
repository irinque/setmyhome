package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.model.Home;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListHomesCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public ListHomesCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.listhomes")) {
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            plugin.getMessageManager().send(commandSender, "error-player-only");
            return true;
        }

        List<Home> homes = plugin.getHomeManager().getHomes(player.getUniqueId());
        if (homes.isEmpty()) {
            plugin.getMessageManager().send(player, "listhomes-no-homes");
            return true;
        }

        sendHomeList(player, "listhomes-header", "listhomes-entry", homes, player.getName());
        return true;
    }

    public void sendHomeList(
            CommandSender commandSender,
            String headerKey,
            String entryKey,
            List<Home> homes,
            String playerName
    ) {
        Map<String, String> headerPlaceholders = new HashMap<>();
        headerPlaceholders.put("player", playerName);
        headerPlaceholders.put("count", String.valueOf(homes.size()));
        plugin.getMessageManager().send(commandSender, headerKey, headerPlaceholders);

        for (Home home : homes) {
            Map<String, String> entryPlaceholders = new HashMap<>();
            entryPlaceholders.put("name", home.getName());
            entryPlaceholders.put("world", home.getWorldName());
            entryPlaceholders.put("x", formatCoordinate(home.getX()));
            entryPlaceholders.put("y", formatCoordinate(home.getY()));
            entryPlaceholders.put("z", formatCoordinate(home.getZ()));
            plugin.getMessageManager().send(commandSender, entryKey, entryPlaceholders);
        }
    }

    private String formatCoordinate(double coordinate) {
        return String.format(java.util.Locale.US, "%.1f", coordinate);
    }
}
