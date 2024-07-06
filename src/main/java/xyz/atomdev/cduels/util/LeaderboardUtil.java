package xyz.atomdev.cduels.util;

import lombok.experimental.UtilityClass;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.profile.Profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @project cDuels is a property of MTR
 * @date 7/6/2024
 */

@UtilityClass
public class LeaderboardUtil {

    public List<String> getTop5Profiles(Comparator<Profile> comparator, String key, String result) {
        List<Profile> top5Profiles = CDuels.getInstance().getProfileHandler().getProfileMap().values().stream()
                .sorted(comparator.reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<String> list = new ArrayList<>();

        int i = 0;
        for (Profile profile : top5Profiles) {
            i++;

            String format = "&f&l#" + i + " &b" + profile.getName() + " " + key + ": &f";
            String finalFormat;

            switch (result.toLowerCase()) {
                case "wins":
                    finalFormat = format + profile.getWins();
                    break;
                case "losses":
                    finalFormat = format + profile.getLosses();
                    break;
                case "gamesPlayed":
                    finalFormat = format + profile.getGamesPlayed();
                    break;
                default:
                    return new ArrayList<>();
            }

            list.add(finalFormat);
        }

        return list;
    }
}
