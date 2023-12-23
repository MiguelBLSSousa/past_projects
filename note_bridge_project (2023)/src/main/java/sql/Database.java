package sql;

import nl.utwente.di.notebridge.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Database {

    public static final String SPLIT_REGEX = "_-_";

    private String host;
    private String dbName;
    private String url;
    private Statement statement;
    private Connection connection;


    public Database() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }
        host = "bronto.ewi.utwente.nl";
        dbName = "dab_dsgnprj_46";
        url = "jdbc:postgresql://" + host + ":5432/" + dbName;

        try {
            connection = DriverManager.getConnection(url, dbName, "dmMvWjVh747KpixR");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addAddress(int house_nr, String postal_code, String street, String city) {
        String query = "INSERT INTO notebridge2.address " +
                "VALUES(" + house_nr + ", '" + postal_code + "', '" + street + "', '" + city + "')";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public void addPerson(int pid, String first_name, String surname, String phone_nr, String email, String postal_code, int house_nr, String password) {
        String query = "INSERT INTO notebridge2.person " +
                "VALUES(" + pid + ", '" + first_name + "', '" + surname + "', '" + phone_nr + "', '" + email + "', '" + postal_code + "', " + house_nr + ", '" + password + "')";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public void addStudent(int sid, int pid) {
        String query = "INSERT INTO notebridge2.student " +
                "VALUES(" + sid + ", " + pid + ")";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");

        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public void addTeacher(int tid, double rating, int pid, int rate) {
        String query = "INSERT INTO notebridge2.teacher " +
                "VALUES(" + tid + ", " + rating + ", " + pid + ", " + rate + ")";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public void addInstrument(String name) {
        String query = "INSERT INTO notebridge2.instrument " +
                "VALUES('" + name + "')";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public void addTIAssociation(int tid, String instrument) {
        String query = "INSERT INTO notebridge2.ti_association " +
                "VALUES(" + tid + ", '" + instrument + "')";
        try {
            statement.executeUpdate(query);
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        generateNewData();
        //deleteData();
    }

    private static void generateNewData() {
        Database database = new Database();
        for (String instrument : RandomDataGenerator.INSTRUMENTS) {
            database.addInstrument(instrument);
        }
        for (RandomDataGenerator.TeacherData data : RandomDataGenerator.generateData()) {
            database.addAddress(data.getHouseNr(), data.getZipCode(), data.getStreet(), data.getCity());
            database.addPerson(data.getCurrentPersonId(), data.getFirstName(), data.getSurname(), data.getPhoneNr(), data.getEmail(), data.getZipCode(), data.getHouseNr(), data.getPassword());
            database.addTeacher(data.getCurrentTeacherId(), data.getRating(), data.getCurrentPersonId(), data.getRate());
            for (String instrument : data.getInstruments()) {
                database.addTIAssociation(data.getCurrentTeacherId(), instrument);
            }
        }
        database.close();
    }

    private static void deleteData() {
        Database database = new Database();
        database.deleteAll();
        database.close();
    }

    public void deleteAll() {
        String persons = "TRUNCATE TABLE notebridge2.address, notebridge2.person, notebridge2.teacher, notebridge2.student, notebridge2.si_association, notebridge2.ti_association CASCADE; ";
        try {
            statement.execute(persons);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<TeacherProfile> getTeachers() {
        try {
            List<TeacherProfile> teachers = new ArrayList<>();
            String query = "SELECT t.tid, p.first_name, p.surname, a.city, a.postal_code, ROUND(AVG(r.score), 2), t.rate, string_agg(DISTINCT ti.instrument, ', '), " +
                    "ARRAY_AGG(DISTINCT CONCAT(s.dayofweek, '" + SPLIT_REGEX + "', s.weekofyear, '" + SPLIT_REGEX + "', s.year, '" + SPLIT_REGEX + "', s.start_time, '" + SPLIT_REGEX + "', s.end_time)), " +
                    "p.picture, t.video " +
                    "FROM notebridge2.person p, notebridge2.address a, notebridge2.ti_association ti, notebridge2.teacher t " +
                    "LEFT JOIN (SELECT * FROM notebridge2.schedule " +
                    "WHERE weekofyear = ? " +
                    "AND year = ? ) s ON t.tid = s.teacher " +
                    "LEFT JOIN notebridge2.review r ON r.tid = t.tid " +
                    "WHERE p.pid = t.pid AND (a.house_nr = p.house_nr AND a.postal_code = p.postal_code) " +
                    "AND t.tid = ti.tid " +
                    "GROUP BY t.tid, p.first_name, p.surname, a.city, a.postal_code, t.rate, p.picture; ";
            System.out.println(query);
            PreparedStatement pre = connection.prepareStatement(query);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            pre.setInt(1, cal.get(Calendar.WEEK_OF_YEAR));
            pre.setInt(2, cal.get(Calendar.YEAR));
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                teachers.add(new TeacherProfile(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getDouble(6),
                        result.getInt(7),
                        Arrays.asList(result.getString(8).split(", ")),
                        Availability.toAvailabilitiesList(result.getArray(9)),
                        result.getString(10),
                        result.getString(11)));
            }

            return teachers;
        } catch (SQLException sqle) {
            System.err.println("Error connecting1: " + sqle);
            sqle.printStackTrace();
            return null;
        }
    }

    public String getStudentName(int sid){
        try {
            String query = "SELECT CONCAT(p.first_name, ' ', p.surname) " +
                    "FROM notebridge2.person p, notebridge2.student s " +
                    "WHERE s.pid = p.pid AND s.sid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, sid);
            ResultSet result = pre.executeQuery();
            System.out.println(query);
            result.next();
            return result.getString(1);
        } catch (SQLException sqle) {
            System.err.println("Error connecting2: " + sqle);
        }
        return null;
    }
    public String getTeacherName(int tid){
        try {
            String query = "SELECT CONCAT(p.first_name, ' ', p.surname) " +
                    "FROM notebridge2.person p, notebridge2.teacher t " +
                    "WHERE t.pid = p.pid AND t.tid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            ResultSet result = pre.executeQuery();
            System.out.println(query);
            result.next();
            return result.getString(1);
        } catch (SQLException sqle) {
            System.err.println("Error connecting2: " + sqle);
        }
        return null;
    }


    public List<Review> getReviews(int tid) {
        try {
            List<Review> reviews = new ArrayList<>();
            String query = "SELECT r.message, CONCAT(p.first_name, ' ', p.surname) AS reviewer, r.score " +
                    "FROM notebridge2.review r, notebridge2.person p, notebridge2.student s " +
                    "WHERE r.tid = ? " +
                    "AND r.sid = s.sid AND s.pid = p.pid " +
                    "ORDER BY r.score";
            System.out.println(query);
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                reviews.add(new Review(result.getString(1),
                        result.getString(2),
                        result.getInt(3)));
            }
            return reviews;
        } catch (SQLException sqle) {
            System.err.println("Error connecting3: " + sqle);
            sqle.printStackTrace();
            return null;
        }
    }

    public TeacherProfile getTeacher(int tid) {
        try {
            String query = "SELECT t.tid, p.first_name, p.surname, a.city, ROUND(AVG(r.score), 2), t.rate, COALESCE(string_agg(DISTINCT ti.instrument, ', '), ''), " +
                    "ARRAY_AGG(DISTINCT CONCAT(s.dayofweek, '" + SPLIT_REGEX + "', s.weekofyear, '" + SPLIT_REGEX + "', s.year, '" + SPLIT_REGEX + "', s.start_time, '" + SPLIT_REGEX + "', s.end_time)), " +
                    "p.picture, t.video " +
                    "FROM notebridge2.person p, notebridge2.address a, notebridge2.teacher t " +
                    "LEFT JOIN (SELECT * FROM notebridge2.schedule " +
                    "WHERE weekofyear = ? " +
                    "AND year = ? ) s ON t.tid = s.teacher " +
                    "LEFT JOIN notebridge2.review r ON r.tid = t.tid " +
                    "LEFT JOIN notebridge2.ti_association ti ON t.tid = ti.tid " +
                    "WHERE p.pid = t.pid AND (a.house_nr = p.house_nr AND a.postal_code = p.postal_code) " +
                    "AND t.tid = ? " +
                    "GROUP BY t.tid, p.first_name, p.surname, a.city, t.rate, p.picture; ";
            PreparedStatement pre = connection.prepareStatement(query);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            pre.setInt(1, cal.get(Calendar.WEEK_OF_YEAR));
            pre.setInt(2, cal.get(Calendar.YEAR));
            pre.setInt(3, tid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return new TeacherProfile(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        "",
                        result.getDouble(5),
                        result.getInt(6),
                        Arrays.asList(result.getString(7).split(", ")),
                        Availability.toAvailabilitiesList(result.getArray(8)),
                        result.getString(9),
                        result.getString(10));
            }
            return null;
        } catch (SQLException sqle) {
            System.err.println("Error connecting4: " + sqle);
            return null;
        }
    }

    public boolean addAvailability(int tid, int dayOfWeek, int weekOfYear, int year, String startTime, String endTime) {
        String avQuery = "INSERT INTO notebridge2.availability " +
                "VALUES(?, ?, ?, ?, ?) ";
        String scheduleQuery = "INSERT INTO notebridge2.schedule " +
                "VALUES(?, ?, ?, ?, ?, ?) ";
        try {
            PreparedStatement pre = connection.prepareStatement(avQuery);
            pre.setInt(1, dayOfWeek);
            pre.setInt(2, weekOfYear);
            pre.setInt(3, year);
            pre.setTime(4, Time.valueOf(startTime));
            pre.setTime(5, Time.valueOf(endTime));
            pre.execute();
            System.out.println("Inserted availability into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
        }

        try {
            PreparedStatement pre = connection.prepareStatement(scheduleQuery);
            pre.setInt(1, tid);
            pre.setInt(2, dayOfWeek);
            pre.setInt(3, weekOfYear);
            pre.setInt(4, year);
            pre.setTime(5, Time.valueOf(startTime));
            pre.setTime(6, Time.valueOf(endTime));
            pre.execute();
            System.out.println("Inserted schedule into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addReview(String message, int sid, int tid, int score) {
        // Check if the student and teacher exist
        String existenceQuery = "SELECT COUNT(*) FROM notebridge2.lesson " +
                "WHERE sid = ? AND tid = ?";
        try {
            PreparedStatement existencePre = connection.prepareStatement(existenceQuery);
            existencePre.setInt(1, sid);
            existencePre.setInt(2, tid);
            ResultSet existenceResult = existencePre.executeQuery();
            existenceResult.next();
            int rowCount = existenceResult.getInt(1);
            if (rowCount == 0) {
                System.out.println("Student or teacher does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Failed to check existence: " + e.getMessage());
            return false;
        }

        // Insert the review
        String reviewQuery = "INSERT INTO notebridge2.review (message, sid, tid, score) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pre = connection.prepareStatement(reviewQuery);
            pre.setString(1, message);
            pre.setInt(2, sid);
            pre.setInt(3, tid);
            pre.setInt(4, score);
            pre.execute();
            System.out.println("Inserted into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
            return false;
        }
        return true;
    }


    public boolean addLesson(int sid, int tid, int dayOfWeek, int weekOfYear, int year, String startTime, String endTime) {
        String reviewQuery = "INSERT INTO notebridge2.lesson (cost, is_paid, sid, tid, start_time, end_time, dayofweek, weekofyear, year, is_confirmed) " +
                "VALUES((SELECT rate FROM notebridge2.teacher WHERE tid = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            PreparedStatement pre = connection.prepareStatement(reviewQuery);
            pre.setInt(1, tid);
            pre.setBoolean(2, false);
            pre.setInt(3, sid);
            pre.setInt(4, tid);
            pre.setTime(5, Time.valueOf(startTime));
            pre.setTime(6, Time.valueOf(endTime));
            pre.setInt(7, dayOfWeek);
            pre.setInt(8, weekOfYear);
            pre.setInt(9, year);
            pre.setBoolean(10, false);
            pre.execute();
            System.out.println("Inserted lesson into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public StudentProfile getStudent(int sid) {
        try {
            String query = "SELECT st.sid, p.first_name, p.surname, a.city, COALESCE(string_agg(DISTINCT CONCAT(si.instrument || ':' || si.expertise || ':' || i.icon_path), ', '), ''), ARRAY_AGG(DISTINCT CONCAT(l.tid, '" + SPLIT_REGEX + "', l.cost, '" + SPLIT_REGEX + "', l.is_paid, '" + SPLIT_REGEX + "', l.dayofweek, '" + SPLIT_REGEX + "', l.weekofyear, '" + SPLIT_REGEX + "', l.year, '" + SPLIT_REGEX + "', l.start_time, '" + SPLIT_REGEX + "', l.end_time)), p.picture " +
                    "FROM notebridge2.person p, notebridge2.address a, notebridge2.instrument i, notebridge2.student st " +
                    "LEFT JOIN (SELECT * FROM notebridge2.lesson WHERE weekofyear = ? AND year = ? ) l ON st.sid = l.sid " +
                    "LEFT JOIN notebridge2.si_association si ON st.sid = si.sid " +
                    "WHERE p.pid = st.pid AND (a.house_nr = p.house_nr AND a.postal_code = p.postal_code) " +
                    "AND i.name = si.instrument " +
                    "AND st.sid = ? " +
                    "GROUP BY st.sid, p.first_name, p.surname, a.city, p.picture;";
            PreparedStatement pre = connection.prepareStatement(query);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            pre.setInt(1, cal.get(Calendar.WEEK_OF_YEAR));
            pre.setInt(2, cal.get(Calendar.YEAR));
            pre.setInt(3, sid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return new StudentProfile(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        Arrays.asList(result.getString(5).split(", ")),
                        Lesson.toLessonList(result.getArray(6)),
                        result.getString(7)
                );
            }
            return null;
        } catch (SQLException sqle) {
            System.err.println("Error connecting5: " + sqle);
            return null;
        }
    }

    public boolean addNotificationStudent(int sid, String message, int tid) {
        String studentNotificationQuery = "INSERT INTO notebridge2.notification(pid, message, from_teacher) " +
                "VALUES((SELECT pid FROM notebridge2.student WHERE sid = ?), ?, ?) ";
        try {
            PreparedStatement pre = connection.prepareStatement(studentNotificationQuery);
            pre.setInt(1, sid);
            pre.setString(2, message);
            pre.setInt(3, tid);
            pre.execute();
            System.out.println("Inserted notification into database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<StudentNotification> getStudentNotifications(int sid) {
        try {
            List<StudentNotification> studentNotifications = new ArrayList<>();
            String query = "SELECT n.message, CONCAT(p.first_name, ' ', p.surname) " +
                    "FROM notebridge2.notification n, notebridge2.teacher t, notebridge2.person p " +
                    "WHERE n.pid = (SELECT pid FROM notebridge2.student WHERE sid = ?) " +
                    "AND n.from_teacher = t.tid " +
                    "AND t.pid = p.pid";
            System.out.println(query);
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, sid);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                studentNotifications.add(new StudentNotification(result.getString(1),
                        result.getString(2)));
            }
            return studentNotifications;
        } catch (SQLException sqle) {
            System.err.println("Error connecting6: " + sqle);
            sqle.printStackTrace();
            return null;
        }
    }

    public boolean addNotificationTeacher(int tid, String message, int sid) {
        String teacherNotificationQuery = "INSERT INTO notebridge2.notification(pid, message, from_student) " +
                "VALUES((SELECT pid FROM notebridge2.teacher WHERE tid = ?), ?, ?) ";
        try {
            PreparedStatement pre = connection.prepareStatement(teacherNotificationQuery);
            pre.setInt(1, tid);
            pre.setString(2, message);
            pre.setInt(3, sid);
            pre.execute();
            System.out.println("Inserted into notification database");
        } catch (SQLException e) {
            System.out.println("Failed to insert: " + e.getMessage());
            return false;
        }
        return true;
    }

    public List<TeacherNotification> getTeacherNotifications(int tid) {
        try {
            List<TeacherNotification> teacherNotifications = new ArrayList<>();
            String query = "SELECT n.message, CONCAT(p.first_name, ' ', p.surname) " +
                    "FROM notebridge2.notification n, notebridge2.student s, notebridge2.person p " +
                    "WHERE n.pid = (SELECT pid FROM notebridge2.teacher WHERE tid = ?) " +
                    "AND n.from_student = s.sid " +
                    "AND s.pid = p.pid";
            System.out.println(query);
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                teacherNotifications.add(new TeacherNotification(result.getString(1),
                        result.getString(2)));
            }
            Collections.reverse(teacherNotifications);
            return teacherNotifications;
        } catch (SQLException sqle) {
            System.err.println("Error connecting7: " + sqle);
            sqle.printStackTrace();
            return null;
        }
    }

    public boolean deleteAllNotifications(int pid) {
        try {
            String query = "DELETE FROM notebridge2.notification " +
                    "WHERE pid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, pid);
            pre.execute();
        } catch (SQLException sqle) {
            System.err.println("Error connecting8 " + sqle);
            return false;
        }
        return true;
    }

    public boolean deleteNotification(int pid, String message) {
        try {
            String query = "DELETE FROM notebridge2.notification " +
                    "WHERE pid = ? AND message = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, pid);
            pre.setString(2, message);
            System.out.println(query);
            pre.execute();
        } catch (SQLException sqle) {
            System.err.println("Error connecting9 " + sqle);
            return false;
        }
        return true;
    }

    public boolean deleteAvailability(int tid, int dayOfWeek, int weekOfYear, int year, String startTime, String endTime) {
        try {
            String query = "DELETE FROM notebridge2.schedule " +
                    "WHERE teacher = ? AND dayofweek = ? AND weekofyear = ? AND year = ? AND start_time = ? AND end_time = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            pre.setInt(2, dayOfWeek);
            pre.setInt(3, weekOfYear);
            pre.setInt(4, year);
            pre.setTime(5, Time.valueOf(startTime));
            pre.setTime(6, Time.valueOf(endTime));
            pre.execute();
        } catch (SQLException sqle) {
            System.err.println("Error connecting10 " + sqle);
            return false;
        }
        return true;
    }

    public boolean updateProfile(int pid, String firstname, String surname, String phone_nr, String email, String street,
                                 int newHouseNum, String city, String newPostalCode, String instruments, int id, boolean isTeacher) {

        if (!instruments.equals("") && !instruments.isEmpty()) {
            String instrumentsArray[] = instruments.split(" ");
            if (isTeacher) {
                try {
                    String query = "DELETE FROM notebridge2.ti_association " +
                            "WHERE notebridge2.ti_association.tid = ?";
                    PreparedStatement pre = connection.prepareStatement(query);
                    pre.setInt(1, id);
                    pre.execute();
                } catch (SQLException sqle) {
                    System.err.println("Error connecting11 " + sqle);
                    return false;
                }
                for (int i = 0; i < instrumentsArray.length; i++) {
                    try {
                        String query = "INSERT INTO notebridge2.ti_association (tid, instrument) " +
                                "VALUES (?, ?)";
                        PreparedStatement pre = connection.prepareStatement(query);
                        pre.setInt(1, id);
                        pre.setString(2, instrumentsArray[i]);
                        pre.execute();
                    } catch (SQLException sqle) {
                        System.err.println("Error connecting12 " + sqle);
                        return false;
                    }
                }
            }
            else {
                try {
                    String query = "DELETE FROM notebridge2.si_association " +
                            "WHERE notebridge2.si_association.sid = ?";
                    PreparedStatement pre = connection.prepareStatement(query);
                    pre.setInt(1, id);
                    pre.execute();
                } catch (SQLException sqle) {
                    System.err.println("Error connecting13 " + sqle);
                    return false;
                }
                for (int i = 0; i < instrumentsArray.length; i++) {
                    try {
                        String query = "INSERT INTO notebridge2.si_association (sid, instrument) " +
                                "VALUES (?, ?)";
                        PreparedStatement pre = connection.prepareStatement(query);
                        pre.setInt(1, id);
                        pre.setString(2, instrumentsArray[i]);
                        pre.execute();
                    } catch (SQLException sqle) {
                        System.err.println("Error connecting14 " + sqle);
                        return false;
                    }
                }
            }
        }


        if (!firstname.equals("") && !firstname.isEmpty()) {
            try {
                String query = "UPDATE notebridge2.person " +
                        "SET first_name = ? " +
                        "WHERE notebridge2.person.pid = ? ";
                PreparedStatement pre = connection.prepareStatement(query);
                pre.setString(1, firstname);
                pre.setInt(2, pid);
                pre.execute();
            } catch (SQLException sqle) {
                System.err.println("Error connecting15 " + sqle);
                return false;
            }
        }
        if (!surname.equals("") && !surname.isEmpty()) {
            try {
                String query = "UPDATE notebridge2.person " +
                        "SET surname = ? " +
                        "WHERE notebridge2.person.pid = ? ";
                PreparedStatement pre = connection.prepareStatement(query);
                pre.setString(1, surname);
                pre.setInt(2, pid);
                pre.execute();
            } catch (SQLException sqle) {
                System.err.println("Error connecting16 " + sqle);
                return false;
            }
        }
        if (!phone_nr.equals("") && !phone_nr.isEmpty()) {
            try {
                String query = "UPDATE notebridge2.person " +
                        "SET phone_nr = ? " +
                        "WHERE notebridge2.person.pid = ? ";
                PreparedStatement pre = connection.prepareStatement(query);
                pre.setString(1, phone_nr);
                pre.setInt(2, pid);
                pre.execute();
            } catch (SQLException sqle) {
                System.err.println("Error connecting17 " + sqle);
                return false;
            }
        }
        if (!email.equals("") && !email.isEmpty()) {
            try {
                String query = "UPDATE notebridge2.person " +
                        "SET email = ? " +
                        "WHERE notebridge2.person.pid = ? ";
                PreparedStatement pre = connection.prepareStatement(query);
                pre.setString(1, email);
                pre.setInt(2, pid);
                pre.execute();
            } catch (SQLException sqle) {
                System.err.println("Error connecting18 " + sqle);
                return false;
            }
        }
        if ((!newPostalCode.equals("") && !newPostalCode.isEmpty()) && (newHouseNum != 0)) {
            try {
                String query = "SELECT notebridge2.person.postal_code, notebridge2.person.house_nr " +
                        "FROM notebridge2.person " +
                        "WHERE notebridge2.person.pid = ? ";
                PreparedStatement pre = connection.prepareStatement(query);
                pre.setInt(1, pid);
                ResultSet result = pre.executeQuery();
                try {
                    query = "UPDATE notebridge2.address " +
                            "SET postal_code = ?, " +
                            "house_nr = ?, " +
                            "city = ?, " +
                            "street = ? " +
                            "WHERE notebridge2.address.house_nr = ? " +
                            "AND notebridge2.address.postal_code = ? ";
                    pre = connection.prepareStatement(query);
                    pre.setString(1, newPostalCode);
                    pre.setInt(2, newHouseNum);
                    pre.setString(3, city);
                    pre.setString(4, street);
                    result.next();
                    pre.setInt(5, result.getInt(2));
                    pre.setString(6, result.getString(1));
                    pre.execute();
                } catch (SQLException sqle) {
                    System.err.println("Error connecting19 " + sqle);
                    return false;
                }
            } catch (SQLException sqle) {
                System.err.println("Error connecting20 " + sqle);
                return false;
            }
        }
        return true;
    }

    public boolean updatePassword(String passwords, int pid) {
        String oldPassword = passwords.split(" ")[0];
        String newPassword = passwords.split(" ")[1];

        try {
            String query = "SELECT p.salt " +
                    "FROM notebridge2.person p " +
                    "WHERE p.pid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, pid);
            pre.execute();
            ResultSet result = pre.getResultSet();
            result.next();

            try {
                query = "UPDATE notebridge2.person " +
                        "SET password = ? " +
                        "WHERE notebridge2.person.pid = ?; ";
                pre = connection.prepareStatement(query);
                pre.setString(1, hashPassword(newPassword, result.getString(1)));
                pre.setInt(2, pid);
                if(checkPassword(pid, oldPassword)) {
                    pre.execute();
                    return true;
                }
            } catch (SQLException sqle) {
                System.err.println("Error connecting " + sqle);
                return false;
            }
        }
        catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return false;
    }

    public List<Availability> getTeacherAvailability(int tid, int weekOfYear, int year) {
        try {
            String query = "SELECT ARRAY_AGG(dates || '" + SPLIT_REGEX + "' || status) " +
                    "FROM ( " +
                    "  SELECT DISTINCT CONCAT(s.dayofweek,  '" + SPLIT_REGEX + "', s.weekofyear,  '" + SPLIT_REGEX + "', s.year,  '" + SPLIT_REGEX + "', s.start_time,  '" + SPLIT_REGEX + "', s.end_time) AS dates, " +
                    "  CASE WHEN COUNT(l.*) > 0 THEN 'closed' ELSE 'open' END AS status " +
                    "  FROM notebridge2.teacher t " +
                    "  LEFT JOIN notebridge2.schedule s ON t.tid = s.teacher AND s.weekofyear = ? AND s.year = ? " +
                    "  LEFT JOIN notebridge2.lesson l ON s.teacher = l.tid AND s.dayofweek = l.dayofweek AND s.weekofyear = l.weekofyear AND s.year = l.year AND s.start_time = l.start_time AND s.end_time = l.end_time " +
                    "  WHERE t.tid = ? " +
                    "  GROUP BY dates " +
                    ") AS availabilities;";

            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, weekOfYear);
            pre.setInt(2, year);
            pre.setInt(3, tid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return Availability.toAvailabilitiesList(result.getArray(1));
            }
            return null;
        } catch (SQLException sqle) {
            System.err.println("Error connecting: " + sqle);
            return null;
        }
    }

    public String getLessonTimes(int tid, int sid){
        List<LessonInfo> list = getLessonsTeacher(tid, sid);
        LessonInfo lessonInfo = list.get(0);
        String lessonTimes;
        switch (lessonInfo.getDayofweek()) {
            case 1:
                lessonTimes = "Monday";
                break;
            case 2:
                lessonTimes = "Tuesday";
                break;
            case 3:
                lessonTimes = "Wednesday";
                break;
            case 4:
                lessonTimes = "Thursday";
                break;
            case 5:
                lessonTimes = "Friday";
                break;
            case 6:
                lessonTimes = "Saturday";
                break;
            case 7:
                lessonTimes = "Sunday";
                break;
            default:
                lessonTimes = "Invalid";
                break;
        }
        return lessonTimes + " in week " + lessonInfo.getWeekofyear() + " at " + lessonInfo.getStarttime();
    }
    public List<LessonInfo> getLessonsTeacher(int tid, int sid) {
        try {
            String query = "SELECT start_time, dayofweek, weekofyear " +
                    "FROM notebridge2.lesson " +
                    "WHERE tid = ? AND sid = ? " +
                    "ORDER BY lid DESC";

            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            pre.setInt(2, sid);
            ResultSet result = pre.executeQuery();
            List<LessonInfo> lessons = new ArrayList();
            while (result.next()) {
                lessons.add(new LessonInfo(result.getString(1),
                        result.getInt(2),
                        result.getInt(3)));

            }
            return lessons;
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
        }
        return null;
    }

    public String getLessonTimes(int lid){
        LessonInfo lessonInfo = getLessonTeacher(lid);
        String lessonTimes;
        switch (lessonInfo.getDayofweek()) {
            case 1:
                lessonTimes = "Monday";
                break;
            case 2:
                lessonTimes = "Tuesday";
                break;
            case 3:
                lessonTimes = "Wednesday";
                break;
            case 4:
                lessonTimes = "Thursday";
                break;
            case 5:
                lessonTimes = "Friday";
                break;
            case 6:
                lessonTimes = "Saturday";
                break;
            case 7:
                lessonTimes = "Sunday";
                break;
            default:
                lessonTimes = "Invalid";
                break;
        }
        return lessonTimes + " in week " + lessonInfo.getWeekofyear() + " at " + lessonInfo.getStarttime();
    }
    public LessonInfo getLessonTeacher(int lid) {
        try {
            String query = "SELECT start_time, dayofweek, weekofyear " +
                    "FROM notebridge2.lesson " +
                    "WHERE lid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, lid);
            ResultSet result = pre.executeQuery();
            System.out.println(query);
            if (result.next()) {
                return new LessonInfo(
                        result.getString(1),
                        result.getInt(2),
                        result.getInt(3)
                );
            }
        }catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
        }
        return null;
    }

    public List<LessonInfo> getLessons(int sid) {
        try {
            String query = "SELECT l.start_time, l.end_time, p.first_name, p.surname, l.dayofweek, l.weekofyear, l.year, l.lid, l.tid, l.sid, l.is_paid " +
                    "FROM notebridge2.lesson l, notebridge2.person p, notebridge2.teacher t " +
                    "WHERE l.sid = ? AND l.tid = t.tid AND t.pid = p.pid";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, sid);
            ResultSet result = pre.executeQuery();
            List<LessonInfo> lessons = new ArrayList();
            while (result.next()) {
                lessons.add(new LessonInfo(result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getInt(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getInt(8),
                        result.getInt(9),
                        result.getInt(10),
                        result.getBoolean(11)));

            }
            return lessons;
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
        }
        return null;
    }

    public int getLessonSid(int lid) {
        try {
            String query = "SELECT sid " +
                    "FROM notebridge2.lesson " +
                    "WHERE lid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, lid);
            ResultSet result = pre.executeQuery();
            System.out.println(query);
            if (result.next()) {
                return result.getInt(1);
            }
        }catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
        }
        return 0;
    }

    public List<LessonInfo> getTeacherLessons(int tid) {
        try {
            String query = "SELECT DISTINCT l.start_time, l.end_time, p.first_name, p.surname, l.dayofweek, l.weekofyear, l.year, l.lid, l.tid, l.sid, l.is_paid " +
                    "FROM notebridge2.lesson l, notebridge2.person p, notebridge2.teacher t , notebridge2.student s " +
                    "WHERE l.tid = ? AND l.sid = s.sid AND s.pid = p.pid";


            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            ResultSet result = pre.executeQuery();
            System.out.println(query);
            List<LessonInfo> lessons = new ArrayList();
            while (result.next()) {
                lessons.add(new LessonInfo(result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getInt(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getInt(8),
                        result.getInt(9),
                        result.getInt(10),
                        result.getBoolean(11)));
            }
            return lessons;
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
        }
        return null;
    }

    public boolean updateExpertise(int sid, String instrument, int expertise) {
        try {
            String query = "UPDATE notebridge2.si_association " +
                    "SET expertise = ? " +
                    "WHERE sid = ? " +
                    "AND instrument = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, expertise);
            pre.setInt(2, sid);
            pre.setString(3, instrument);
            pre.execute();
            System.out.println("Update successful!");
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    public String hashPassword(String pass, String salt) {
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

    public boolean checkPassword(int pid, String password) {
        try {
            String query = "SELECT password, salt " +
                    "FROM notebridge2.person " +
                    "WHERE pid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, pid);
            ResultSet result = pre.executeQuery();
            result.next();
            String passwordCheck = result.getString(1);
            String saltCheck = result.getString(2);
            String passwordHash = hashPassword(password, saltCheck);
            if (!passwordCheck.equals(passwordHash)) {
                return false;
            }
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    public String getStudentBilling(int sid){
        try{
            String query = "SELECT s.email, a.postal_code, ARRAY_AGG(a.street || '" + SPLIT_REGEX + "' || a.house_nr)," +
                    "FROM student s, address a, person p" +
                    "WHERE s.sid = ? AND s.pid = a.pid AND (a.house_nr = p.house_nr AND a.postal_code = p.postal_code)";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, sid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return null;
            }
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return null;
        }
        return null;
    }

    public boolean updateProfilePicture(int pid, String picture) {
        try {
            String query = "UPDATE notebridge2.person " +
                    "SET picture = ? " +
                    "WHERE pid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setString(1, picture);
            pre.setInt(2, pid);
            pre.execute();
            System.out.println("Update successful!");
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    public boolean updateVideo(int tid, String video) {
        try {
            String query = "UPDATE notebridge2.teacher " +
                    "SET video = ? " +
                    "WHERE tid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setString(1, video);
            pre.setInt(2, tid);
            pre.execute();
            System.out.println("Update successful!");
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    /**
     * Delete lesson from a student account.
     * @param sid student ID
     * @param tid teacher ID
     * @param lid lesson ID
     * @return true if the deletion has been done
     */
    public boolean deleteLesson(int sid, int tid, int lid) {
        try {
            String query = "DELETE FROM notebridge2.lesson " +
                    "WHERE tid = ? " +
                    "AND sid = ? " +
                    "AND lid = ? " +
                    "AND is_paid = false";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            pre.setInt(2, sid);
            pre.setInt(3, lid);
            pre.execute();
            System.out.println("Delete successful!");
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    /**
     * Delete lesson from a teacher account.
     * @param tid teacher ID
     * @param lid lesson ID
     * @return true if the deletion has been done
     */
    public boolean deleteLesson(int tid, int lid) {
        try {
            String query = "DELETE FROM notebridge2.lesson " +
                    "WHERE tid = ? " +
                    "AND lid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            pre.setInt(2, lid);
            pre.execute();
            System.out.println("Delete successful!");
        } catch (SQLException sqle) {
            System.err.println("Error connecting " + sqle);
            return false;
        }
        return true;
    }

    public boolean updatePaid(boolean isPaid, int lid){
        try{
            String query = "UPDATE notebridge2.lesson " +
                    "SET is_paid = ? " +
                    "WHERE lid = ? ";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setBoolean(1, isPaid);
            pre.setInt(2, lid);
            pre.execute();
            System.out.println("Update successful!");
        }
        catch (SQLException sqle){
            System.err.println("Error connecting" + sqle);
            return false;
        }
        return true;
    }

    public boolean getLessonStatus(int lid){
        try{
            String query = "SELECT is_paid " +
                    "FROM notebridge2.lesson " +
                    "WHERE lid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, lid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return result.getBoolean(1);
            }
            return false;
        } catch (SQLException sqle){
            System.err.println("Error connecting" + sqle);
            return false;
        }
    }

    public int getTeacherRate(int tid){
        try{
            String query = "SELECT rate " +
                    "FROM notebridge2.teacher " +
                    "WHERE tid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, tid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
            return 0;
        } catch (SQLException sqle){
            System.err.println("Error connecting" + sqle);
            return 0;
        }
    }

    public String getStudentEmail(int sid){
        try{
            String query = "SELECT p.email " +
                    "FROM notebridge2.person p, notebridge2.student s " +
                    "WHERE p.pid = s.pid " +
                    "AND s.sid = ?";
            PreparedStatement pre = connection.prepareStatement(query);
            pre.setInt(1, sid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
            return null;
        } catch(SQLException sqle) {
            System.err.println("Error Connecting" + sqle);
            return null;
        }
    }
}