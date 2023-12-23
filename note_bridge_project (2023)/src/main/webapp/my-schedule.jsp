<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% if (session.getAttribute("pid") == null || (int) request.getSession().getAttribute("pid") == 0) response.sendRedirect("login.jsp"); %>



<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Teacher Schedule</title>
  <script src="https://cdn.jsdelivr.net/npm/ol@v7.3.0/dist/ol.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ol@v7.3.0/ol.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/my-schedule.css">
  <link rel="icon" type="image/png" href="img/music-note.png">

  <script>
    $(function () {
      $("#header").load("header.jsp");
      $("#mobile-navbar").load("mobile-navbar.jsp");
      $("#side-bar-insert").load("side-bar.html");
      $("#footer").load("footer.html");
    });

  </script>
</head>

<body>
<section class="header" id="header"></section>

<section class="main-content">

  <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>

  <div id="side-bar-insert"></div>

  <div class="page-content">

  <div class="lesson-container" id="lessons">

        <div class="lessonInfoContainer" >
            <div class="days" id="result"></div>
        </div>


    </div>
  </div>


</section>


<section>



</section>
<section class="footer" id="footer"></section>








<% if (request.getSession().getAttribute("tid") != null
        && (int) request.getSession().getAttribute("tid") != 0)
         { %>
<script src="scripts/my-schedule-teacher.js" type="text/javascript"></script>
<% } else { %>
<script src="scripts/my-schedule.js" type="text/javascript"> </script>
<% } %>



</body>
</html>
