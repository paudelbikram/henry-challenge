package code.challenge.henrymeds.apis;

import code.challenge.henrymeds.config.ConfigService;
import code.challenge.henrymeds.om.Availability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Reserve extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reserve.class);


    // Assuming Client will call this
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        LOGGER.info("Got request: Reserve availability");
        LOGGER.info("Request : {}", request);
        String availabilityid = request.getParameter("availabilityid");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        try (Connection conn = DriverManager.getConnection(ConfigService.getConfiguration().getDbConnectionString())) {

            // create a connection to the database
            final String insertIntoAvailability = "INSERT INTO Reservation (availabilityid, clientid, fromtime, totime, reserved, confirmed) "
                    + "	VALUES (?,(SELECT Id From Client LIMIT 1),?, ?,1, 0) ";
            final String getLastInserted = "select last_insert_rowid() as last";
            PreparedStatement pstmt = conn.prepareStatement(insertIntoAvailability);
            pstmt.setInt(1, Integer.parseInt(availabilityid));
            pstmt.setDouble(2, Double.parseDouble(from));
            pstmt.setDouble(3, Double.parseDouble(to));
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(getLastInserted);
            ResultSet rs  = pstmt.executeQuery();
            int reservationId = rs.getInt("last");
            response.setStatus(201);
            response.getWriter().print("Availability successfully reserved. ReservationId :"+ reservationId);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            response.setStatus(500);
            response.getWriter().print("Server error. Failed to reserve Availability.");
        }
    }
}
