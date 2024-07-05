package xyz.atomdev.cduels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.RequiredArgsConstructor;
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
@CommandAlias("arena")
@CommandPermission("cduels.admin")
public class ArenaCommands extends BaseCommand {

    private final CDuels instance;

    @Default
    @Subcommand("help")
    public void help(Player player) {
        CC.sendMessageAsList(player, new String[]{
                "&8--------------------------------",
                "&b&lArena Manager Help",
                "&8--------------------------------",
                "&7* &e/arena create <name>",
                "&7* &e/arena delete <name>",
                "&7* &e/arena setpos1 / setpos2 <name>",
                "&7* &e/arena addkit <name> <kit>",
                "&7* &e/arena removekit <name> <kit>",
                "&7* &e/arena allowedkits <name>",
                "&7* &e/arena list",
                "&8--------------------------------"
        });
    }

    @Subcommand("create")
    public void create(Player player, @Name("name") String name) {
        Arena oldArena = instance.getArenaHandler().getArenaByName(name);

        if(oldArena != null) {
            CC.sendMessage(player, "&cThis arena already exists! Please choose a new name for your new arena!");
            return;
        }

        instance.getArenaHandler().createArena(name);
        CC.sendMessage(player, "&aCreated arena &e" + name);
    }

    @Subcommand("delete")
    public void delete(Player player, @Name("name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        instance.getArenaHandler().deleteArena(arena.getId());
        CC.sendMessage(player, "&aDeleted arena &e" + name);
    }

    @Subcommand("setpos1")
    public void setpos1(Player player, @Name("name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        arena.setPos1(player.getLocation());
        instance.getArenaHandler().saveArena(arena);

        CC.sendMessage(player, "&aSet pos1 arena &e" + name);
    }

    @Subcommand("setpos2")
    public void setpos2(Player player, @Name("name") String name) {
        Arena arena = instance.getArenaHandler().getArenaByName(name);

        if(arena == null) {
            CC.sendMessage(player, "&cThis arena does not exist!");
            return;
        }

        arena.setPos2(player.getLocation());
        instance.getArenaHandler().saveArena(arena);

        CC.sendMessage(player, "&aSet pos1 arena &e" + name);
    }

    @Subcommand("list")
    public void list(Player player) {
        CC.sendMessage(player, "&aArenas: &b" + instance.getArenaHandler().getArenas().values().stream().map(Arena::getName).collect(Collectors.toList()));
    }
}
