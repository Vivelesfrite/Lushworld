package antoine.vivelesfrites.lushworld.managers;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PortalLinkManager implements Listener {

    private final Lushworld plugin;
    private final Map<String, String> worldLinks = new HashMap<>();
    private final File linkFile;
    private final FileConfiguration linkConfig;

    public PortalLinkManager(Lushworld plugin) {
        this.plugin = plugin;

        this.linkFile = new File(plugin.getDataFolder(), "link.yml");
        if (!linkFile.exists()) {
            try {
                linkFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Impossible de créer le fichier link.yml !");
                e.printStackTrace();
            }
        }

        this.linkConfig = YamlConfiguration.loadConfiguration(linkFile);
        loadWorldLinks();
    }


    private void loadWorldLinks() {
        if (linkConfig.isConfigurationSection("world-links")) {
            for (String overworld : linkConfig.getConfigurationSection("world-links").getKeys(false)) {
                String nether = linkConfig.getString("world-links." + overworld + ".nether");
                String end = linkConfig.getString("world-links." + overworld + ".end");

                if (nether != null) {
                    worldLinks.put(overworld + ":nether", nether);
                    worldLinks.put(nether + ":overworld", overworld);
                }
                if (end != null) {
                    worldLinks.put(overworld + ":end", end);
                    worldLinks.put(end + ":overworld", overworld);
                }
            }
        }
    }

    public void saveWorldLinks() {
        try {
            for (String key : worldLinks.keySet()) {
                String[] parts = key.split(":");
                String overworld = parts[0];
                String type = parts[1];

                if (type.equals("nether")) {
                    linkConfig.set("world-links." + overworld + ".nether", worldLinks.get(key));
                } else if (type.equals("end")) {
                    linkConfig.set("world-links." + overworld + ".end", worldLinks.get(key));
                }
            }
            linkConfig.save(linkFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Impossible de sauvegarder le fichier link.yml !");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        World originWorld = player.getWorld();
        Location originLocation = player.getLocation();

        String targetKey = null;
        if (originWorld.getEnvironment() == World.Environment.NORMAL) {
            targetKey = originWorld.getName() + ":nether";
        } else if (originWorld.getEnvironment() == World.Environment.NETHER) {
            targetKey = originWorld.getName() + ":overworld";
        } else if (originWorld.getEnvironment() == World.Environment.THE_END) {
            targetKey = originWorld.getName() + ":overworld";
        }

        if (targetKey != null && worldLinks.containsKey(targetKey)) {
            String targetWorldName = worldLinks.get(targetKey);

            World targetWorld = Bukkit.getWorld(targetWorldName);
            if (targetWorld == null) {
                player.sendMessage(plugin.getMessage("messages.world-not-found", "%world%", targetWorldName));
                return;
            }

            Location targetLocation = calculateTargetLocation(originLocation, originWorld, targetWorld);
            event.setTo(targetLocation);
            //player.sendMessage(plugin.getMessage("messages.portal-teleport", "%target%", targetWorld.getName()));
        }
    }

    /**
     * Calcule les coordonnées cibles en fonction du ratio d'échelle (Nether ↔ Overworld).
     */
    private Location calculateTargetLocation(Location origin, World originWorld, World targetWorld) {
        double scale = 1.0;

        if (originWorld.getEnvironment() == World.Environment.NORMAL && targetWorld.getEnvironment() == World.Environment.NETHER) {
            scale = 1.0 / 8.0;
        } else if (originWorld.getEnvironment() == World.Environment.NETHER && targetWorld.getEnvironment() == World.Environment.NORMAL) {
            scale = 8.0;
        }

        Location targetLocation = origin.clone();
        targetLocation.setWorld(targetWorld);
        targetLocation.setX(origin.getX() * scale);
        targetLocation.setZ(origin.getZ() * scale);

        targetLocation.setY(origin.getY());
        targetLocation.setPitch(origin.getPitch());
        targetLocation.setYaw(origin.getYaw());

        return targetLocation;
    }

    public void addWorldLink(String overworld, String nether, String end) {
        if (nether != null) {
            worldLinks.put(overworld + ":nether", nether);
            worldLinks.put(nether + ":overworld", overworld);
        }
        if (end != null) {
            worldLinks.put(overworld + ":end", end);
            worldLinks.put(end + ":overworld", overworld);
        }
        saveWorldLinks();
    }
}