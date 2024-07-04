package xyz.atomdev.cduels.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import xyz.atomdev.cduels.CDuels;

import java.sql.Connection;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@Getter
public class SQLDatabase {

    private HikariDataSource dataSource = null;
    private Connection connection = null;

    public SQLDatabase(CDuels instance) {
        try {
            HikariConfig config = new HikariConfig();
            ConfigurationSection configuration = instance.getConfig().getConfigurationSection("database");

            String host = configuration.getString("host");
            String port = configuration.getString("port");
            String databaseName = configuration.getString("database");
            String username = configuration.getString("username");
            String password = configuration.getString("password");

            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + databaseName);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);

            this.dataSource = new HikariDataSource(config);
            this.connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
