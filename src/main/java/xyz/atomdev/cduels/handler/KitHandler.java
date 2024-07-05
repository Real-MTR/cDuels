package xyz.atomdev.cduels.handler;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.kit.Kit;
import xyz.atomdev.cduels.util.ConfigFile;
import xyz.atomdev.cduels.util.SerializationUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@Getter
public class KitHandler {

    private final CDuels plugin;
    private final Map<String, Kit> kits;

    public KitHandler(CDuels plugin) {
        this.plugin = plugin;
        this.kits = new ConcurrentHashMap<>();

        loadKits();
    }

    @SneakyThrows
    private void loadKits() {
        ConfigFile kitFile = plugin.getKitFile();

        if (kitFile == null) {
            plugin.getLogger().severe("Failed to load kit file!");
            return;
        }

        kits.clear();

        for (String key : kitFile.getConfiguration().getKeys(false)) {
            kits.put(key, new Kit(key,
                    SerializationUtil.deserializeItems(kitFile.getString(key + ".items")),
                    SerializationUtil.deserializeItems(kitFile.getString(key + ".armor")),
                    SerializationUtil.deserializeEffects(kitFile.getString(key + ".effects"))));
        }
    }

    public Kit getKit(String name) {
        return kits.values().stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Kit createKit(String name) {
        Kit kit = new Kit(name);
        kits.put(name, kit);

        return kit;
    }

    public void saveKit(Kit kit) {
        ConfigFile kitFile = plugin.getKitFile();

        if (kitFile == null) {
            plugin.getLogger().severe("Failed to save kit file!");
            return;
        }

        kitFile.set("arenas." + kit.getName() + ".name", kit.getName());
        kitFile.set("arenas." + kit.getName() + ".effects", kit.getEffects().isEmpty() ? new ArrayList<>() :
                SerializationUtil.serializeEffects(kit.getEffects()));
        kitFile.set("arenas." + kit.getName() + ".items", kit.getInventoryContents().length == 0 ? new ItemStack[]{} :
                SerializationUtil.serializeItems(kit.getInventoryContents()));
        kitFile.set("arenas." + kit.getName() + ".armor", kit.getArmorContents().length == 0 ? new ItemStack[]{} :
                SerializationUtil.serializeItems(kit.getArmorContents()));

        kitFile.save();
    }

    public void deleteKit(Kit kit) {
        kits.remove(kit.getName());
        ConfigFile kitFile = plugin.getKitFile();

        if (kitFile == null) {
            plugin.getLogger().severe("Failed to delete kit from kit file!");
            return;
        }

        kitFile.getConfiguration().set(kit.getName(), null);
        kitFile.save();
    }
}
