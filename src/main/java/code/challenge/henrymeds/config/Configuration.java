package code.challenge.henrymeds.config;

public class Configuration {

    private int maxThreads;
    private int minThreads;
    private int threadTimeout;
    private int port;
    private String dbConnectionString;

    public Configuration(){}



    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(int minThreads) {
        this.minThreads = minThreads;
    }

    public int getThreadTimeout() {
        return threadTimeout;
    }

    public void setThreadTimeout(int threadTimeout) {
        this.threadTimeout = threadTimeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbConnectionString() {
        return dbConnectionString;
    }

    public void setDbConnectionString(String dbConnectionString) {
        this.dbConnectionString = dbConnectionString;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "maxThreads=" + maxThreads +
                ", minThreads=" + minThreads +
                ", threadTimeout=" + threadTimeout +
                ", port=" + port +
                ", dbConnectionString='" + dbConnectionString + '\'' +
                '}';
    }
}
