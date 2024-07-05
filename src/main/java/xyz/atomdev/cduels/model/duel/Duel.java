package xyz.atomdev.cduels.model.duel;

import lombok.Getter;
import lombok.Setter;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.arena.Arena;
import xyz.atomdev.cduels.model.duel.state.DuelState;
import xyz.atomdev.cduels.model.kit.Kit;
import xyz.atomdev.cduels.model.profile.Profile;

import java.util.UUID;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@Getter
@Setter
public class Duel {

    private final UUID uuid;
    private final String player1, player2;
    private Kit kit;
    private Arena arena;
    private DuelState state;
    private double wager = 0;
    private int duration;

    public Duel(String player1, String player2, Kit kit) {
        this.uuid = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
        this.state = DuelState.STARTING;
        this.duration = 0;
    }

    public Duel(String player1, String player2, Kit kit, double wager) {
        this.uuid = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.wager = wager;
        this.kit = kit;
        this.state = DuelState.STARTING;
        this.duration = 0;
    }

    public boolean isWager() {
        return wager != 0;
    }

    public Profile getFirstPlayer() {
        return CDuels.getInstance().getProfileHandler().getProfileMap().get(player1);
    }

    public Profile getSecondPlayer() {
        return CDuels.getInstance().getProfileHandler().getProfileMap().get(player2);
    }

    public Profile getOpponent(Profile player) {
        return player1.equals(player.getName()) ? getSecondPlayer() : getFirstPlayer();
    }

    public void end() {
        CDuels.getInstance().getDuelHandler().getDuels().remove(player1);
        CDuels.getInstance().getDuelHandler().getDuels().remove(player2);
    }
}