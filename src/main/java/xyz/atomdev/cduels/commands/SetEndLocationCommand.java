package xyz.atomdev.cduels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.util.CC;
import xyz.atomdev.cduels.util.SerializationUtil;

@RequiredArgsConstructor
public class SetEndLocationCommand extends BaseCommand {

    private final CDuels instance;

    @CommandAlias("setendlocation")
    @CommandPermission("cduels.admin")
    public void setEndLocation(Player player) {
        instance.getConfig().set("end-location", SerializationUtil.serializeLocation(player.getLocation()));
        instance.saveConfig();

        CC.sendMessage(player, "&aSaved your location as end location for duels!");
    }
}