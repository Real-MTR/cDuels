package xyz.atomdev.cduels.model.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.atomdev.cduels.model.arena.Arena;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@AllArgsConstructor
@Getter
@Setter
public class Kit {

    private final String name;
    private ItemStack[] inventoryContents, armorContents;
    private List<PotionEffect> effects;
    private List<UUID> allowedArenasId;

    public Kit(String name) {
        this.name = name;
        this.inventoryContents = null;
        this.armorContents = null;
        this.effects = new ArrayList<>();
        this.allowedArenasId = new ArrayList<>();
    }

    // TODO: Return all of the arenas assigned to the specific kit.
    public List<Arena> getAllowedArenas() {
        return new ArrayList<>();
    }

    public void removeEffect(PotionEffectType effectType) {
        if(hasEffect(effectType)) effects.remove(effects.stream().filter(effect -> effect.getType() == effectType).findFirst().get());
    }

    public boolean hasEffect(PotionEffectType effectType) {
        return effects.stream().filter(effect -> effect.getType() == effectType).findFirst().orElse(null) != null;
    }
}