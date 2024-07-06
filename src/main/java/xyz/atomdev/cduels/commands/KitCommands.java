package xyz.atomdev.cduels.commands;

import lombok.RequiredArgsConstructor;
import me.andyreckt.raspberry.annotation.Children;
import me.andyreckt.raspberry.annotation.Command;
import me.andyreckt.raspberry.annotation.Param;
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
@Command(names = "duelkit", permission = "cduels.admin")
public class KitCommands {

    private final CDuels instance;

    @Children(names = "create")
    public void create(Player player, @Param(name = "name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit != null) {
            CC.sendMessage(player, "&cThis kit already exists! Please choose a new name for your new kit!");
            return;
        }

        Kit newKit = instance.getKitHandler().createKit(name);

        newKit.setArmorContents(player.getInventory().getContents());
        newKit.setInventoryContents(player.getInventory().getArmorContents());

        instance.getKitHandler().saveKit(newKit);
        CC.sendMessage(player, "&aCreated kit &e" + name);
    }

    @Children(names = "delete")
    public void delete(Player player, @Param(name = "name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);

        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        instance.getKitHandler().deleteKit(kit);
        CC.sendMessage(player, "&aDeleted kit &e" + name);
    }

    @Children(names = "setitems")
    public void setItems(Player player, @Param(name = "name") String name) {
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

    @Children(names = "addeffect")
    public void addEffect(Player player, @Param(name = "name") String name, @Param(name = "effect") String effectName, @Param(name = "amplifier") int amplifier) {
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

    @Children(names = "removeeffect")
    public void removeEffect(Player player, @Param(name = "name") String name, @Param(name = "effect") String effectName) {
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

    @Children(names = "showeffects")
    public void showEffects(Player player, @Param(name = "name") String name) {
        Kit kit = instance.getKitHandler().getKit(name);
        if(kit == null) {
            CC.sendMessage(player, "&cThis kit does not exist!");
            return;
        }

        CC.sendMessage(player, "&b" + name + "'s &aEffects: " + kit.getEffects().stream().map(effect -> effect.getType().getName()));
    }

    @Children(names = "list")
    public void list(Player player) {
        CC.sendMessage(player, "&aKits List: " + instance.getKitHandler().getKits().values().stream().map(Kit::getName).collect(Collectors.toList()));
    }
}