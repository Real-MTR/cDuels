package xyz.atomdev.cduels.commands;

import lombok.RequiredArgsConstructor;
import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.arena.Arena;
import xyz.atomdev.cduels.util.CC;

import java.util.stream.Collectors;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@RequiredArgsConstructor
@Command(names = {"arena"}, permission = "cduels.admin")
public class ArenaCommands {

    private final CDuels instance;

    @Children(names = "create")
    public void create(Player player, @Param(name = "name") String name) {
        Arena oldArena = instance.getArenaHandler().getArenaByName(name);

        if(oldArena != null) {
            CC.sendMessage(player, "&cThis arena already exists! Please choose a new name for your new arena!");
            return;
        }

        instance.getArenaHandler().createArena(name);
        CC.sendMessage(player, "&aCreated arena &e" + name);
    }

    @Children(names = "delete")
    public void delete(Player player, @Param(name = "name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        instance.getArenaHandler().deleteArena(arena.getId());
        CC.sendMessage(player, "&aDeleted arena &e" + name);
    }

    @Children(names = "setpos1")
    public void setpos1(Player player, @Param(name = "name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        arena.setPos1(player.getLocation());
        instance.getArenaHandler().saveArena(arena);

        CC.sendMessage(player, "&aSet pos1 arena &e" + name);
    }

    @Children(names = "setpos2")
    public void setpos2(Player player, @Param(name = "name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        arena.setPos2(player.getLocation());
        instance.getArenaHandler().saveArena(arena);

        CC.sendMessage(player, "&aSet pos2 arena &e" + name);
    }

    @Children(names = "list")
    public void list(Player player) {
        CC.sendMessage(player, "&aArenas: &b" + instance.getArenaHandler().getArenas().values().stream().map(Arena::getName).collect(Collectors.toList()));
    }
}
