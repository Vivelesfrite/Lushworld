package antoine.vivelesfrites.lushworld.commands;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportWorldTabCompleter implements TabCompleter {

    private final Lushworld plugin;

    public TeleportWorldTabCompleter(Lushworld plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(player.getName());
                }
            }
        } else if (args.length == 2) {
            List<String> customWorlds = plugin.getWorldManager().getCustomWorlds();
            for (String worldName : customWorlds) {
                if (worldName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(worldName);
                }
            }
        }

        return suggestions;
    }
}