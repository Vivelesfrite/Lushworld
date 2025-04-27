package antoine.vivelesfrites.lushworld.managers;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PositionManager {

    private final Lushworld plugin;
    private File file;
    private FileConfiguration config;

    public PositionManager(Lushworld plugin) {
        this.plugin = plugin;
        createFile();
    }

    private void createFile() {
        file = new File(plugin.getDataFolder(), "positions.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayerLocation(UUID uuid, String worldName, Location location) {
        String path = uuid.toString() + "." + worldName;
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
        saveFile();
    }

    public Location getSavedLocation(UUID uuid, World world) {
        String path = uuid.toString() + "." + world.getName();
        if (!config.contains(path)) return null;

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
