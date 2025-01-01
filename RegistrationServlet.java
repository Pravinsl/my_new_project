package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistartionServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Get form parameters
        String uname = request.getParameter("name");
        String umail = request.getParameter("email");
        String upwd = request.getParameter("pass");
        String umobile = request.getParameter("contact");

        // Initialize request dispatcher
        RequestDispatcher dispatcher = null;
        Connection con = null;

        try {
            // Load MySQL driver (You can omit this if you are using JDBC 4.0 or later)
            Class.forName("com.mysql.cj.jdbc.Driver"); // Updated driver for newer MySQL versions

            // Establish connection to the database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/signup?useSSL=false", "root", "root");

            // Prepare the SQL INSERT statement
            String sql = "INSERT INTO users (uname, upwd, uemail, umobile) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, uname);
            pst.setString(2, upwd);
            pst.setString(3, umail);  // Use umail, not uemail
            pst.setString(4, umobile);

            // Execute the query
            int rowCount = pst.executeUpdate();

            // Prepare the response based on success or failure
            dispatcher = request.getRequestDispatcher("registration.jsp"); // Forward to a JSP page
            if (rowCount > 0) {
                request.setAttribute("status", "success"); // Set success attribute for the JSP page
            } else {
                request.setAttribute("status", "failed"); // Set failure attribute for the JSP page
            }

        } catch (Exception e) {
            // Handle exceptions and print stack trace
            e.printStackTrace();
            request.setAttribute("status", "error"); // In case of an error, set the status to 'error'
        } finally {
            // Ensure the connection is closed after use
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Forward the request and response to the appropriate page
        dispatcher.forward(request, response);
    }
}
