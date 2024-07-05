package xyz.atomdev.cduels.handler;

import lombok.Getter;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.kit.Kit;
import xyz.atomdev.cduels.util.ConfigFile;

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

    private void loadKits() {
        ConfigFile kitFile = plugin.getKitFile();

        if (kitFile == null) {
            plugin.getLogger().severe("Failed to load kit file!");
            return;
        }

        kits.clear();

        for (String key : kitFile.getConfiguration().getKeys(false)) {
            kits.put(key, (Kit) kitFile.getConfiguration().get(key, Kit.class));
        }
    }

    public Kit getKit(String name) {
        return kits.values().stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Kit createKit(String name) {
        Kit kit = new Kit(name);
        saveKit(kit);

        kits.put(name, kit);
        return kit;
    }

    public void saveKit(Kit kit) {
        ConfigFile kitFile = plugin.getKitFile();

        if (kitFile == null) {
            plugin.getLogger().severe("Failed to save kit file!");
            return;
        }

        kitFile.getConfiguration().set(kit.getName(), kit);
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
