package xyz.atomdev.cduels.model.duel.task;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.atomdev.cduels.model.duel.Duel;
import xyz.atomdev.cduels.model.duel.state.DuelState;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

public class DurationTask extends BukkitRunnable {

    private final Duel duel;

    public DurationTask(Duel duel) {
        this.duel = duel;
    }

    @Override
    public void run() {
        if (duel.getState() == DuelState.FIGHTING || duel.getState() == DuelState.STARTING) {
            duel.setDuration(duel.getDuration() + 1);
        } else {
            this.cancel();
        }
    }
}