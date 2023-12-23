package nl.utwente.di.notebridge.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import nl.utwente.di.notebridge.FileSaver;
import sql.Database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@MultipartConfig
public class RegisterServlet extends HttpServlet {
    private String host = "bronto.ewi.utwente.nl";
    private String dbName = "dab_dsgnprj_46";
    private String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
    private static final String DB_PASSWORD = "dmMvWjVh747KpixR";


    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

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
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String firstname = request.getParameter("firstname");
        String surname = request.getParameter("surname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String salt = generateSalt();
        String password = hashPassword(request.getParameter("password"),salt);
        String street = request.getParameter("street");
        String house = request.getParameter("housenumber");
        String city = request.getParameter("city");
        String zip = request.getParameter("zip");
        String type = request.getParameter("accounttype");
        String rate = request.getParameter("rate");
        String instruments = request.getParameter("instruments");
        List<String> instrumentsList = Arrays.asList(instruments.split(","));

        // TODO: add checks for data

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }

        // Insert data into the database
        try (Connection connection = DriverManager.getConnection(url, dbName, DB_PASSWORD)) {
            // Insert address
            try {
                String addressQuery = "INSERT INTO notebridge2.address VALUES (?, ?, ?, ?)";
                PreparedStatement addressStatement = connection.prepareStatement(addressQuery);
                addressStatement.setInt(1, Integer.parseInt(house));
                addressStatement.setString(2, zip);
                addressStatement.setString(3, street);
                addressStatement.setString(4, city);
                addressStatement.executeUpdate();
            } catch (SQLException e){
                System.out.println("dont have to add since already exists");
            }


            // Insert person & teacher/student
            String personQuery = "INSERT INTO notebridge2.person(first_name, surname, phone_nr, email, postal_code, house_nr, password, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement personStatement = connection.prepareStatement(personQuery, Statement.RETURN_GENERATED_KEYS);
            personStatement.setString(1, firstname);
            personStatement.setString(2, surname);
            personStatement.setString(3, phone);
            personStatement.setString(4, email);
            personStatement.setString(5, zip);
            personStatement.setInt(6, Integer.parseInt(house));
            personStatement.setString(7, password);
            personStatement.setString(8,salt);
            personStatement.executeUpdate();
            ResultSet generatedKeys = personStatement.getGeneratedKeys();
            generatedKeys.next();
            int pid = generatedKeys.getInt(1);
            if (type.equals("student")){
                String studentQuery = "INSERT INTO notebridge2.student(pid) VALUES (?)";
                PreparedStatement studentStatement = connection.prepareStatement(studentQuery, Statement.RETURN_GENERATED_KEYS);
                studentStatement.setInt(1, pid);
                studentStatement.executeUpdate();
                ResultSet generatedKeys2 = studentStatement.getGeneratedKeys();
                generatedKeys2.next();
                int sid = generatedKeys2.getInt(1);
                if(!instrumentsList.isEmpty()){
                    for(String instrument : instrumentsList){
                        String studentInstrumentQuery = "INSERT INTO notebridge2.si_association(sid, instrument) VALUES (?, ?)";
                        PreparedStatement studentInstrumentStatement = connection.prepareStatement(studentInstrumentQuery);
                        studentInstrumentStatement.setInt(1,sid);
                        studentInstrumentStatement.setString(2,instrument);
                        studentInstrumentStatement.executeUpdate();
                    }
                }
            } else if(type.equals("teacher")) {
                String teacherQuery = "INSERT INTO notebridge2.teacher(pid, rate) VALUES (?, ?)";
                PreparedStatement teacherStatement = connection.prepareStatement(teacherQuery, Statement.RETURN_GENERATED_KEYS);
                teacherStatement.setInt(1, pid);
                teacherStatement.setDouble(2, Double.parseDouble(rate));
                teacherStatement.executeUpdate();
                ResultSet generatedKeys3 = teacherStatement.getGeneratedKeys();
                generatedKeys3.next();
                int tid = generatedKeys3.getInt(1);
                if (!instrumentsList.isEmpty()) {
                    for (String instrument : instrumentsList) {
                        String teacherInstrumentQuery = "INSERT INTO notebridge2.ti_association(tid, instrument) VALUES (?, ?)";
                        PreparedStatement teacherInstrumentStatement = connection.prepareStatement(teacherInstrumentQuery);
                        teacherInstrumentStatement.setInt(1, tid);
                        teacherInstrumentStatement.setString(2, instrument);
                        teacherInstrumentStatement.executeUpdate();
                    }
                }
            }

            // Saving the profile picture
            Part filePart = request.getPart("picture");
            String filePath = FileSaver.saveProfilePicture(filePart, getServletContext().getInitParameter("uploadlocation"), pid);
            if (filePath == null) {
                filePath = "/img/sidebarmenu.png";
            }

            Database database = new Database();
            database.updateProfilePicture(pid, filePath);
            database.close();

            response.sendRedirect("./login.jsp");
        } catch (SQLException e) {
            out.println("Error inserting data: " + e.getMessage());
        }
    }
}
