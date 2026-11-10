package irinque.setmyhome.model;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Home {
    private final String name;
    private final UUID worldId;
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Home(String name, UUID worldId, String worldName, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.worldId = worldId;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location getLocation() {
        org.bukkit.World world = worldId == null ? null : Bukkit.getWorld(worldId);
        if (world == null) {
            world = Bukkit.getWorld(worldName);
        }
        if (world == null) {
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }
}
