package xyz.atomdev.cduels;

import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

public final class CDuels extends JavaPlugin {

    @Getter private static CDuels instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
       instance = null;
    }
}