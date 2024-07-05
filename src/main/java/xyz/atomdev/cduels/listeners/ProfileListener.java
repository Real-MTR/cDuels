package xyz.atomdev.cduels.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.profile.Profile;
import xyz.atomdev.cduels.util.CC;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

public class ProfileListener implements Listener {

    private final CDuels instance;

    public ProfileListener(CDuels instance) {
        this.instance = instance;

        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    // Using login event cuz async prelogin event is useless here
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Profile profile = instance.getProfileHandler().getProfileMap().get(player.getName());

        if(profile == null) {
            profile = new Profile(player.getName());

            instance.getProfileHandler().getProfileMap().put(profile.getName(), profile);
        }
    }
}
