package xyz.atomdev.cduels.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import xyz.atomdev.cduels.model.profile.Profile;
import xyz.atomdev.cduels.util.LeaderboardUtil;

import java.util.Comparator;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "cduels";
    }

    @Override
    public String getAuthor() {
        return "EmpireMTR";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        switch (params.toLowerCase()) {
            case "top_wins":
                return fromListToString(LeaderboardUtil.getTop5Profiles(Comparator.comparing(Profile::getWins), "Wins", "wins"));
            case "top_losses":
                return fromListToString(LeaderboardUtil.getTop5Profiles(Comparator.comparing(Profile::getWins), "Losses", "losses"));
            case "top_gamesplayed":
                return fromListToString(LeaderboardUtil.getTop5Profiles(Comparator.comparing(Profile::getWins), "Games Played", "gamesPlayed"));
        }

        return super.onPlaceholderRequest(player, params);
    }

    private String fromListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        list.forEach(builder::append);

        return builder.toString();
    }
}
