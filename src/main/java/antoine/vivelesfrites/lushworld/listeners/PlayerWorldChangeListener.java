package antoine.vivelesfrites.lushworld.listeners;

import antoine.vivelesfrites.lushworld.Lushworld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChangeListener implements Listener {

    private final Lushworld plugin;

    public PlayerWorldChangeListener(Lushworld plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String previousWorld = event.getFrom().getName();


        try {
            plugin.getPositionManager().savePlayerLocation(
                    player.getUniqueId(),
                    previousWorld,
                    player.getLocation()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}