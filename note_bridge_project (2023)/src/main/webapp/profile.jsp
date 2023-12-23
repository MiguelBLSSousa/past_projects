<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //If not logged in and not viewing a teacher, go to login page
    if (request.getSession().getAttribute("pid") == null && request.getParameter("tid") == null) {
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
    <title>Profile</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/profile.css" rel="stylesheet">
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
<section class="main-content">
    <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>
    <div id="side-bar-insert"></div>
    <div class="page-content">
        <div class="container">
            <% if (request.getParameter("tid") != null || (int) request.getSession().getAttribute("tid") != 0) {
                String tid = (request.getParameter("tid") != null) ? request.getParameter("tid") : String.valueOf(request.getSession().getAttribute("tid"));
            %>
            <h1 class="role">Teacher</h1>
            <div class="details">
                <div class="profile-pic">
                    <img id="teacher-picture" alt="profile picture" src="./img/sidebarmenu.png">
                </div>
                <div>
                    <div class="contents">
                        <p class="name">Name:</p>
                        <p class="location">City:</p>
                        <p class="rating">Rating:</p>
                        <p class="rate">Rate:</p>
                    </div>
                    <div class="personal-info">
                        <p id="name" class="name"></p>
                        <p id="location" class="location"></p>
                        <p id="rating" class="rating"></p>
                        <p id="rate" class="rate"></p>
                    </div>
                </div>
            </div>
            <div class="statistics">
                <div class="profile-col">
                    <div class="availability">
                        <h2>Availability</h2>
                        <div>
                            <div class="days">
                                <p>Monday:</p>
                                <p>Tuesday:</p>
                                <p>Wednesday:</p>
                                <p>Thursday:</p>
                                <p>Friday:</p>
                                <p>Saturday:</p>
                                <p>Sunday:</p>
                            </div>
                            <div class="time" id="availability">
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                            </div>
                        </div>
                    </div>
                    <% if (request.getParameter("tid") != null) { %>
                    <button class="book-lesson-btn" onclick="location.href='book.jsp?tid=<% out.print(request.getParameter("tid")); %>'">BOOK LESSON</button>
                    <% } else {%>
                    <button class="book-lesson-btn" onclick="location.href='availability.jsp'">CHANGE AVAILABILITY</button>
                    <% } %>
                </div>
                <div class="profile-col">
                    <div class="instruments">
                        <h2>Teaching Instruments</h2>
                        <p id="teacher-instruments"></p>
                    </div>
                    <div class="description">
                        <h2>Description</h2>
                        <p>Hello this is a description text!</p>
                    </div>
                    <div>
                        <button onclick="location.href='feedback.jsp?tid=' + getTID()" class="book-lesson-btn">REVIEW
                        </button>
                    </div>
                </div>
                <div class="profile-col">
                    <video id="video" controls>
                        Error Message
                    </video>
                </div>
            </div>

            <div class="all-reviews" id="all-reviews">
                <div class="reviews" id="reviews"></div>
                <button onclick="location.href='reviews.jsp?tid=' + getTID()" class="see-reviews-btn">SEE ALL
                </button>
            </div>


            <input value="<% out.print(tid); %>" id="tid" type="hidden">
            <script type="text/javascript" src="scripts/teacher-profile.js"></script>
            <% } %>

            <% if (request.getSession().getAttribute("sid") != null
                    && request.getSession().getAttribute("tid") != null
                    && (int) request.getSession().getAttribute("sid") != 0
                    && (int) request.getSession().getAttribute("tid") != 0
                    && request.getParameter("tid") == null) { %>
            <div class="profile-border"></div>
            <% } %>

            <% if (request.getSession().getAttribute("sid") != null && (int) request.getSession().getAttribute("sid") != 0 && request.getParameter("tid") == null) { %>
            <h1 class="role">Student</h1>
            <% if (request.getSession().getAttribute("tid") == null || (int) request.getSession().getAttribute("tid") == 0) { %>
            <div class="details">
                <div class="profile-pic">
                    <img id="student-picture" alt="profile picture" src="./img/sidebarmenu.png">
                </div>
                <div>
                    <div class="contents">
                        <p class="name">Name:</p>
                        <p class="location">City:</p>
                    </div>
                    <div class="personal-info">
                        <p id="student-name" class="name"></p>
                        <p id="student-location" class="location"></p>
                    </div>
                </div>
            </div>
            <% } %>
            <div class="statistics">
                <div class="profile-col">
                    <div class="availability">
                        <h2>Schedule</h2>
                        <div>
                            <div class="days">
                                <p>Monday:</p>
                                <p>Tuesday:</p>
                                <p>Wednesday:</p>
                                <p>Thursday:</p><p>Friday:</p>
                                <p>Saturday:</p>
                                <p>Sunday:</p>
                            </div>
                            <div class="time" id="schedule">
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                                <p>-</p>
                            </div>
                        </div>
                    </div>
                    <button class="book-lesson-btn" onclick="location.href='my-schedule.jsp';">SCHEDULE</button>
                </div>
                <div class="profile-col" style="max-width: 800px; min-width: 340px;">
                    <div class="expertise">
                        <h2>Expertise</h2>
                        <div id="student-instruments" class="items scrollable-y"></div>
                        <button onclick="updateExpertise(document.getElementById('student-instruments').children); location.href='profile.jsp';" class="change-exp-btn">SAVE CHANGES</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript" src="scripts/student-profile.js"></script>
            <% } %>
        </div>
    </div>
</section>
<section class="footer" id="footer"></section>
</body>
</html>