package irinque.setmyhome.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class PlayerLookup {
    private PlayerLookup() {
    }

    public static OfflinePlayer findKnownPlayer(String playerName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                return player;
            }
        }

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.hasPlayedBefore()
                    && player.getName() != null
                    && player.getName().equalsIgnoreCase(playerName)) {
                return player;
            }
        }

        return null;
    }
}
