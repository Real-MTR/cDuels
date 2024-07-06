package xyz.atomdev.cduels.model.duel.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.duel.state.DuelState;
import xyz.atomdev.cduels.util.CC;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

public class CountdownTask extends BukkitRunnable {

    private final CDuels instance;
    private final Player player1, player2;
    private int secondsLeft;

    public CountdownTask(CDuels instance, Player player1, Player player2, int seconds) {
        this.instance = instance;
        this.player1 = player1;
        this.player2 = player2;
        this.secondsLeft = seconds;
    }

    @Override
    public void run() {
        if (secondsLeft > 0) {
            CC.sendMessage(player1, "&a" + secondsLeft + "...");
            CC.sendMessage(player2, "&a" + secondsLeft + "...");
            secondsLeft--;
        } else if (secondsLeft == 0) {
            CC.sendMessage(player1, "&aGO!");
            CC.sendMessage(player2, "&aGO!");

            instance.getProfileHandler().getProfileMap().get(player1.getName()).getCurrentDuel().setState(DuelState.FIGHTING);
            this.cancel();
        }
    }
}