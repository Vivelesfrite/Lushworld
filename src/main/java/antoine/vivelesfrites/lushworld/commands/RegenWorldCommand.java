package antoine.vivelesfrites.lushworld.commands;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Random;

public class RegenWorldCommand implements CommandExecutor {

    private final Lushworld plugin;

    public RegenWorldCommand(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lushworld.regenmonde")) {
            sender.sendMessage(plugin.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessage("messages.usage-regenmonde"));
            return false;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage(plugin.getMessage("messages.world-not-found", "%world%", worldName));
            return true;
        }

        World.Environment environment = world.getEnvironment();

        boolean success = Bukkit.unloadWorld(world, false);
        if (!success) {
            sender.sendMessage(plugin.getMessage("messages.world-unload-error", "%world%", worldName));
            return true;
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (!deleteFolder(worldFolder)) {
            sender.sendMessage(plugin.getMessage("messages.world-delete-error", "%world%", worldName));
            return true;
        }

        WorldCreator creator = new WorldCreator(worldName);
        creator.environment(environment);

        if (args.length >= 2) {
            try {
                long seed = Long.parseLong(args[1]);
                creator.seed(seed);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessage("messages.invalid-seed", "%seed%", args[1]));
                return true;
            }
        } else {
            long randomSeed = new Random().nextLong();
            creator.seed(randomSeed);
            sender.sendMessage(plugin.getMessage("messages.random-seed-used", "%seed%", String.valueOf(randomSeed)));
        }

        World newWorld = creator.createWorld();
        if (newWorld != null) {
            sender.sendMessage(plugin.getMessage("messages.world-regenerated", "%world%", worldName));
        } else {
            sender.sendMessage(plugin.getMessage("messages.world-regeneration-error", "%world%", worldName));
        }

        return true;
    }

    private boolean deleteFolder(File folder) {
        if (!folder.exists()) return false;

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                }
            }
        }
        return folder.delete();
    }
}