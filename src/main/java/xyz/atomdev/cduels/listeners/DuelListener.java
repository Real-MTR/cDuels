package xyz.atomdev.cduels.listeners;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.duel.state.DuelState;
import xyz.atomdev.cduels.model.profile.Profile;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

public class DuelListener implements Listener {

    private final CDuels plugin;

    public DuelListener(CDuels instance) {
        this.plugin = instance;

        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(player.getName());

        if (duelPlayer.isInDuel() && !player.hasPermission("duels.admin")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Arrow) && !(event.getDamager() instanceof FishHook)) return;

        Player damaged = (Player) event.getEntity();
        Player damager = getDamager(event);

        if (cancelDamageEvent(damaged, damager)) {
            event.setCancelled(true);
        }
    }

    private Player getDamager(EntityDamageByEntityEvent event) {
        Player damager = null;

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
            Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(damager.getName());

            if (duelPlayer.isInDuel() && !plugin.getProfileHandler().getProfileMap().get(event.getEntity().getName()).isInDuel()) {
                return null;
            }
        }

        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile.getShooter() instanceof Player) {
                damager = (Player) projectile.getShooter();
            }
        }

        return damager;
    }

    private boolean cancelDamageEvent(Player damaged, Player damager) {
        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(damaged.getName());
        return !duelPlayer.isInDuel() || damager != null && !duelPlayerIsOpponent(damaged, damager);
    }

    private boolean duelPlayerIsOpponent(Player damaged, Player damager) {
        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(damaged.getName());
        return duelPlayer.getCurrentDuel().getOpponent(duelPlayer).getName().equals(damager.getName());
    }

    @EventHandler
    public void disableDamageIfDuelEndsOrStarts(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(player.getName());
            if (!duelPlayer.isInDuel()) return;

            if (duelPlayer.getCurrentDuel().getState() != DuelState.FIGHTING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(player.getName());
        if (!duelPlayer.isInDuel()) return;

        if (player.getHealth() <= event.getFinalDamage()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.spigot().respawn(), 2L);

            // TODO: End duel here!
            plugin.getDuelHandler().end(duelPlayer.getCurrentDuel());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(event.getPlayer().getName());
        if (!duelPlayer.isInDuel()) return;

        if (event.getPlayer().getItemInHand().getType() == Material.DIAMOND_SWORD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.getProfileHandler().getProfileMap().get(player.getName()).isInDuel()) {
            if (event.getInventory() != null && !event.getInventory().equals(player.getInventory())) {
                event.getInventory().clear();
                event.setCursor(null);
                player.setItemOnCursor(null);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (plugin.getProfileHandler().getProfileMap().get(player.getName()).isInDuel()) {
            if (event.getInventory() != null && !event.getInventory().equals(player.getInventory())) {
                event.getInventory().clear();
                player.setItemOnCursor(null);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event) {
        handlePlayerQuit(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handlePlayerQuit(event.getPlayer());
    }

    private void handlePlayerQuit(Player player) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(player.getUniqueId());

        if (!offlinePlayer.hasPlayedBefore() || !plugin.getProfileHandler().getProfileMap().get(player.getName()).isInDuel()) {
            return;
        }

        Profile duelPlayer = plugin.getProfileHandler().getProfileMap().get(player.getName());

        plugin.getDuelHandler().end(duelPlayer.getCurrentDuel());
    }
}
