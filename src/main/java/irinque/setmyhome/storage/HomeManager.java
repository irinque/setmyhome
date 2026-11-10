package irinque.setmyhome.storage;

import irinque.setmyhome.Setmyhome;
import irinque.setmyhome.model.Home;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class HomeManager {
    private final Setmyhome plugin;
    private final File homesFile;
    private final YamlConfiguration homesData;

    public HomeManager(Setmyhome plugin) {
        this.plugin = plugin;
        homesFile = new File(plugin.getDataFolder(), "homes.yml");
        homesData = YamlConfiguration.loadConfiguration(homesFile);
    }

    public Home getHome(UUID playerId, String homeName) {
        ConfigurationSection playerHomes = getPlayerHomes(playerId);
        if (playerHomes == null) {
            return null;
        }

        for (String savedName : playerHomes.getKeys(false)) {
            if (savedName.equalsIgnoreCase(homeName)) {
                return loadHome(savedName, playerHomes.getConfigurationSection(savedName));
            }
        }

        return null;
    }

    public List<Home> getHomes(UUID playerId) {
        List<Home> homes = new ArrayList<>();
        ConfigurationSection playerHomes = getPlayerHomes(playerId);
        if (playerHomes == null) {
            return homes;
        }

        for (String homeName : playerHomes.getKeys(false)) {
            Home home = loadHome(homeName, playerHomes.getConfigurationSection(homeName));
            if (home != null) {
                homes.add(home);
            }
        }

        homes.sort(Comparator.comparing(Home::getName, String.CASE_INSENSITIVE_ORDER));
        return homes;
    }

    public boolean createHome(UUID playerId, String homeName, Location location) {
        if (getHome(playerId, homeName) != null) {
            return false;
        }

        String path = getHomePath(playerId, homeName);
        homesData.set(path + ".world-id", location.getWorld().getUID().toString());
        homesData.set(path + ".world", location.getWorld().getName());
        homesData.set(path + ".x", location.getX());
        homesData.set(path + ".y", location.getY());
        homesData.set(path + ".z", location.getZ());
        homesData.set(path + ".yaw", location.getYaw());
        homesData.set(path + ".pitch", location.getPitch());
        save();
        return true;
    }

    public boolean deleteHome(UUID playerId, String homeName) {
        Home home = getHome(playerId, homeName);
        if (home == null) {
            return false;
        }

        homesData.set(getHomePath(playerId, home.getName()), null);
        ConfigurationSection playerHomes = getPlayerHomes(playerId);
        if (playerHomes != null && playerHomes.getKeys(false).isEmpty()) {
            homesData.set("homes." + playerId, null);
        }
        save();
        return true;
    }

    private ConfigurationSection getPlayerHomes(UUID playerId) {
        return homesData.getConfigurationSection("homes." + playerId);
    }

    private String getHomePath(UUID playerId, String homeName) {
        return "homes." + playerId + "." + homeName;
    }

    private Home loadHome(String homeName, ConfigurationSection homeData) {
        if (homeData == null || !homeData.contains("world")) {
            return null;
        }

        String worldName = homeData.getString("world");
        String worldIdValue = homeData.getString("world-id", "");
        UUID worldId = null;
        if (worldIdValue != null && !worldIdValue.isBlank()) {
            try {
                worldId = UUID.fromString(worldIdValue);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return new Home(
                homeName,
                worldId,
                worldName,
                homeData.getDouble("x"),
                homeData.getDouble("y"),
                homeData.getDouble("z"),
                (float) homeData.getDouble("yaw"),
                (float) homeData.getDouble("pitch")
        );
    }

    private void save() {
        try {
            homesData.save(homesFile);
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save homes: " + exception.getMessage());
        }
    }
}
