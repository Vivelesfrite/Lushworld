package antoine.vivelesfrites.lushworld.managers;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WorldManager {

    private final Lushworld plugin;
    private final File worldsFile;
    private final FileConfiguration worldsConfig;

    public WorldManager(Lushworld plugin) {
        this.plugin = plugin;

        this.worldsFile = new File(plugin.getDataFolder(), "worlds.yml");
        if (!worldsFile.exists()) {
            try {
                worldsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Impossible de créer le fichier worlds.yml !");
                e.printStackTrace();
            }
        }

        this.worldsConfig = YamlConfiguration.loadConfiguration(worldsFile);
    }

    public void saveCustomWorld(String worldName, World.Environment environment) {
        List<String> customWorlds = worldsConfig.getStringList("custom-worlds");
        if (!customWorlds.contains(worldName)) {
            customWorlds.add(worldName);
            worldsConfig.set("custom-worlds", customWorlds);
            worldsConfig.set("world-environments." + worldName, environment.name());
            saveWorldsConfig();
            plugin.getLogger().info("Le monde '" + worldName + "' a été enregistré avec l'environnement : " + environment.name());
        }
    }

    public void removeCustomWorld(String worldName) {
        List<String> customWorlds = worldsConfig.getStringList("custom-worlds");
        if (customWorlds.contains(worldName)) {
            customWorlds.remove(worldName);
            worldsConfig.set("custom-worlds", customWorlds);
            worldsConfig.set("world-environments." + worldName, null);
            saveWorldsConfig();
            plugin.getLogger().info("Le monde '" + worldName + "' a été supprimé de worlds.yml.");
        }
    }

    public void loadCustomWorlds() {
        List<String> customWorlds = worldsConfig.getStringList("custom-worlds");
        for (String worldName : customWorlds) {
            plugin.getLogger().info("Chargement du monde personnalisé : " + worldName);
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                String environmentName = worldsConfig.getString("world-environments." + worldName, "NORMAL");
                World.Environment environment = World.Environment.valueOf(environmentName);

                WorldCreator creator = new WorldCreator(worldName);
                creator.environment(environment);
                creator.type(WorldType.NORMAL);
                creator.createWorld();
                plugin.getLogger().info("Le monde '" + worldName + "' a été chargé avec l'environnement : " + environment.name());
            } else {
                plugin.getLogger().info("Le monde '" + worldName + "' est déjà chargé.");
            }
        }
    }

    private void saveWorldsConfig() {
        try {
            worldsConfig.save(worldsFile);
            plugin.getLogger().info("Le fichier worlds.yml a été sauvegardé.");
        } catch (IOException e) {
            plugin.getLogger().severe("Impossible de sauvegarder le fichier worlds.yml !");
            e.printStackTrace();
        }
    }

    public List<String> getCustomWorlds() {
        return worldsConfig.getStringList("custom-worlds");
    }
}