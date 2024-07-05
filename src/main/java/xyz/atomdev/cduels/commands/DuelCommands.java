package xyz.atomdev.cduels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.menu.KitsMenu;
import xyz.atomdev.cduels.model.duel.Duel;
import xyz.atomdev.cduels.model.request.DuelRequest;
import xyz.atomdev.cduels.util.CC;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@RequiredArgsConstructor
public class DuelCommands extends BaseCommand {

    private final CDuels instance;

    @CommandAlias("duel")
    public void onDuelCommand(Player sender, Player target, @Name("wager") @Optional Double wager) {
        if (target == null || !target.isOnline()) {
            CC.sendMessage(sender, "&cThat player was not found!");
            return;
        }

        if (sender.equals(target)) {
            CC.sendMessage(sender, "&cYou cannot duel yourself!");
            return;
        }

        DuelRequest request = DuelRequest.getByRequester(target.getUniqueId());

        if (request != null && !request.isExpired()) {
            CC.sendMessage(sender, "&cYou have already sent a duel request to " + target.getName() + ", Please wait until it expires or he accepts!");
            return;
        }

        new KitsMenu(instance, target, wager != null, wager == null ? -1 : wager);
    }

    @CommandAlias("duelaccept")
    public void onDuelAcceptCommand(Player sender, @Name("player") Player target) {
        DuelRequest request = DuelRequest.getByRequester(target.getUniqueId());

        if (request == null || request.isExpired()) {
            CC.sendMessage(sender, "&cNo pending requests were found from " + target.getName() + "!");
            return;
        }

        CC.sendMessage(sender, "&aYou have accepted &b" + target.getName() + "'s &aduel request with the kit " + request.getKit().getName());
        CC.sendMessage(target, "&b" + sender.getName() + " &aaccepted the duel request with the kit " + request.getKit().getName());

        Duel duel;

        if(request.getBet() == 0) {
            duel = new Duel(target.getName(), sender.getName(), request.getKit());
        } else {
            duel = new Duel(target.getName(), sender.getName(), request.getKit(), request.getBet());
        }

        instance.getDuelHandler().start(duel);
    }
}
