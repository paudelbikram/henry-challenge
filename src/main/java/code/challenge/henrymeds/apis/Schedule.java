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

public class Schedule extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Schedule.class);


    // Assuming Provider will call this
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        LOGGER.info("Got request: Add to Schedule");
        LOGGER.info("Request : {}", request);
        String day = request.getParameter("day");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        try (Connection conn = DriverManager.getConnection(ConfigService.getConfiguration().getDbConnectionString())) {

            // create a connection to the database
            final String insertIntoAvailability = "INSERT INTO Availability (day, providerid, fromtime, totime, fullyreserved) "
                    + "	VALUES (?,(SELECT Id From Provider LIMIT 1),?, ?, 0) ";

            PreparedStatement pstmt = conn.prepareStatement(insertIntoAvailability);
            pstmt.setString(1, day);
            pstmt.setDouble(2, Double.parseDouble(from));
            pstmt.setDouble(3, Double.parseDouble(to));
            pstmt.executeUpdate();
            response.setStatus(201);
            response.getWriter().print("Schedule successfully saved.");
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            response.setStatus(500);
            response.getWriter().print("Server error. Failed to save schedule.");
        }
    }


    // Assuming client will call this doGet
    protected void doGet(HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        LOGGER.info("Got request: Get Schedules");
        LOGGER.info("Request : {}", request);
        List<Availability> availabilityList = new ArrayList<>();
        String sql = "SELECT id, day, providerid, fromtime, totime "
                + "FROM Availability WHERE fullyreserved = 0 ";

        try (Connection conn = DriverManager.getConnection(ConfigService.getConfiguration().getDbConnectionString());
             PreparedStatement pstmt  = conn.prepareStatement(sql)
        ) {
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                availabilityList.add(new Availability(rs.getInt("id"),
                        rs.getInt("providerid"), rs.getString("day"),
                        rs.getDouble("fromtime"), rs.getDouble("totime")));
            }
            response.setStatus(200);
            response.getWriter().print(new Gson().toJson(availabilityList));
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            response.setStatus(500);
            response.getWriter().print("Server error. Failed to get schedules.");
        }
    }
}
