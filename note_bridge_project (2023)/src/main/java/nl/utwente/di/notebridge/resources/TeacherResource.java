package nl.utwente.di.notebridge.resources;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import nl.utwente.di.notebridge.FileSaver;
import nl.utwente.di.notebridge.model.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import sql.Database;

import java.io.InputStream;
import java.util.List;

@MultipartConfig
public class TeacherResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    private HttpServletRequest httpRequest;
    @Context
    private ServletConfig config;

    int tid;
    public TeacherResource(UriInfo uriInfo, Request request, int tid, HttpServletRequest httpRequest, ServletConfig config) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.tid = tid;
        this.httpRequest = httpRequest;
        this.config = config;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TeacherProfile getTeacher() {
        Database database = new Database();
        return database.getTeacher(tid);
    }

    @PUT
    @Path("/feedback")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void setReview(Review review) {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return;
        }
        int sid = (int) session.getAttribute("sid");
        Database database = new Database();
        database.addReview(review.getReview(),
                sid,
                review.getTid(),
                review.getRating());
        database.close();
    }

    @GET
    @Path("/review")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Review> getReview(){
        Database database = new Database();
        return database.getReviews(tid);
    }

    @PUT
    @Path("/book")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String bookLesson(Availability availability) {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return "You are not logged in";
        }
        int sid = (int) session.getAttribute("sid");

        Database database = new Database();
        boolean succes = database.addLesson(
                sid,
                tid,
                availability.getDayOfWeek(),
                availability.getWeekOfYear(),
                availability.getYear(),
                availability.getStartTime(),
                availability.getEndTime()
        );
        database.close();

        if (succes) {
            return "This lesson is booked";
        }
        return "Failed to book lesson";
    }

    @PUT
    @Path("/update")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateProfile(Update update){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return;
        }

        int pid = (int) session.getAttribute("pid");

        Database database = new Database();
        database.updateProfile(
                pid,
                update.getFirstname(),
                update.getSurname(),
                update.getPhoneNum(),
                update.getEmail(),
                update.getStreet(),
                update.getNewHouseNum(),
                update.getCity(),
                update.getNewPostalCode(),
                update.getInstruments(),
                update.getTid(),
                true
        );
        database.close();
    }

    @PUT
    @Path("/update-password")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updatePassword(String password){
        String jPassword = "";
        for (int i = 0; i < password.length(); i++) {
            if (i != 0 && i != (password.length() - 1)){
                jPassword += password.charAt(i);
            }
        }
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return;
        }

        int pid = (int) session.getAttribute("pid");

        Database database = new Database();
        database.updatePassword(
                jPassword,
                pid
        );
        database.close();
    }

    @GET
    @Path("/availability")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Availability> getAvailability(@QueryParam("weekNumber") int weekNumber, @QueryParam("year") int year) {
        Database database = new Database();
        return database.getTeacherAvailability(tid, weekNumber, year);
    }

    @PUT
    @Path("/teacher_notification")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addTeacherNotification(TeacherNotification teacherNotification){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return;
        }
        int from = (int) session.getAttribute("sid");
        Database database = new Database();
        database.addNotificationTeacher(teacherNotification.getTid(),
                database.getStudentName(from) + " has booked a lesson with you on " + database.getLessonTimes(tid, from)
                ,from);
        database.close();
    }

    @PUT
    @Path("/teacher_cancel_notification")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addTeacherCancelNotification(TeacherNotification teacherNotification, @QueryParam("lid") int lid){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return;
        }
        int from = (int) session.getAttribute("sid");
        Database database = new Database();
        database.addNotificationTeacher(teacherNotification.getTid(),
                database.getStudentName(from) + " has cancelled the lesson with you on " + database.getLessonTimes(lid)
                ,from);
        database.close();
    }

    @PUT
    @Path("/student_cancel_notification")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void addStudentCancelNotification(@QueryParam("lid") int lid){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return;
        }
        int from = (int) session.getAttribute("tid");
        Database database = new Database();
        database.addNotificationStudent(database.getLessonSid(lid),
                database.getTeacherName(from) + " has cancelled the lesson with you on " + database.getLessonTimes(lid)
                ,from);
        database.close();
    }

    @GET
    @Path("/get_teacher_notification")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TeacherNotification> getTeacherNotification(){
        Database database = new Database();
        return database.getTeacherNotifications(tid);
    }

    @PUT
    @Path("/update-video")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public Response uploadProfilePicture(@FormDataParam("video") InputStream fileStream,
                                         @FormDataParam("video") FormDataContentDisposition fileDetails) {

        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return Response.notModified("Not authenticated").build();
        }
        if (fileDetails == null || fileStream == null) {
            return Response.notModified("video was empty").build();
        }

        String path = FileSaver.saveVideo(fileStream, config.getServletContext().getInitParameter("uploadlocation"), fileDetails.getFileName(), (int) session.getAttribute("pid"));
        if (path == null) {
            return Response.notModified("An error occurred").build();
        }

        int tid = (int) session.getAttribute("tid");
        Database database = new Database();
        database.updateVideo(tid, path);
        database.close();

        return Response.ok("Profile picture uploaded").build();
    }

    @DELETE
    @Path("/cancelLesson")
    public void cancelLesson(@QueryParam("lid") int lid){
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Database database = new Database();
        database.deleteLesson(tid, lid);
        database.close();
    }

    @GET
    @Path("/getrate")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public int getTeacherRate(){
        Database database = new Database();
        int rate = database.getTeacherRate(tid);
        database.close();
        return rate;
    }
}
