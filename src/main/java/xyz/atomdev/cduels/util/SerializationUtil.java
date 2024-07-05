package xyz.atomdev.cduels.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@UtilityClass
public class SerializationUtil {

    public String serializeItems(ItemStack[] items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public ItemStack[] deserializeItems(String data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    private byte[] serializePotionEffectsToBytes(List<PotionEffect> potionEffects) {
        String serializedData = PotionEffectSerializer.serializePotionEffects(potionEffects);
        return serializedData.getBytes(StandardCharsets.UTF_8);
    }

    private List<PotionEffect> bytesToPotionEffects(byte[] serializedData) {
        String dataString = new String(serializedData, StandardCharsets.UTF_8);
        return PotionEffectSerializer.deserializePotionEffectsList(dataString);
    }

    public String serializeEffects(List<PotionEffect> potionEffects) {
        byte[] serializedData = serializePotionEffectsToBytes(potionEffects);
        return Base64.getEncoder().encodeToString(serializedData);
    }

    public List<PotionEffect> deserializeEffects(String base64Data) {
        byte[] serializedData = Base64.getDecoder().decode(base64Data);
        return bytesToPotionEffects(serializedData);
    }
}
