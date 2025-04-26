package antoine.vivelesfrites.lushworld.commands;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class DeleteWorldCommand implements CommandExecutor {

    private final Lushworld plugin;

    public DeleteWorldCommand(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lushworld.command.deletemonde")) {
            sender.sendMessage(plugin.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessage("messages.usage-deletemonde"));
            return false;
        }

        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            sender.sendMessage(plugin.getMessage("messages.world-not-found", "%world%", worldName));
            return true;
        }

        boolean success = Bukkit.unloadWorld(world, false);
        if (!success) {
            sender.sendMessage(plugin.getMessage("messages.world-unload-error", "%world%", worldName));
            return true;
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (deleteFolder(worldFolder)) {
            plugin.getWorldManager().removeCustomWorld(worldName);
            sender.sendMessage(plugin.getMessage("messages.world-deleted", "%world%", worldName));
        } else {
            sender.sendMessage(plugin.getMessage("messages.world-delete-error", "%world%", worldName));
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