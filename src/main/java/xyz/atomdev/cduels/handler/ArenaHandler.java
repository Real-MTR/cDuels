package xyz.atomdev.cduels.handler;

import lombok.Getter;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.arena.Arena;
import xyz.atomdev.cduels.util.SerializationUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@Getter
public class ArenaHandler {

    private final CDuels plugin;
    private final Map<String, Arena> arenas;

    public ArenaHandler(CDuels plugin) {
        this.plugin = plugin;
        this.arenas = new ConcurrentHashMap<>();

        loadArenas();
    }

    private void loadArenas() {
        if (plugin.getArenaFile().getConfiguration().contains("arenas") && plugin.getArenaFile().getConfiguration().getConfigurationSection("arenas") != null) {
            for (String arenaId : plugin.getArenaFile().getConfiguration().getConfigurationSection("arenas").getKeys(false)) {
                Arena arena = new Arena(arenaId,
                        plugin.getArenaFile().getString("arenas." + arenaId + ".name"),
                        SerializationUtil.deserializeLocation(plugin.getArenaFile().getString("arenas." + arenaId + ".pos1")),
                        SerializationUtil.deserializeLocation(plugin.getArenaFile().getString("arenas." + arenaId + ".pos2")),
                        plugin.getArenaFile().getBoolean("arenas." + arenaId + ".occupied"));

                arenas.put(arenaId, arena);
            }
        }
    }

    public Arena getArenaById(String id) {
        return arenas.get(id);
    }

    public Arena getArenaByName(String name) {
        return arenas.values().stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Arena getFirstAvailableArena() {
        for(Arena arena : arenas.values()) {
            if(!arena.isOccupied()) return arena;
        }

        return null;
    }

    public void createArena(String name) {
        if (getArenaByName(name) != null) return;
        Arena arena = new Arena(name.toLowerCase(), name);

        saveArena(arena);
    }

    public void saveArena(Arena arena) {
        arenas.putIfAbsent(arena.getId().toLowerCase(), arena);

        plugin.getArenaFile().set("arenas." + arena.getId() + ".name", arena.getName());
        plugin.getArenaFile().set("arenas." + arena.getId() + ".occupied", arena.isOccupied());
        plugin.getArenaFile().set("arenas." + arena.getId() + ".pos1", SerializationUtil.serializeLocation(arena.getPos1()));
        plugin.getArenaFile().set("arenas." + arena.getId() + ".pos2", SerializationUtil.serializeLocation(arena.getPos2()));

        plugin.getArenaFile().save();
    }

    public void deleteArena(String arenaId) {
        Arena arena = getArenaById(arenaId);

        if (arena != null) {
            arenas.remove(arenaId.toLowerCase());

            plugin.getArenaFile().set("arenas." + arenaId.toLowerCase(), null);
            plugin.getArenaFile().save();
        }
    }
}