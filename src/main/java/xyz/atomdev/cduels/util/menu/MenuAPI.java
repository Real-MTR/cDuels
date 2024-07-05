package xyz.atomdev.cduels.util.menu;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import xyz.atomdev.cduels.util.menu.button.ButtonListener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class MenuAPI {
    @Getter private static Plugin plugin;
    @Getter private static Map<UUID, Menu> openedMenus = new ConcurrentHashMap<>();

    public MenuAPI(Plugin p0) {
        plugin = p0;

        plugin.getServer().getPluginManager().registerEvents(new ButtonListener(), plugin);
    }
}