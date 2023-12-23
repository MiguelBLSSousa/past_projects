<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% if (session.getAttribute("pid") != null) response.sendRedirect("profile.jsp"); %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Register</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/form.css" rel="stylesheet">
    <link href="css/map.css" rel="stylesheet">
    <link rel="icon" type="image/png" href="img/music-note.png">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <script>
        $(function () {
            $("#header").load("header.jsp");
            $("#mobile-navbar").load("mobile-navbar.jsp");
            $("#footer").load("footer.html");
        });
    </script>
</head>

<body>
<section class="header" id="header"></section>
<section class="main-content">
    <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>
    <div class="reg-form">
        <h1>Register</h1>

        <div class="account-selector">
            <p class="selected" id="student-account" onclick="toggleAccount('student')">Student</p>
            <p id="teacher-account" onclick="toggleAccount('teacher')">Teacher</p>
        </div>

        <form method="post" enctype='multipart/form-data' action="./register">
            <div id="page-1">
                <input id="account-value" name="accounttype" type="hidden" value="student">
                <p>First name</p>
                <input name="firstname" placeholder="First name" type="text">
                <p>Surname</p>
                <input name="surname" placeholder="Surname" type="text">
                <p>Phone number</p>
                <input name="phone" placeholder="Phone number" type="text">
                <p>Email</p>
                <input name="email" placeholder="Email" type="email">
                <p>Password</p>
                <input name="password" placeholder="Password" type="password">
                <p>Confirm password</p>
                <input name="passwordcheck" placeholder="Password" type="password">
            </div>

            <div id="student" style="display: none">
                <p class="center">Which <span class="interest">instruments</span> do you want to <span class="interest">learn!</span>
                </p>
            </div>

            <div id="address" style="display: none">
                <p>Street</p>
                <input name="street" placeholder="Street" type="text">
                <p>Nr.</p>
                <input name="housenumber" placeholder="House number" type="text">
                <p>City</p>
                <input name="city" placeholder="City" type="text">
                <p>Postal / Zip code</p>
                <input name="zip" placeholder="Zip" type="text">
            </div>
            <div id="teacher" style="display: none">
                <p>Rate (can be added later)</p>
                <input name="rate" placeholder="Rate per hour" type="text">
                <p class="center">Which <span class="interest">instruments</span> do you want to <span class="interest">teach!</span>
                </p>

            </div>

            <div id="instruments-page">
                <div class="map-filter-form">
                    <div class="search">
                        <p>Search instruments</p>
                        <input id="search" name="search-instruments" oninput="searchInstruments()"
                               placeholder="Instrument..." type="search">
                    </div>
                    <input id="instruments-input" name="instruments" type="hidden">
                    <ul id="instruments-filter"></ul>
                    <input type="file" name="picture" accept="image/*" >
                </div>
            </div>

            <div class="page-buttons">
                <button class="page-button" id="page-back" onclick="prevPage() " type="button">Back</button>
                <button class="page-button" id="page-next" onclick="nextPage()" type="button">Next</button>
                <input id="submit" name="submit" type="submit" value="Submit">
            </div>
            <p class="center">Already have an account? <a href="./login.jsp">Login</a></p>

        </form>
        <div class="progress-bar">
            <div class="slider" id="progress-bar-slider" style="width: 30px"></div>
        </div>
    </div>
</section>
<section class="footer" id="footer"></section>
<script src="scripts/account-selector.js" type="text/javascript"></script>
</body>

</html>