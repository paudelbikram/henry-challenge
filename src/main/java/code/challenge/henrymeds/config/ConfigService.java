package code.challenge.henrymeds.config;

import code.challenge.henrymeds.JettyServer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigService {
    private static Configuration configuration = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);

    private ConfigService(){}

    private static void testConnection() throws SQLException {
        try (Connection conn = DriverManager.getConnection(configuration.getDbConnectionString())) {
            // create a connection to the database
            LOGGER.info("Connection to SQLite has been established.");
        }
    }


    // For testing purpose
    private static void seedDb() throws SQLException, IOException {
        String dbFilePath = configuration.getDbConnectionString().split("sqlite:")[1];
        Files.deleteIfExists(Paths.get(dbFilePath));
        try (Connection conn = DriverManager.getConnection(configuration.getDbConnectionString())) {
            // create a connection to the database
            LOGGER.info("Now Seeding Database");
            final String createClientTable = "CREATE TABLE Client (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL\n"
                    + ");";
            final String createProviderTable = "CREATE TABLE Provider (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL\n"
                    + ");";

            final String createAvailabilityTable = "CREATE TABLE Availability (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	day text NOT NULL,\n"
                    + " providerid integer, \n"
                    + " fromtime real, \n"
                    + " totime real, \n"
                    + " fullyreserved integer, \n"
                    + " FOREIGN KEY(providerid) REFERENCES Provider(id) \n"
                    + ");";


            final String createReservationTable = "CREATE TABLE Reservation (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	availabilityid integer NOT NULL,\n"
                    + " fromtime real, \n"
                    + " totime real, \n"
                    + " clientid integer, \n"
                    + " reserved integer, \n"
                    + " confirmed integer, \n"
                    + " FOREIGN KEY(clientid) REFERENCES Client(id), \n"
                    + " FOREIGN KEY(availabilityid) REFERENCES Availability(id) \n"
                    + ");";

            final String insertIntoClient = "INSERT INTO Client(name) "
                    + " values ('TestClient')";
            final String insertIntoProvider = "INSERT INTO Provider(name) "
                    + " values ('TestProvider')";

            Statement stmt = conn.createStatement();
            stmt.execute(createClientTable);
            stmt.execute(createProviderTable);
            stmt.execute(createAvailabilityTable);
            stmt.execute(createReservationTable);
            stmt.execute(insertIntoClient);
            stmt.execute(insertIntoProvider);
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void init() throws Exception {
        if (configuration == null) {
            final Path configPath = Paths.get( "./config/config.json");
            try (final BufferedReader reader = Files.newBufferedReader(configPath))
            {
                configuration = new Gson().fromJson(reader, Configuration.class);
                LOGGER.info("Configuration has been read: {}",configuration );
                testConnection();
                seedDb();
            } catch (IOException e) {
                LOGGER.error("Cound not initialized the Configuraton with file: {}", configPath);
                LOGGER.error(e.toString());
                throw new Exception("Failed to initialize config");
            }
        }
    }

}
