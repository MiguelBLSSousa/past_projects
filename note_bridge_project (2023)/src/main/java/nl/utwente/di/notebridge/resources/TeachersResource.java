package nl.utwente.di.notebridge.resources;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.UriInfo;
import nl.utwente.di.notebridge.model.Availability;
import nl.utwente.di.notebridge.model.LessonInfo;
import nl.utwente.di.notebridge.model.TeacherProfile;
import sql.Database;

import java.util.List;

@Path("/teachers")
public class TeachersResource {

    @Context
    private ServletConfig config;
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<TeacherProfile> getTeachers() {
        Database database = new Database();
        return database.getTeachers();
    }

    @Path("{tid}")
    public TeacherResource getTeacher(@PathParam("tid") String id) {
        try {
            return new TeacherResource(uriInfo, request, Integer.parseInt(id), httpRequest, config);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @PUT
    @Path("/availability")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void setAvailability(Availability availability) {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return;
        }
        int tid = (int) session.getAttribute("tid");
        Database database = new Database();
        database.addAvailability(tid,
                availability.getDayOfWeek(),
                availability.getWeekOfYear(),
                availability.getYear(),
                availability.getStartTime(),
                availability.getEndTime());
        database.close();
    }

    @DELETE
    @Path("/availability")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteAvailability(Availability availability) {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return;
        }
        int tid = (int) session.getAttribute("tid");
        Database database = new Database();
        database.deleteAvailability(tid,
                availability.getDayOfWeek(),
                availability.getWeekOfYear(),
                availability.getYear(),
                availability.getStartTime(),
                availability.getEndTime());
        database.close();
    }

    @GET
    @Path("/lesson")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<LessonInfo> getTeacherLessonInfo() {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("tid") == null || (int) session.getAttribute("tid") == 0) {
            return null;
        }
        int tid = (int) session.getAttribute("tid");
        Database database = new Database();

        return database.getTeacherLessons(tid);
    }
}
