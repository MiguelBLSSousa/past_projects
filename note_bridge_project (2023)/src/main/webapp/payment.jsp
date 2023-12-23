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
    <title>Payment</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/edit-profile.css" rel="stylesheet">
    <link href="css/map.css" rel="stylesheet">
    <link href="css/form.css" rel="stylesheet">
    <link href="css/payment.css" rel="stylesheet">
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
    <div class="reg-form">
        <h1>Booking Payment</h1>
        <div id = "page-1">
            <p>Select bank issuer</p>
            <select class="bank-select" name="bank" id="bank-selector">
                <option value = "ABNANL2A">ABN Amro Bank</option>
                <option value = "ASNBNL21">ASN bank</option>
                <option value = "BUNQNL2A">bunq</option>
                <option value = "INGBNL2A">ING</option>
                <option value = "RABONL2U">Rabobank</option>
                <option value = "RBRBNL21">Regiobank</option>
                <option value = "SNSBNL2A">SNS Bank</option>
                <option value = "TRIONL2U">Triodos Bank</option>
                <option value = "FVLBNL22">Van Lanschot Bankiers</option>
            </select>
        </div>

        <div id = "page-2" style="display: none">
            <p>Confirm payment details</p>
            <p id="bank"></p>
            <p id="rate"></p>
        </div>

        <div id = "page-error" style="display: none">
            <p>This Lesson is already paid</p>
        </div>

        <div id = "page-payment-unsuccessful" style="display: none">
            <p>payment is unsuccessful, please try again</p>
        </div>
        <div class="page-buttons">
            <button class="page-button" id="page-back" onclick="prevPage() " type="button" style="display: none">Back</button>
            <button class="page-button" id="page-next" onclick="nextPage()" type="button">Next</button>
            <button class="page-button" id="submit" onclick="submit()" type="button" style="display: none">Submit</button>
        </div>

    </div>
</section>
<section class="footer" id="footer"></section>
<script onload="fetchPayment()" src="scripts/payment.js" type="text/javascript"></script>
</body>
</html>
