package xyz.atomdev.cduels.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.arena.Arena;
import xyz.atomdev.cduels.model.duel.Duel;
import xyz.atomdev.cduels.model.duel.state.DuelState;
import xyz.atomdev.cduels.model.duel.task.CountdownTask;
import xyz.atomdev.cduels.model.duel.task.DurationTask;
import xyz.atomdev.cduels.model.profile.Profile;
import xyz.atomdev.cduels.util.CC;
import xyz.atomdev.cduels.util.SerializationUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@RequiredArgsConstructor
@Getter
public class DuelHandler {

    private final CDuels instance;
    private final Map<UUID, Duel> duels = new ConcurrentHashMap<>();

    public void start(Duel duel) {
        Player firstPlayer = duel.getFirstPlayer().toBukkitPlayer();
        Player secondPlayer = duel.getSecondPlayer().toBukkitPlayer();
        Profile firstPlayerProfile = duel.getFirstPlayer();
        Profile secondPlayerProfile = duel.getSecondPlayer();
        Arena arena = instance.getArenaHandler().getFirstAvailableArena();

        if(arena == null) {
            CC.sendMessage(firstPlayer, "&cThere are no available arenas at the moment. Please wait a few minutes and try again!");
            CC.sendMessage(secondPlayer, "&cThere are no available arenas at the moment. Please wait a few minutes and try again!");
            duel.end();
            return;
        }

        if(arena.getPos1() == null || arena.getPos2() == null) {
            CC.sendMessage(firstPlayer, "&cThe selected arena: " + arena.getName() + " wasn't set up correctly, Please talk to a staff member and report this issue!");
            CC.sendMessage(secondPlayer, "&cThe selected arena: " + arena.getName() + " wasn't set up correctly, Please talk to a staff member and report this issue!");
            duel.end();
            return;
        }

        duel.setArena(arena);

        if(arena.getPos2().getChunk() != null && !arena.getPos2().getChunk().isLoaded()) {
            arena.getPos2().getWorld().loadChunk(arena.getPos2().getChunk());
        }

        if(arena.getPos1().getChunk() != null && !arena.getPos1().getChunk().isLoaded()) {
            arena.getPos1().getWorld().loadChunk(arena.getPos1().getChunk());
        }

        saveTemporarilySaves(firstPlayerProfile, duel);
        saveTemporarilySaves(secondPlayerProfile, duel);

        preparePlayer(firstPlayer, duel, true);
        preparePlayer(secondPlayer, duel, false);

        runTimer(duel);
    }

    private void saveTemporarilySaves(Profile profile, Duel duel) {
        profile.setInDuel(true);
        profile.setCurrentDuel(duel);
        profile.setLastArmor(profile.toBukkitPlayer().getInventory().getArmorContents());
        profile.setLastItems(profile.toBukkitPlayer().getInventory().getContents());
        profile.setLastEffects(profile.toBukkitPlayer().getActivePotionEffects());
    }

    private void preparePlayer(Player player, Duel duel, boolean first) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(first ? duel.getArena().getPos1() : duel.getArena().getPos2());
        player.getInventory().setContents(duel.getKit().getInventoryContents());
        player.getInventory().setArmorContents(duel.getKit().getArmorContents());
    }

    public void end(Duel duel) {
        duel.setState(DuelState.ENDING);

        instance.getServer().getScheduler().runTaskLater(instance, () -> {
            Profile firstPlayerProfile = duel.getFirstPlayer();
            Profile secondPlayerProfile = duel.getSecondPlayer();

            resetPlayer(firstPlayerProfile);
            resetPlayer(secondPlayerProfile);

            resetTemporarilySaves(firstPlayerProfile);
            resetTemporarilySaves(secondPlayerProfile);

            duel.end();
        }, 60L);
    }

    private void resetPlayer(Profile profile) {
        profile.toBukkitPlayer().getInventory().setContents(profile.getLastItems());
        profile.toBukkitPlayer().getInventory().setArmorContents(profile.getLastArmor());
        profile.getLastEffects().forEach(profile.toBukkitPlayer()::addPotionEffect);
        profile.toBukkitPlayer().teleport(SerializationUtil.deserializeLocation(instance.getConfig().getString("end-location")));
    }

    private void resetTemporarilySaves(Profile profile) {
        profile.setInDuel(false);
        profile.setCurrentDuelId(new UUID(0, 0));
        profile.setLastArmor(new ItemStack[]{});
        profile.setLastItems(new ItemStack[]{});
        profile.setLastEffects(new ArrayList<>());
    }

    private void runTimer(Duel duel) {
        new CountdownTask(instance, duel.getFirstPlayer().toBukkitPlayer(), duel.getSecondPlayer().toBukkitPlayer(), 3)
                .runTaskTimerAsynchronously(instance, 0, 20);
        new DurationTask(duel).runTaskTimerAsynchronously(instance, 0, 20);
    }

    public Duel getDuelById(UUID id) {
        return duels.values().stream().filter(duel -> duel.getUuid().toString().equals(id.toString())).findFirst().orElse(null);
    }
}
