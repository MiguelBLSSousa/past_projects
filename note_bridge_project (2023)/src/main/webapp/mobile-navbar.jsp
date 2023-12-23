<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="navbar">
    <% if(session.getAttribute("pid") != null) {
        out.print("<li><a href=\"profile.jsp\">Profile</a></li>\n" +
                "<li><a href=\"logout\">Logout</a></li>");
    }
    else {
        out.print("<li><a href=\"login.jsp\">Login</a></li>\n" +
                "<li><a href=\"register.jsp\">Register</a></li>");
    }%>
</ul>