package code.challenge.henrymeds;

import code.challenge.henrymeds.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args){
        try {
            LOGGER.info("Starting app...");
            ConfigService.init();

            JettyServer server = new JettyServer();
            server.start();
        } catch (Exception e) {
            LOGGER.error("Error Occurred", e);
            System.exit(1);
        }
    }
}
