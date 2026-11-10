package irinque.setmyhome.handlers;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.model.Home;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class HomeTeleportManager implements Listener {
    private final Setmyhome plugin;
    private final Map<UUID, PendingTeleport> pendingTeleports = new HashMap<>();

    public HomeTeleportManager(Setmyhome plugin) {
        this.plugin = plugin;
    }

    public void teleport(Player player, Home home) {
        PendingTeleport pendingTeleport = pendingTeleports.get(player.getUniqueId());
        if (pendingTeleport != null) {
            long secondsLeft = Math.max(1, (pendingTeleport.endsAt - System.currentTimeMillis() + 999) / 1000);
            plugin.getMessageManager().send(
                    player,
                    "home-teleport-cooldown",
                    Map.of("seconds", String.valueOf(secondsLeft))
            );
            return;
        }

        Location destination = home.getLocation();
        if (destination == null) {
            plugin.getMessageManager().send(player, "error-home-world-unavailable");
            return;
        }

        int cooldown = Math.max(0, plugin.getConfig().getInt("cooldown", 0));
        if (cooldown == 0) {
            finishTeleport(player, home, destination);
            return;
        }

        PendingTeleport request = new PendingTeleport(
                player.getLocation().clone(),
                System.currentTimeMillis() + cooldown * 1000L
        );
        request.task = plugin.getServer().getScheduler().runTaskLater(
                plugin,
                () -> {
                    if (pendingTeleports.remove(player.getUniqueId(), request)) {
                        finishTeleport(player, home, destination);
                    }
                },
                cooldown * 20L
        );
        pendingTeleports.put(player.getUniqueId(), request);
        plugin.getMessageManager().send(
                player,
                "home-teleport-cooldown",
                Map.of("seconds", String.valueOf(cooldown))
        );
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.getConfig().getBoolean("cancel-on-move", false)) {
            return;
        }

        PendingTeleport request = pendingTeleports.get(event.getPlayer().getUniqueId());
        if (request == null || event.getTo() == null || !hasChangedBlock(request.startLocation, event.getTo())) {
            return;
        }

        cancelTeleport(event.getPlayer().getUniqueId(), event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cancelTeleport(event.getPlayer().getUniqueId(), event.getPlayer(), false);
    }

    public void cancelAll() {
        for (PendingTeleport request : pendingTeleports.values()) {
            request.task.cancel();
        }
        pendingTeleports.clear();
    }

    private boolean hasChangedBlock(Location startLocation, Location currentLocation) {
        return !startLocation.getWorld().getUID().equals(currentLocation.getWorld().getUID())
                || startLocation.getBlockX() != currentLocation.getBlockX()
                || startLocation.getBlockY() != currentLocation.getBlockY()
                || startLocation.getBlockZ() != currentLocation.getBlockZ();
    }

    private void cancelTeleport(UUID playerId, Player player, boolean sendMessage) {
        PendingTeleport request = pendingTeleports.remove(playerId);
        if (request == null) {
            return;
        }

        request.task.cancel();
        if (sendMessage) {
            plugin.getMessageManager().send(player, "home-teleport-canceled");
        }
    }

    private void finishTeleport(Player player, Home home, Location destination) {
        if (!player.isOnline() || !player.teleport(destination)) {
            return;
        }

        if (plugin.getConfig().getBoolean("play-sound", true)) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
        plugin.getMessageManager().send(player, "home-teleport-success", Map.of("name", home.getName()));
    }

    private static class PendingTeleport {
        private final Location startLocation;
        private final long endsAt;
        private BukkitTask task;

        private PendingTeleport(Location startLocation, long endsAt) {
            this.startLocation = startLocation;
            this.endsAt = endsAt;
        }
    }
}
