package xyz.atomdev.cduels;

import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import xyz.atomdev.cduels.database.SQLDatabase;

@Getter
public final class CDuels extends JavaPlugin {

    @Getter private static CDuels instance;

    private SQLDatabase database;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadDatabase();
    }

    @Override
    public void onDisable() {
       instance = null;
    }

    private void loadDatabase() {
        try {
            this.database = new SQLDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}