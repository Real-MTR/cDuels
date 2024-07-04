package xyz.atomdev.cduels.model.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import xyz.atomdev.cduels.model.duel.Duel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@RequiredArgsConstructor
@Getter @Setter
public class Profile {

    private final UUID uuid;

    private UUID currentDuelId = new UUID(0, 0);
    private ItemStack[] lastItems = new ItemStack[]{}, lastArmor = new ItemStack[]{};
    private Collection<PotionEffect> lastEffects = new ArrayList<>();

    private int wins = 0, losses = 0, winStreak = 0;

    // TODO: Easier to access the player's current duel
    public Duel getCurrentDuel() {
        return null;
    }

    public boolean isInDuel() {
        return getCurrentDuel() != null;
    }

    public void setCurrentDuel(Duel duel) {
        this.currentDuelId = duel.getUuid();
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer toBukkitOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public boolean isOnline() {
        return toBukkitPlayer() != null && toBukkitPlayer().isOnline();
    }
}
