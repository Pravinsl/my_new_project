package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uemail = request.getParameter("username");
        String upwd = request.getParameter("password");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;

        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
           // Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/signup?useSSL=false", "root", "root");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/signup?useSSL=false&allowPublicKeyRetrieval=true", "root", "root");

            // Prepare SQL query
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE uemail = ? AND upwd = ?");
            pst.setString(1, uemail);
            pst.setString(2, upwd);

            // Execute query
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Successful login
                session.setAttribute("name", rs.getString("uname"));
                dispatcher = request.getRequestDispatcher("index.jsp");
            } else {
                // Failed login
                request.setAttribute("status", "failed");
                dispatcher = request.getRequestDispatcher("login.jsp");
            }

            // Forward to the appropriate page
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
