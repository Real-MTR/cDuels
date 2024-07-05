package xyz.atomdev.cduels.handler;

import lombok.Getter;
import lombok.SneakyThrows;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.profile.Profile;
import xyz.atomdev.cduels.util.SerializationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@Getter
public class ProfileHandler {

    private final CDuels instance;
    private final Map<String, Profile> profileMap;

    public ProfileHandler(CDuels instance) {
        this.instance = instance;
        this.profileMap = new ConcurrentHashMap<>();

        this.createTable();
        this.load();
    }

    @SneakyThrows
    public void load() {
        String query = "SELECT * FROM profiles";

        try (Connection connection = instance.getDatabase().getDataSource().getConnection();
             Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");

                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                int gamesPlayed = resultSet.getInt("gamesPlayed");

                String items = resultSet.getString("lastItems");
                String armor = resultSet.getString("lastArmor");
                String effects = resultSet.getString("lastEffects");

                Profile profile = new Profile(name,
                        wins,
                        losses,
                        gamesPlayed,
                        SerializationUtil.deserializeItems(items),
                        SerializationUtil.deserializeItems(armor),
                        SerializationUtil.deserializeEffects(effects));

                profileMap.put(name, profile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(boolean reset) {
        reset();
        if(reset) return;

        for (Profile profile : profileMap.values()) {
            add(profile);
        }
    }

    public void add(Profile profile) {
        String query = "INSERT INTO profiles (name, wins, losses, gamesPlayed, lastItems, lastArmor, lastEffects) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = instance.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, profile.getName());

            statement.setInt(2, profile.getWins());
            statement.setInt(3, profile.getLosses());
            statement.setInt(4, profile.getGamesPlayed());

            statement.setString(5, SerializationUtil.serializeItems(profile.getLastItems()));
            statement.setString(6, SerializationUtil.serializeItems(profile.getLastArmor()));
            statement.setString(7, SerializationUtil.serializeEffects(new ArrayList<>(profile.getLastEffects())));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        String query = "DELETE FROM profiles";

        try (Connection connection = instance.getDatabase().getDataSource().getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS profiles (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "wins INT NOT NULL," +
                "losses INT NOT NULL," +
                "gamesPlayed INT NOT NULL," +
                "lastItems VARCHAR(255) NOT NULL," +
                "lastArmor VARCHAR(255) NOT NULL," +
                "lastEffects VARCHAR(255) NOT NULL" +
                ")";

        try (Connection connection = instance.getDatabase().getDataSource().getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}