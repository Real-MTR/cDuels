package xyz.atomdev.cduels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.kit.Kit;
import xyz.atomdev.cduels.util.CC;

import java.util.stream.Collectors;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@RequiredArgsConstructor
@CommandAlias("duelkit")
@CommandPermission("cduels.admin")
public class KitCommands extends BaseCommand {

    private final CDuels instance;

    @Default
    @Subcommand("help")
    public void help(Player player) {
        CC.sendMessageAsList(player, new String[] {
                "&8--------------------------------",
                "&b&lKit Manager Help",
                "&8--------------------------------",
                "&7* &e/duelkit create <name>",
                "&7* &e/duelkit delete <name>",
                "&7* &e/duelkit setitems <name>",
                "&7* &e/duelkit addeffect <name> <effect> <amplifier>",
                "&7* &e/duelkit removeeffect <name> <effect>",
                "&7* &e/duelkit showeffects <name>",
                "&7* &e/duelkit list",
                "&8--------------------------------"
        });
    }

    @Subcommand("create")
    public void create(Player player, @Name("name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit != null) {
            CC.sendMessage(player, "&cThis kit already exists! Please choose a new name for your new kit!");
            return;
        }

        instance.getKitHandler().createKit(name);
        CC.sendMessage(player, "&aCreated kit &e" + name);
    }

    @Subcommand("delete")
    public void delete(Player player, @Name("name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        instance.getKitHandler().deleteKit(kit);
        CC.sendMessage(player, "&aDeleted kit &e" + name);
    }

    @Subcommand("setitems")
    public void setItems(Player player, @Name("name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        kit.setInventoryContents(player.getInventory().getContents());
        kit.setArmorContents(player.getInventory().getArmorContents());
        instance.getKitHandler().saveKit(kit);
        CC.sendMessage(player, "&aSet items to kit &e" + name);
    }

    @Subcommand("addeffect")
    public void addEffect(Player player, @Name("name") String name, @Name("effect") String effectName, @Name("amplifier") int amplifier) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        PotionEffectType effectType = PotionEffectType.getByName(effectName);
        if(effectType == null) {
            CC.sendMessage(player, "&cThis potion effect does not exist!");
            return;
        }

        if(amplifier > 255 || amplifier < 0) {
            CC.sendMessage(player, "&cThe amplifier can be set between 0 and 255!");
            return;
        }

        if(kit.hasEffect(effectType)) {
            CC.sendMessage(player, "&cThis kit already contains this effect!");
            return;
        }

        kit.getEffects().add(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier, false, false));
        instance.getKitHandler().saveKit(kit);
        CC.sendMessage(player, "&aAdded potion to kit &e" + name);
    }

    @Subcommand("removeeffect")
    public void removeEffect(Player player, @Name("name") String name, @Name("effect") String effectName) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        PotionEffectType effectType = PotionEffectType.getByName(effectName);
        if(effectType == null) {
            CC.sendMessage(player, "&cThis potion effect does not exist!");
            return;
        }

        if(!kit.hasEffect(effectType)) {
            CC.sendMessage(player, "&cThis kit does not contain this effect!");
            return;
        }

        kit.removeEffect(effectType);
        instance.getKitHandler().saveKit(kit);
        CC.sendMessage(player, "&aRemoved potion to kit &e" + name);
    }

    @Subcommand("showeffects")
    public void showEffects(Player player, @Name("name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);
        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        CC.sendMessage(player, "&b" + name + "'s &aEffects: " + kit.getEffects().stream().map(effect -> effect.getType().getName()));
    }

    @Subcommand("list")
    public void list(Player player) {
        CC.sendMessage(player, "&aKits List: " + instance.getKitHandler().getKits().values().stream().map(Kit::getName).collect(Collectors.toList()));
    }
}