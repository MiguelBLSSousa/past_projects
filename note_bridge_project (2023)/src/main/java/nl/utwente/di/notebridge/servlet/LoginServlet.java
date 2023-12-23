package nl.utwente.di.notebridge.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class LoginServlet extends HttpServlet {
    private String host = "bronto.ewi.utwente.nl";
    private String dbName = "dab_dsgnprj_46";
    private String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
    private static final String DB_PASSWORD = "dmMvWjVh747KpixR";

    public String hashPassword(String pass, String salt){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
            byte[] passBytes = pass.getBytes(StandardCharsets.UTF_8);

            byte[] combinedBytes = new byte[saltBytes.length + passBytes.length];
            System.arraycopy(saltBytes, 0, combinedBytes, 0, saltBytes.length);
            System.arraycopy(passBytes, 0, combinedBytes, saltBytes.length, passBytes.length);

            byte[] hash = digest.digest(combinedBytes);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        // Retrieve form data
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || email.equals("")) {
            response.sendRedirect("/error.html?msg=Empty data while logging in");
            return;
        }

        // Insert data into the database

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }

        try (Connection connection = DriverManager.getConnection(url, dbName, DB_PASSWORD)) {
            // Insert address
            String loginQuery = "SELECT p.pid, p.password, p.salt, t.tid, s.sid " +
                                "FROM notebridge2.person p " +
                                "LEFT JOIN notebridge2.teacher t ON p.pid = t.pid " +
                                "LEFT JOIN notebridge2.student s ON p.pid = s.pid " +
                                "WHERE p.email = ?";
            PreparedStatement addressStatement = connection.prepareStatement(loginQuery);
            addressStatement.setString(1, email);
            ResultSet result = addressStatement.executeQuery();

            if(!result.next()) {
                response.sendRedirect("./login.jsp?msg=Email and/or Password not correct!");
                System.out.println(addressStatement);
                return;
            }

            int pid = result.getInt(1);
            String passwordCheck = result.getString(2);
            String saltCheck = result.getString(3);
            int tid = result.getInt(4);
            int sid = result.getInt(5);
            String passwordHash = hashPassword(password,saltCheck);
            if(!passwordCheck.equals(passwordHash)){
                response.sendRedirect("./login.jsp?msg=Email and/or Password not correct!");
                System.out.println(addressStatement);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("pid", pid);
            session.setAttribute("tid", tid);
            session.setAttribute("sid", sid);

            response.sendRedirect("./profile.jsp");
        } catch (SQLException e) {
            response.sendRedirect("./login.jsp?msg=An error occurred while trying to login");
            e.printStackTrace();
        }
    }
}
