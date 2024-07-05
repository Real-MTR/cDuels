package xyz.atomdev.cduels.util;

import lombok.experimental.UtilityClass;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@UtilityClass
public class PotionEffectSerializer {
    public String serializePotionEffects(PotionEffect[] potionEffects) {
        List<String> serializedEffects = new ArrayList<>();

        for (PotionEffect effect : potionEffects) {
            String serializedEffect = effect.getType().getName() + ";" +
                    effect.getDuration() + ";" +
                    effect.getAmplifier();
            serializedEffects.add(serializedEffect);
        }

        return String.join(",", serializedEffects);
    }

    public String serializePotionEffects(List<PotionEffect> potionEffects) {
        List<String> serializedEffects = new ArrayList<>();

        for (PotionEffect effect : potionEffects) {
            String serializedEffect = effect.getType().getName() + ";" +
                    effect.getDuration() + ";" +
                    effect.getAmplifier();
            serializedEffects.add(serializedEffect);
        }

        return String.join(",", serializedEffects);
    }

    public PotionEffect[] deserializePotionEffects(String serializedData) {
        List<PotionEffect> potionEffects = new ArrayList<>();

        if (serializedData != null && !serializedData.isEmpty()) {
            String[] serializedEffects = serializedData.split(",");

            for (String serializedEffect : serializedEffects) {
                String[] parts = serializedEffect.split(";");
                if (parts.length == 3) {
                    PotionEffectType type = PotionEffectType.getByName(parts[0]);
                    if (type != null) {
                        int duration = Integer.parseInt(parts[1]);
                        int amplifier = Integer.parseInt(parts[2]);
                        PotionEffect effect = new PotionEffect(type, duration, amplifier);
                        potionEffects.add(effect);
                    }
                }
            }
        }

        return potionEffects.toArray(new PotionEffect[0]);
    }

    public List<PotionEffect> deserializePotionEffectsList(String serializedData) {
        List<PotionEffect> potionEffects = new ArrayList<>();

        if (serializedData != null && !serializedData.isEmpty()) {
            String[] serializedEffects = serializedData.split(",");

            for (String serializedEffect : serializedEffects) {
                String[] parts = serializedEffect.split(";");
                if (parts.length == 3) {
                    PotionEffectType type = PotionEffectType.getByName(parts[0]);
                    if (type != null) {
                        int duration = Integer.parseInt(parts[1]);
                        int amplifier = Integer.parseInt(parts[2]);
                        PotionEffect effect = new PotionEffect(type, duration, amplifier);
                        potionEffects.add(effect);
                    }
                }
            }
        }

        return potionEffects;
    }
}