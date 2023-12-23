package nl.utwente.di.notebridge.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.UriInfo;
import nl.utwente.di.notebridge.model.LessonInfo;
import nl.utwente.di.notebridge.model.StudentProfile;
import sql.Database;

import java.util.List;

@Path("/students")
public class StudentsResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    private HttpServletRequest httpRequest;

    @GET
    @Path("/profile")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StudentProfile getProfile() {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return null;
        }
        int sid = (int) session.getAttribute("sid");
        Database database = new Database();
        return database.getStudent(sid);
    }

    @GET
    @Path("/lesson")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<LessonInfo> getLessonInfo() {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
            return null;
        }
        int sid = (int) session.getAttribute("sid");
        Database database = new Database();

        return database.getLessons(sid);
    }

    @Path("/student")
    public StudentResource getStudent() {
        try {
            HttpSession session = httpRequest.getSession();
            if (session.getAttribute("sid") == null || (int) session.getAttribute("sid") == 0) {
                return null;
            }
            int sid = (int) session.getAttribute("sid");
            return new StudentResource(uriInfo, request, sid, httpRequest);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


