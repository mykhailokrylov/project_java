package fish.api.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class DatabaseService {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final Logger logger = Logger.getLogger(DatabaseService.class.getName());

    static {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("/app/.env")) {
            props.load(input);
        } catch (Exception e) {
            logger.severe("Failed to load database credentials from .env file: " + e.getMessage());
            throw new RuntimeException("Failed to load database credentials from .env file");
        }
        URL = "jdbc:postgresql://database:" + props.getProperty("POSTGRES_PORT") + "/" + props.getProperty("POSTGRES_DB");
        USER = props.getProperty("POSTGRES_USER");
        PASSWORD = props.getProperty("POSTGRES_PASSWORD");
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void executeQuery(String query, List<SimpleEntry<String, Object>> params) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i).getValue());
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("SQL Exception: " + e.getMessage());
        }
    }
}
