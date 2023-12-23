<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta content="IE=edge" http-equiv="X-UA-Compatible">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <title>Feedback</title>
  <link href="css/style.css" rel="stylesheet">
  <link href="css/form.css" rel="stylesheet">
  <link href="css/feedback.css" rel="stylesheet">
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
    <h2>Thank you for</h2>
    <h2>helping to improve</h2>
    <h2>our service!</h2>
    <br>
    <form action="javascript:void(0)">
      <p>Rating: <span id="ratingValue">1</span></p>
      <div class="slidecontainer">
        <input type="range" min="1" max="5" value="1" class="slider" id="myRange">
      </div>
      <br>
      <p>Message</p>
        <textarea id="message" name="message" rows="4" cols="50" placeholder="Fill in your message here"></textarea>
      <input id="submit" name="submit" type="submit" onclick="sendFeedback(); location.href='profile.jsp?tid=' + getTID();" value="Submit">
    </form>
  </div>
</section>
<section class="footer" id="footer"></section>
<script src="scripts/feedback.js" type="text/javascript"></script>

</body>
</html>