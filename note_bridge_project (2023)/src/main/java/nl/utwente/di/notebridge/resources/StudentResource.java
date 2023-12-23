package nl.utwente.di.notebridge.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.UriInfo;
import nl.utwente.di.notebridge.model.*;
import sql.Database;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StudentResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    private HttpServletRequest httpRequest;
    int sid;

    public StudentResource(UriInfo uriInfo, Request request, int sid, HttpServletRequest httpRequest) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.sid = sid;
        this.httpRequest = httpRequest;
    }


    @GET
    @Path("/get_student_notification")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<StudentNotification> getStudentNotification(){
        Database database = new Database();
        return database.getStudentNotifications(sid);
    }

    @POST
    @Path("/expertise")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateExpertise(si_association si_association){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return;
        }
        int sid = (int) session.getAttribute("sid");
        Database database = new Database();
        database.updateExpertise(sid,
                si_association.getInstrument(),
                si_association.getExpertise());
        database.close();
    }

    @PUT
    @Path("/update")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateProfile(Update update){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
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
                update.getSid(),
                false
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
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
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

    @DELETE
    @Path("/cancelLesson")
    public void cancelLesson(@QueryParam("tid") int tid,
                             @QueryParam("lid") int lid){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Database database = new Database();
        database.deleteLesson(sid, tid, lid);
        database.close();
    }

    @PUT
    @Path("/paid")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updatePaid(@QueryParam("isPaid") boolean isPaid,
                           @QueryParam("lid") int lid){
        Database database = new Database();
        database.updatePaid(isPaid, lid);
        database.close();
    }

    @GET
    @Path("/is_paid")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public boolean getLessonStatus(@QueryParam("lid") int lid){
        Database database = new Database();
        boolean status = database.getLessonStatus(lid);
        database.close();
        return status;
    }

    @GET
    @Path("/getstudentemail")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getTeacherRate(){
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return null;
        }
        int sid = (int) session.getAttribute("sid");
        Database database = new Database();
        String email = database.getStudentEmail(sid);
        database.close();
        return email;
    }
}
