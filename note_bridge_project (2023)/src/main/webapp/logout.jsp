<%--
  Created by IntelliJ IDEA.
  User: joris
  Date: 12-6-2023
  Time: 10:20
  To change this template use File | Settings | File Templates.
--%>
<%
    // Invalidate the session and redirect to the home page
    session.invalidate();
    response.sendRedirect("home.html");
%>
