package xyz.atomdev.cduels.commands;

import lombok.RequiredArgsConstructor;
import me.andyreckt.raspberry.annotation.Command;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.util.CC;
import xyz.atomdev.cduels.util.SerializationUtil;

@RequiredArgsConstructor
public class SetEndLocationCommand {

    private final CDuels instance;

    @Command(names = "setendlocation", permission = "cduels.admin")
    public void setEndLocation(Player player) {
        instance.getConfig().set("end-location", SerializationUtil.serializeLocation(player.getLocation()));
        instance.saveConfig();

        CC.sendMessage(player, "&aSaved your location as end location for duels!");
    }
}