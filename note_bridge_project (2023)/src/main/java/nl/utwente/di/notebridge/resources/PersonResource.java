package nl.utwente.di.notebridge.resources;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import nl.utwente.di.notebridge.FileSaver;
import nl.utwente.di.notebridge.model.Notification;
import nl.utwente.di.notebridge.model.StudentNotification;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import sql.Database;

import java.io.InputStream;


@Path("/person")
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    private HttpServletRequest httpRequest;
    @Context
    private ServletConfig config;

    @DELETE
    @Path("/delete_notification")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteNotification(Notification notification) {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("pid") == null || (int) session.getAttribute("pid") == 0) {
            return;
        }
        int pid = (int) session.getAttribute("pid");
        Database database = new Database();
        database.deleteNotification(pid,
                notification.getMessage());
        database.close();
    }

    @DELETE
    @Path("/delete_all_notifications")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteAllNotifications() {
        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("pid") == null || (int) session.getAttribute("pid") == 0) {
            return;
        }
        int pid = (int) session.getAttribute("pid");
        Database database = new Database();
        database.deleteAllNotifications(pid);
        database.close();
    }

    @PUT
    @Path("/update-profile-picture")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public Response uploadProfilePicture(@FormDataParam("picture") InputStream fileStream,
                                         @FormDataParam("picture") FormDataContentDisposition fileDetails) {

        HttpSession session = httpRequest.getSession();
        if (session.getAttribute("pid") == null || (int) session.getAttribute("pid") == 0) {
            return Response.notModified("Not authenticated").build();
        }
        if (fileDetails == null || fileStream == null) {
            return Response.notModified("Profile picture was empty").build();
        }

        String path = FileSaver.saveProfilePicture(fileStream, config.getServletContext().getInitParameter("uploadlocation"), fileDetails.getFileName(), (int) session.getAttribute("pid"));
        if (path == null) {
            return Response.notModified("An error occurred").build();
        }

        int pid = (int) session.getAttribute("pid");
        Database database = new Database();
        database.updateProfilePicture(pid, path);
        database.close();

        return Response.ok("Profile picture uploaded").build();
    }
}
