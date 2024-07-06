package xyz.atomdev.cduels.model.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@AllArgsConstructor
@Getter
@Setter
public class Kit {

    private final String name;
    private String permission;
    private ItemStack[] inventoryContents, armorContents;
    private List<PotionEffect> effects;

    public Kit(String name) {
        this.name = name;
        this.permission = "cduels.kit." + name;
        this.inventoryContents = null;
        this.armorContents = null;
        this.effects = new ArrayList<>();
    }

    public void removeEffect(PotionEffectType effectType) {
        if(hasEffect(effectType)) effects.remove(effects.stream().filter(effect -> effect.getType() == effectType).findFirst().get());
    }

    public boolean hasEffect(PotionEffectType effectType) {
        return effects.stream().filter(effect -> effect.getType() == effectType).findFirst().orElse(null) != null;
    }
}