package xyz.atomdev.cduels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
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

@CommandAlias("duel")
@RequiredArgsConstructor
public class DuelCommands extends BaseCommand {

    private final CDuels instance;

    @Default
    @Subcommand("help")
    public void help(Player player) {
        CC.sendMessageAsList(player, new String[]{
                "&8--------------------------------",
                "&b&lDuel Commands",
                "&8--------------------------------",
                "&7* &e/duel send <player>",
                "&7* &e/duel accept <player>",
                "&8--------------------------------"
        });
    }

    @Subcommand("send")
    public void onDuelCommand(Player sender, @Name("player") Player target) {
        DuelRequest request = DuelRequest.getByRequester(sender.getUniqueId());

        if(sender.getName().equals(target.getName())) {
            CC.sendMessage(sender, "&cYou cannot duel yourself!");
            return;
        }

        if (request != null && !request.isExpired() && request.getTarget().getName().equals(target.getName())) {
            CC.sendMessage(sender, "&cYou have already sent a duel request to " + target.getName() + ", Please wait until it expires or he accepts!");
            return;
        }

        new KitsMenu(instance, target).openMenu(sender);
    }

    @Subcommand("accept")
    public void onDuelAcceptCommand(Player sender, @Name("player") Player target) {
        DuelRequest request = DuelRequest.getByRequester(target.getUniqueId());

        if (request == null || request.isExpired()) {
            CC.sendMessage(sender, "&cNo pending requests were found from " + target.getName() + "!");
            return;
        }

        CC.sendMessage(sender, "&aYou have accepted &b" + target.getName() + "'s &aduel request with the kit " + request.getKit().getName());
        CC.sendMessage(target, "&b" + sender.getName() + " &aaccepted the duel request with the kit " + request.getKit().getName());

        Duel duel = new Duel(target.getName(), sender.getName(), request.getKit());
        instance.getDuelHandler().start(duel);
    }
}