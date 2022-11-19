import code.challenge.henrymeds.JettyServer;
import code.challenge.henrymeds.apis.Schedule;
import code.challenge.henrymeds.config.ConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class TestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestApi.class);

    private static JettyServer jettyServer;

    @BeforeClass
    public static void setup() throws Exception {
        ConfigService.init();
        jettyServer = new JettyServer();
        jettyServer.start();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        jettyServer.stop();
    }


    @Test
    public void testOverallSchedule() throws Exception {
        addToSchedule("2022-12-01", "8.00", "17.00");
        addToSchedule("2022-12-05", "7.00", "12.00");
        addToSchedule("2022-12-01", "9.00", "10.00");
        getSchedule();
    }

    @Test
    public void testOverallReservation() throws Exception {
        addToSchedule("2022-12-01", "8.00", "17.00");
        addToSchedule("2022-12-05", "7.00", "12.00");
        addToSchedule("2022-12-01", "9.00", "10.00");
        reserveSchedule("1");
        reserveSchedule("2");
        reserveSchedule("3");
    }


    @Test
    public void testOverallConfirmation() throws Exception {
        addToSchedule("2022-12-01", "8.00", "17.00");
        addToSchedule("2022-12-05", "7.00", "12.00");
        addToSchedule("2022-12-01", "9.00", "10.00");
        reserveSchedule("1");
        reserveSchedule("2");
        reserveSchedule("3");
        confirmReservation("1");
        confirmReservation("2");
        confirmReservation("3");
    }

    private void confirmReservation(String reservationId) throws Exception {
        String url = "http://localhost:8080/confirm";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("reservationid", reservationId));
        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(request);
        LOGGER.info("Response :{}", response);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(204);
    }

    private void reserveSchedule(String availabilityId) throws Exception {
        String url = "http://localhost:8080/reserve";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("availabilityid", availabilityId));
        params.add(new BasicNameValuePair("from", "8.30"));
        params.add(new BasicNameValuePair("to", "11.00"));
        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(request);
        LOGGER.info("Response :{}", response);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
        String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        LOGGER.info("ResponseContent :{}", responseContent);
        assertThat(responseContent!=null).isEqualTo(true);
    }


    private void addToSchedule(String day, String from, String to) throws Exception {
        String url = "http://localhost:8080/schedule";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("day", day));
        params.add(new BasicNameValuePair("from", from));
        params.add(new BasicNameValuePair("to", to));
        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(request);
        LOGGER.info("Response :{}", response);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

    private void getSchedule() throws Exception {
        String url = "http://localhost:8080/schedule";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        LOGGER.info("Response :{}", response);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        LOGGER.info("ResponseContent :{}", responseContent);
        assertThat(responseContent!=null).isEqualTo(true);
    }





}
