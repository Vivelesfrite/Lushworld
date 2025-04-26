package antoine.vivelesfrites.lushworld.commands;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LinkWorldCommand implements CommandExecutor {

    private final Lushworld plugin;

    public LinkWorldCommand(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lushworld.linkmonde")) {
            sender.sendMessage(plugin.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("messages.usage-linkmonde"));
            return false;
        }

        String worldName = args[0];
        String type = args[1].toLowerCase();

        if (!type.equals("nether") && !type.equals("end")) {
            sender.sendMessage(plugin.getMessage("messages.invalid-type", "%type%", type));
            return true;
        }

        World sourceWorld = Bukkit.getWorld(worldName);
        if (sourceWorld == null) {
            sender.sendMessage(plugin.getMessage("messages.world-not-found", "%world%", worldName));
            return true;
        }

        String targetWorldName;
        if (type.equals("nether")) {
            targetWorldName = worldName + "_nether";
        } else {
            targetWorldName = worldName + "_end";
        }

        World targetWorld = Bukkit.getWorld(targetWorldName);
        if (targetWorld == null) {
            sender.sendMessage(plugin.getMessage("messages.target-world-not-found", "%world%", targetWorldName));
            return true;
        }

        plugin.getPortalLinkManager().addWorldLink(worldName, type.equals("nether") ? targetWorldName : null, type.equals("end") ? targetWorldName : null);
        sender.sendMessage(plugin.getMessage("messages.link-created", "%source%", worldName, "%target%", targetWorldName));
        return true;
    }
}