package xyz.atomdev.cduels.model.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.duel.Duel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@RequiredArgsConstructor
@Getter @Setter
public class Profile {

    private final String name;

    private UUID currentDuelId = new UUID(0, 0);
    private ItemStack[] lastItems = new ItemStack[]{}, lastArmor = new ItemStack[]{};
    private Collection<PotionEffect> lastEffects = new ArrayList<>();

    private int wins = 0, losses = 0, gamesPlayed = 0;
    private boolean inDuel = false;

    public Profile(String name, int wins, int losses, int gamesPlayed, ItemStack[] lastItems, ItemStack[] lastArmor, List<PotionEffect> lastEffects) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.gamesPlayed = gamesPlayed;
        this.lastItems = lastItems;
        this.lastArmor = lastArmor;
        this.lastEffects = lastEffects;
    }

    public Duel getCurrentDuel() {
        return CDuels.getInstance().getDuelHandler().getDuelById(currentDuelId);
    }

    public boolean isInDuel() {
        return getCurrentDuel() != null;
    }

    public void setCurrentDuel(Duel duel) {
        this.currentDuelId = duel.getUuid();
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(name);
    }

    public OfflinePlayer toBukkitOfflinePlayer() {
        return Bukkit.getOfflinePlayer(name);
    }

    public boolean isOnline() {
        return toBukkitPlayer() != null && toBukkitPlayer().isOnline();
    }

    // Just to check
    @Override
    public String toString() {
        return getName() + ";"
                + getWins() + ";"
                + getLosses() + ";"
                + getGamesPlayed() + ";";
    }
}
