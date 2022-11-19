package code.challenge.henrymeds;

import code.challenge.henrymeds.apis.Confirm;
import code.challenge.henrymeds.apis.Reserve;
import code.challenge.henrymeds.apis.Schedule;
import code.challenge.henrymeds.config.ConfigService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyServer {
    private Server server;
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);

    public void start() throws  Exception
    {
        LOGGER.info("Starting Jetty Server");
        int maxThreads = ConfigService.getConfiguration().getMaxThreads();
        int minThreads = ConfigService.getConfiguration().getMinThreads();
        int idleTimeout = ConfigService.getConfiguration().getThreadTimeout();
        LOGGER.info("Setting maxThreads: {}, minThread: {} and idleTimeout: {}",maxThreads, minThreads, idleTimeout);


        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(ConfigService.getConfiguration().getPort());
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(Schedule.class, "/schedule");
        servletHandler.addServletWithMapping(Reserve.class, "/reserve");
        servletHandler.addServletWithMapping(Confirm.class, "/confirm");
        server.start();
    }

    public void stop() throws Exception {
        LOGGER.warn("Stopping Jetty Server");
        server.stop();
    }
}
