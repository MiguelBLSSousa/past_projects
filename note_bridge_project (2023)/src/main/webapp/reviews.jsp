<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Reviews</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/reviews.css" rel="stylesheet">
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
<section class="header" id="header"></section>

<section class="main-content">
    <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>
    <div class="rev-page">
        <div class="reviews" id="reviews"></div>
    </div>
</section>
<section class="footer" id="footer"></section>
<script src="scripts/reviews.js" type="text/javascript"></script>
</body>
</html>
