package code.challenge.henrymeds.om;

public class Availability {
    int id;
    int providerid;
    String day;
    double fromtime;
    double totime;

    public Availability(int id, int providerid, String day, double fromtime, double totime) {
        this.id = id;
        this.providerid = providerid;
        this.day = day;
        this.fromtime = fromtime;
        this.totime = totime;
    }
}
