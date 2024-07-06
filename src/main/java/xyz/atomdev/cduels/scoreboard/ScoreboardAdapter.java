package xyz.atomdev.cduels.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.duel.state.DuelState;
import xyz.atomdev.cduels.model.profile.Profile;
import xyz.atomdev.cduels.util.CC;
import xyz.atomdev.cduels.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@RequiredArgsConstructor
public class ScoreboardAdapter implements AssembleAdapter {

    private final CDuels instance;

    @Override
    public String getTitle(Player player) {
        Profile profile = instance.getProfileHandler().getProfileMap().get(player.getName());
        return CC.colorize(profile.isInDuel() ? instance.getConfig().getString("scoreboard.in-duel.title")
                :
                instance.getConfig().getString("scoreboard.no-duel.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> list = new ArrayList<>();
        Profile profile = instance.getProfileHandler().getProfileMap().get(player.getName());

        if(profile.isInDuel() && profile.getCurrentDuel().getState() != DuelState.ENDING) {
            for (String str : instance.getConfig().getStringList("scoreboard.in-duel.lines")) {
                list.add(CC.colorize(str
                        .replace("<opponent>", profile.getCurrentDuel().getOpponent(profile).getName())
                        .replace("<duration>", formatDuration(profile.getCurrentDuel().getDuration()))
                        .replace("<ping>", String.valueOf(getPlayerPing(player)))
                        .replace("<opponent_ping>", String
                                .valueOf(getPlayerPing(instance.getServer().getPlayer(profile.getCurrentDuel().getOpponent(profile).getName()))))));
            }
        } else {
            for (String str : instance.getConfig().getStringList("scoreboard.no-duel.lines")) {
                list.add(CC.colorize(str));
            }
        }

        return list;
    }

    @Override
    public List<World> getAllowedWorlds(Player player) {
        List<World> list = new ArrayList<>();
        instance.getConfig().getStringList("scoreboard.allowed-worlds-names").forEach(str -> list.add(instance.getServer().getWorld(str)));

        return list;
    }

    // In case the server doesn't have player.getPing() method.
    private int getPlayerPing(Player player) {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + ReflectionUtils.VERSION + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object entityPlayer = craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);

            Field pingField = entityPlayer.getClass().getField("ping");
            return pingField.getInt(entityPlayer);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }

        if (minutes > 0 || hours > 0) {
            formattedTime.append(minutes).append("m ");
        }

        formattedTime.append(remainingSeconds).append("s");

        return formattedTime.toString();
    }
}