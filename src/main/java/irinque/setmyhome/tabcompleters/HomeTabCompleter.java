package irinque.setmyhome.tabcompleters;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.commands.PlayerLookup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class HomeTabCompleter implements TabCompleter {
    private final Setmyhome plugin;

    public HomeTabCompleter(Setmyhome plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 0) {
            return suggestions;
        }

        if (args.length == 1) {
            if (command.getName().equalsIgnoreCase("home") || command.getName().equalsIgnoreCase("delhome")) {
                addPlayerHomes(commandSender, suggestions);
            } else if (command.getName().equalsIgnoreCase("adminlisthomes")
                    || command.getName().equalsIgnoreCase("admindelhome")) {
                addOnlinePlayers(suggestions);
            }
        } else if (args.length == 2 && command.getName().equalsIgnoreCase("admindelhome")) {
            OfflinePlayer target = PlayerLookup.findKnownPlayer(args[0]);
            if (target != null) {
                plugin.getHomeManager().getHomes(target.getUniqueId()).forEach(home -> suggestions.add(home.getName()));
            }
            Collections.sort(suggestions, String.CASE_INSENSITIVE_ORDER);
            return StringUtil.copyPartialMatches(args[1], suggestions, new ArrayList<>());
        }

        Collections.sort(suggestions, String.CASE_INSENSITIVE_ORDER);
        return StringUtil.copyPartialMatches(args[args.length - 1], suggestions, new ArrayList<>());
    }

    private void addPlayerHomes(CommandSender commandSender, List<String> suggestions) {
        if (!(commandSender instanceof Player player)) {
            return;
        }
        plugin.getHomeManager().getHomes(player.getUniqueId()).forEach(home -> suggestions.add(home.getName()));
    }

    private void addOnlinePlayers(List<String> suggestions) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }
    }
}
