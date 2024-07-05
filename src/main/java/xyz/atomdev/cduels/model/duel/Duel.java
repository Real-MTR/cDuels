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
    private final UUID player1, player2;
    private Kit kit;
    private Arena arena;
    private DuelState state;
    private double wager = 0;
    private int duration;

    public Duel(UUID player1, UUID player2, Kit kit) {
        this.uuid = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
        this.state = DuelState.STARTING;
        this.duration = 0;
    }

    public Duel(UUID player1, UUID player2, Kit kit, double wager) {
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

    // TODO: Retrieve the duel players...
    public Profile getFirstPlayer() {
        return null;
    }

    public Profile getSecondPlayer() {
        return null;
    }

    public Profile getOpponent(Profile player) {
        return getFirstPlayer().getName().equals(player.getName()) ? getSecondPlayer() : getFirstPlayer();
    }

    // TODO: Remove the players from the game & remove their map traces
    public void end() {
    }
}