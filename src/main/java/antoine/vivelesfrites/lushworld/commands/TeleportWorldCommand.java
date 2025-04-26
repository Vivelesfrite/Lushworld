package antoine.vivelesfrites.lushworld.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import antoine.vivelesfrites.lushworld.Lushworld;

public class TeleportWorldCommand implements CommandExecutor {

    private final Lushworld plugin;

    public TeleportWorldCommand(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lushworld.command.tpmonde")) {
            sender.sendMessage(plugin.getMessage("messages.no-permission"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.getMessage("messages.usage-tpmonde"));
            return false;
        }

        Player targetPlayer = null;
        String worldName;

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessage("messages.player-only"));
                return true;
            }
            targetPlayer = (Player) sender;
            worldName = args[0];
        } else {
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(plugin.getMessage("messages.player-not-found", "%player%", args[0]));
                return true;
            }
            worldName = args[1];
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(plugin.getMessage("messages.world-not-found", "%world%", worldName));
            return true;
        }

        targetPlayer.teleport(world.getSpawnLocation());
        targetPlayer.sendMessage(plugin.getMessage("messages.teleported", "%world%", worldName));
        if (sender != targetPlayer) {
            sender.sendMessage(plugin.getMessage("messages.teleported-other", "%player%", targetPlayer.getName(), "%world%", worldName));
        }
        return true;
    }
}