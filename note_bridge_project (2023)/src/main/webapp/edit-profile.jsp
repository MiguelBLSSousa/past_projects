<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  //If not logged in and not viewing a teacher, go to login page
  if (request.getSession().getAttribute("pid") == null) {
    response.sendRedirect("./login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta content="IE=edge" http-equiv="X-UA-Compatible">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <title>Edit Profile</title>
  <link href="css/style.css" rel="stylesheet">
  <link href="css/edit-profile.css" rel="stylesheet">
  <link href="css/map.css" rel="stylesheet">
  <link href="css/form.css" rel="stylesheet">
  <link href="img/music-note.png" rel="icon" type="image/png">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
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
<section class="content">
  <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>
  <div id="side-bar-insert"></div>
  <div class="edit-page-content">
    <div class="container" id="settings">
      <form method="PUT" action="javascript:void(0)">
        <div class="subdivision">
          Personal Information
        </div>
        <ul class="submenu" id="personal-info">
          <li>
            <p>First Name</p>
            <input name="firstname" type="text">
          </li>
          <li>
            <p>Surname</p>
            <input name="surname" type="text">
          </li>
          <li>
            <p>Street</p>
            <input name="street" type="text">
          </li>
          <li>
            <p>House Number</p>
            <input name="new-house-number" type="text">
          </li>
          <li>
            <p>City</p>
            <input name="city" type="text">
          </li>
          <li>
            <p>Postal/Zip Code</p>
            <input name="new-zip" type="text">
          </li>
          <li>
            <p>Phone Number</p>
            <input name="phone-number" type="text">
          </li>
        </ul>
        <div class="subdivision">
          Security Information
        </div>
        <ul class="submenu" id="security-info">
          <li>
            <p>Email</p>
            <input name="new-email" type="email">
          </li>
          <li>
            <p> Current Password </p>
            <input name="current-password" type="password">
          </li>
          <li>
            <p> New Password </p>
            <input name="new-password" type="password">
          </li>
          <li>
            <p> Confirm New Password </p>
            <input name="new-passwordCheck" type="password">
          </li>
        </ul>
        <div class="subdivision">
          Instruments
        </div>
        <ul class="submenu">
          <div class="instruments">
            <div id="instruments-page">
              <div class="map-filter-form">
                <div class="search">
                  <p>Search instruments</p>
                  <input id="search" name="search-instruments" oninput="searchInstruments()"
                         placeholder="Instrument..." type="search">
                </div>
                <input id="instruments-input" name="instruments-input" type="hidden">
                <ul id="instruments-filter"></ul>
              </div>
            </div>
          </div>
        </ul>
        <div class="subdivision">
          Customization
        </div>
        <ul class="submenu" id="profile-custom">
          <p> Profile Picture </p>
          <li class="sub">
            <input type="file" id="avatar" name="avatar" accept="image/*">
          </li>
          <p> Profile Video </p>
          <li class="sub">
            <input type="file" id="video" name="video" accept="video/mp4">
          </li>
          <p> Description </p>
          <li class="sub">
            <textarea class="story" name="description"> </textarea>
          </li>
        </ul>
        <div class="page-buttons">
          <% if (request.getParameter("tid") != null || (int) request.getSession().getAttribute("tid") != 0) {
            String tid = (request.getParameter("tid") != null) ? request.getParameter("tid") : String.valueOf(request.getSession().getAttribute("tid"));
          %>
          <input value="<% out.print(tid); %>" id="tid" name="tid" type="hidden">
          <%} if (request.getParameter("sid") != null || (int) request.getSession().getAttribute("sid") != 0) {
            String sid = (request.getParameter("sid") != null) ? request.getParameter("sid") : String.valueOf(request.getSession().getAttribute("sid"));
          %>
          <input value="<% out.print(sid); %>" id="sid" name="sid" type="hidden">
          <%}%>
          <input class="page-button" id="info-save" name="submit" type="submit" onclick="sendUpdate()" value="Save Changes">
          <button class="page-button" id="discard" type="button"><a href="profile.jsp" style="color: black; text-decoration: none">Discard Changes</a></button>
        </div>
      </form>
    </div>
  </div>
</section>
<section class="footer" id="footer"></section>
<script src="scripts/profile-edit.js" type="text/javascript"></script>
<script>
  var acc = document.getElementsByClassName("subdivision");
  var i;

  for (i = 0; i < acc.length; i++) {
    acc[i].closeElem = function () {
      this.nextElementSibling.style.display = "none";
      console.log("test closing");
    }

    acc[i].addEventListener("click", function() {
      this.classList.toggle("active");
      var panel = this.nextElementSibling;
      if (panel.style.display === "block") {
        panel.style.display = "none";
      } else {
        panel.style.display = "block";
      }

      for (let j = 0; j < acc.length; j++) {
        if (acc[j] !== this) {
          acc[j].closeElem();
        }
      }

    });
  }
</script>
</body>

</html>