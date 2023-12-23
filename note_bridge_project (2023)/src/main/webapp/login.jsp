<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% if (session.getAttribute("pid") != null) response.sendRedirect("profile.jsp"); %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Login</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/form.css" rel="stylesheet">
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
        <h1>Welcome</h1>
        <h1>Back!</h1>
        <form method="post" action="./login">
            <p>Email</p>
            <input name="email" placeholder="Email" type="text">
            <p>Password</p>
            <input name="password" placeholder="Password" type="password">
            <p class="center" id="msg"></p>
            <p class="center">Forgot password? <a href="./register.jsp"> Reset your password</a></p>
            <input id="submit" name="submit" type="submit" value="Submit">
            <p class="center">Don't have an account? <a href="./register.jsp"> Register</a></p>
        </form>
    </div>
</section>
<section class="footer" id="footer"></section>
<script>
    let searchParams = new URLSearchParams(location.search)
    let msg = searchParams.get("msg")
    if (msg !== null) {
        document.getElementById("msg").innerText = msg;
        document.getElementById("msg").classList.toggle("error", true);
    }
</script>

</body>
</html>