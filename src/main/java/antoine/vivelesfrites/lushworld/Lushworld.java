package antoine.vivelesfrites.lushworld;

import antoine.vivelesfrites.lushworld.commands.*;
import antoine.vivelesfrites.lushworld.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Lushworld extends JavaPlugin {

    private WorldManager worldManager;
    private PortalLinkManager portalLinkManager;
    private PositionManager positionManager;
    @Override
    public void onEnable() {
        saveDefaultConfig();

        worldManager = new WorldManager(this);
        portalLinkManager = new PortalLinkManager(this);
        positionManager = new PositionManager(this);

        worldManager.loadCustomWorlds();

        getCommand("createmonde").setExecutor(new CreateWorldCommand(this));
        getCommand("tpmonde").setExecutor(new TeleportWorldCommand(this));
        getCommand("deletemonde").setExecutor(new DeleteWorldCommand(this));
        getCommand("regenmonde").setExecutor(new RegenWorldCommand(this));
        getCommand("linkmonde").setExecutor(new LinkWorldCommand(this));

        getCommand("tpmonde").setTabCompleter(new TeleportWorldTabCompleter(this));

        getServer().getPluginManager().registerEvents(portalLinkManager, this);

        getLogger().info("Le plugin Lushworld est activé !");
    }

    @Override
    public void onDisable() {
        portalLinkManager.saveWorldLinks();

        getLogger().info("Le plugin Lushworld est désactivé.");
    }


    public WorldManager getWorldManager() {
        return worldManager;
    }


    public PortalLinkManager getPortalLinkManager() {
        return portalLinkManager;
    }


    public String getMessage(String path, String... placeholders) {
        String message = getConfig().getString(path, "Message non configuré : " + path);
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }
    public PositionManager getPositionManager() {
        return positionManager;
    }
}