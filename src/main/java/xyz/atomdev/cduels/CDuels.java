package xyz.atomdev.cduels;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;
import xyz.atomdev.cduels.commands.ArenaCommands;
import xyz.atomdev.cduels.commands.DuelCommands;
import xyz.atomdev.cduels.commands.KitCommands;
import xyz.atomdev.cduels.commands.SetEndLocationCommand;
import xyz.atomdev.cduels.database.SQLDatabase;
import xyz.atomdev.cduels.handler.ArenaHandler;
import xyz.atomdev.cduels.handler.DuelHandler;
import xyz.atomdev.cduels.handler.KitHandler;
import xyz.atomdev.cduels.handler.ProfileHandler;
import xyz.atomdev.cduels.listeners.DuelListener;
import xyz.atomdev.cduels.listeners.ProfileListener;
import xyz.atomdev.cduels.util.ConfigFile;
import xyz.atomdev.cduels.util.menu.MenuAPI;

@Getter
public final class CDuels extends JavaPlugin {

    @Getter private static CDuels instance;

    private ConfigFile arenaFile, kitFile;
    private SQLDatabase database;

    private ProfileHandler profileHandler;
    private DuelHandler duelHandler;
    private ArenaHandler arenaHandler;
    private KitHandler kitHandler;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        arenaFile = new ConfigFile(this, "arenas.yml");
        kitFile = new ConfigFile(this, "kits.yml");
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadObjects();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        profileHandler.save(false);
        instance = null;
    }

    private void loadObjects() {
        this.database = new SQLDatabase(this);
        this.profileHandler = new ProfileHandler(this);
        this.duelHandler = new DuelHandler(this);
        this.arenaHandler = new ArenaHandler(this);
        this.kitHandler = new KitHandler(this);
    }

    private void registerListeners() {
        new ProfileListener(this);
        new DuelListener(this);
        new MenuAPI(this);
    }

    private void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        commandManager.registerCommand(new ArenaCommands(this));
        commandManager.registerCommand(new DuelCommands(this));
        commandManager.registerCommand(new KitCommands(this));
        commandManager.registerCommand(new SetEndLocationCommand(this));
    }
}