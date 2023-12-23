<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Book</title>
    <link href="css/style.css" rel="stylesheet">
    <link href="css/schedule.css" rel="stylesheet">
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

<body onload="showMonth(); showWeek()">
<section class="header" id="header"></section>
<section class="main-content">
    <div class="mobile-navbar" id="mobile-navbar" style="height: 0"></div>
    <div id="side-bar-insert"></div>
    <div class="page-content">
        <div class="days" id="schedule">
            <div class="times">
                <p class="time agenda" onclick="openCalendar()"></p>
                <p class="time">08:00</p>
                <p class="time">09:00</p>
                <p class="time">10:00</p>
                <p class="time">11:00</p>
                <p class="time">12:00</p>
                <p class="time">13:00</p>
                <p class="time">14:00</p>
                <p class="time">15:00</p>
                <p class="time">16:00</p>
                <p class="time">17:00</p>
                <p class="time">18:00</p>
                <p class="time">19:00</p>
                <p class="time">20:00</p>
                <p class="time">21:00</p>
            </div>
            <div class="day">
                <div class="slot type">
                    <p class="week-name">Monday</p>
                    <p class="date"></p>
                </div>
                <div class="slot" data-day="1" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="1" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type"><p class="week-name">Tuesday</p>
                    <p class="date"></p></div>
                <div class="slot" data-day="2" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="2" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type"><p class="week-name">Wednesday</p>
                    <p class="date"></p></div>
                <div class="slot" data-day="3" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="3" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type">
                    <p class="week-name">Thursday</p>
                    <p class="date"></p>
                </div>
                <div class="slot" data-day="4" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="4" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type">
                    <p class="week-name">Friday</p>
                    <p class="date"></p>
                </div>
                <div class="slot" data-day="5" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="5" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type">
                    <p class="week-name">Saturday</p>
                    <p class="date"></p>
                </div>
                <div class="slot" data-day="6" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="6" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
            <div class="day">
                <div class="slot type">
                    <p class="week-name">Sunday</p>
                    <p class="date"></p>
                </div>
                <div class="slot" data-day="7" data-time="08:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="09:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="10:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="11:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="12:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="13:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="14:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="15:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="16:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="17:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="18:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="19:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="20:00" onclick="onSlot(this)"></div>
                <div class="slot" data-day="7" data-time="21:00" onclick="onSlot(this)"></div>
            </div>
        </div>

        <div class="my-calendar-container" id="calendar" onclick="closeCalendar(event)" style="display: none">
            <div class="my-calendar">
                <div class="my-calendar-header">
                    <button class="my-calbutton" onclick="switchMonth(-1)">&lt;</button>
                    <div class="date" id="current-month-year"></div>
                    <button class="my-calbutton" onclick="switchMonth(1)">&gt;</button>
                </div>
                <div class="my-calendar-grid"></div>
            </div>
        </div>

        <div class="booking-modal-container" id="bookingModal" onclick="hideBookingModal(event)" style="display: none">
            <div class="booking-modal">
                <h2>Book a Lesson</h2>
                <div class="buttons">
                    <button class="booking-lesson-button" id="book-lesson-btn">Book</button>
                    <button class="booking-lesson-button" id="close-booking-modal-btn" onclick="hideBookingModal(event)">Close</button>
                </div>
                <div id="dateTimeContainer"></div>
            </div>
        </div>
    </div>
</section>
<section class="footer" id="footer"></section>
<script src="scripts/schedule-student.js" type="text/javascript"></script>
</body>
</html>