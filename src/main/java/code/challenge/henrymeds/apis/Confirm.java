package code.challenge.henrymeds.apis;

import code.challenge.henrymeds.config.ConfigService;
import code.challenge.henrymeds.om.Availability;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Confirm extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Schedule.class);


    // Assuming Client will call this
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        LOGGER.info("Got request: Confirm Reservation");
        LOGGER.info("Request : {}", request);
        String reservationid = request.getParameter("reservationid");

        try (Connection conn = DriverManager.getConnection(ConfigService.getConfiguration().getDbConnectionString())) {
            // create a connection to the database
            final String updateReservation = " UPDATE Reservation SET confirmed = 1 WHERE Id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateReservation);
            pstmt.setInt(1, Integer.parseInt(reservationid));
            pstmt.executeUpdate();
            response.setStatus(204);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            response.setStatus(500);
            response.getWriter().print("Server error. Failed to confirm reservation.");
        }
    }
}
