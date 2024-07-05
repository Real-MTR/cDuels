package xyz.atomdev.cduels;

import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import xyz.atomdev.cduels.database.SQLDatabase;
import xyz.atomdev.cduels.handler.ProfileHandler;
import xyz.atomdev.cduels.listeners.ProfileListener;

@Getter
public final class CDuels extends JavaPlugin {

    @Getter private static CDuels instance;

    private SQLDatabase database;
    private ProfileHandler profileHandler;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadDatabase();
        registerListeners();
    }

    @Override
    public void onDisable() {
        profileHandler.save(false);
        instance = null;
    }

    private void loadDatabase() {
        this.database = new SQLDatabase(this);
        this.profileHandler = new ProfileHandler(this);
    }

    private void registerListeners() {
        new ProfileListener(this);
    }
}