package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creates database connections.
 * Settings come from environment variables (DB_URL, DB_USER, DB_PASSWORD)
 * or from a db.properties file, so no password is written in the source code.
 */
public final class DBConnection {

    private static final String CONFIG_FILE = "db.properties";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Properties fileSettings = loadFileSettings();

        String url = readSetting("DB_URL", "db.url", fileSettings);
        String user = readSetting("DB_USER", "db.user", fileSettings);
        String password = readSetting("DB_PASSWORD", "db.password", fileSettings);

        if (url == null || user == null || password == null) {
            throw new SQLException("Database settings not found. Create db.properties "
                    + "(copy db.properties.example) or set DB_URL, DB_USER and DB_PASSWORD.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadFileSettings() {
        Properties settings = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            settings.load(input);
        } catch (IOException e) {
            // No file is fine: the environment variables may be used instead.
        }
        return settings;
    }

    private static String readSetting(String envName, String fileKey, Properties fileSettings) {
        String value = System.getenv(envName);
        if (value == null || value.isEmpty()) {
            value = fileSettings.getProperty(fileKey);
        }
        return value;
    }
}
