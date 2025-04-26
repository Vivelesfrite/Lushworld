package antoine.vivelesfrites.lushworld.commands;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateWorldCommand implements CommandExecutor {

    private final Lushworld plugin;

    public CreateWorldCommand(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lushworld.createmonde")) {
            sender.sendMessage(plugin.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessage("messages.usage-createmonde"));
            return false;
        }

        String worldName = args[0];
        Environment environment = Environment.NORMAL;
        WorldType worldType = WorldType.NORMAL;

        if (args.length >= 2) {
            try {
                environment = Environment.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(plugin.getMessage("messages.invalid-environment", "%env%", args[1]));
                return true;
            }
        }

        if (args.length >= 3) {
            try {
                worldType = WorldType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(plugin.getMessage("messages.invalid-worldtype", "%type%", args[2]));
                return true;
            }
        }

        if (Bukkit.getWorld(worldName) != null) {
            sender.sendMessage(plugin.getMessage("messages.world-already-exists", "%world%", worldName));
            return true;
        }

        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(environment);
        creator.type(worldType);
        World world = creator.createWorld();

        if (world != null) {
            plugin.getWorldManager().saveCustomWorld(worldName, environment);
            sender.sendMessage(plugin.getMessage("messages.world-created", "%world%", worldName));
        } else {
            sender.sendMessage(plugin.getMessage("messages.world-creation-failed", "%world%", worldName));
        }

        return true;
    }
}