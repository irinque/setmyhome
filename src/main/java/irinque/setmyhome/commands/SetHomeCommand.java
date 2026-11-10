package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final Setmyhome plugin;

    public SetHomeCommand(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!CommandSupport.hasPermission(plugin, commandSender, "homeplugin.sethome")) {
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            plugin.getMessageManager().send(commandSender, "error-player-only");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        String homeName = args[0];
        if (homeName.contains(".")) {
            plugin.getMessageManager().send(player, "error-home-name-invalid");
            return true;
        }
        if (!isValidHomeName(homeName)) {
            plugin.getMessageManager().send(player, "error-invalid-arguments");
            return true;
        }

        if (plugin.getHomeManager().getHome(player.getUniqueId(), homeName) != null) {
            plugin.getMessageManager().send(player, "sethome-already-exists");
            return true;
        }

        int maxHomes = getMaxHomes(player);
        if (plugin.getHomeManager().getHomes(player.getUniqueId()).size() >= maxHomes) {
            plugin.getMessageManager().send(player, "sethome-max-homes", Map.of("max", String.valueOf(maxHomes)));
            return true;
        }

        plugin.getHomeManager().createHome(player.getUniqueId(), homeName, player.getLocation());
        plugin.getMessageManager().send(player, "sethome-success", Map.of("name", homeName));
        return true;
    }

    private boolean isValidHomeName(String homeName) {
        return !homeName.isBlank() && homeName.length() <= 32;
    }

    private int getMaxHomes(Player player) {
        ConfigurationSection limits = plugin.getConfig().getConfigurationSection("max-homes");
        if (limits == null) {
            return 15;
        }

        int maxHomes = limits.getInt("default", 15);
        for (String rank : limits.getKeys(false)) {
            if (!rank.equalsIgnoreCase("default") && player.hasPermission("homeplugin.limit." + rank)) {
                maxHomes = Math.max(maxHomes, limits.getInt(rank, maxHomes));
            }
        }
        return Math.max(maxHomes, 0);
    }
}
