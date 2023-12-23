<%--
Created by IntelliJ IDEA.
User: jortb
Date: 1-6-2023
Time: 22:44
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="scripts/logout.js" type="text/javascript"></script>
<img alt="menu" class="burger" onclick="hamburgerSlide()" src="img/burger.png" type="button">
<a href="home.html"><img alt="" class="logo" height="100px" src="img/note-bridge-logo-no-background.png"></a>
<div>
    <ul class="navbar">
        <li><a href="home.html">Home</a></li>
        <% if(session.getAttribute("pid") != null) {
            out.print("<li class=\"dropdown\">\n" +
                    "    <a href=\"#\" class=\"dropbtn\" onclick=\"toggleDropdown()\">Inbox</a>\n" +
                    "    <div class=\"dropdown-content\">\n" +
                    "        <div id=\"notifications\"></div>\n" +
                    "        <button class=\"delete-all-btn\" onclick=\"deleteAllNotifications()\">DELETE ALL</button>\n" +
                    "    </div>\n" +
                    "</li>\n" +
                    "<li><a href=\"profile.jsp\">Profile</a></li>\n" +
                    "<li><a href=\"logout.jsp\">Logout</a></li>");
        } else {
            out.print("<li><a href=\"login.jsp\">Login</a></li>\n" +
                    "<li><a href=\"register.jsp\">Register</a></li>");
        }%>
    </ul>
</div>




<script>
    //html for a single notification
    const notificationTemplate = `
  <div class="notification">
      <img src="img/garbage-bin-png-1.png" alt="bin" class="delete-btn" onclick="deleteNotification('%message')">
      <h2 class="message"><span class="name">%name</span> %restOfMessage</h2>
  </div>`;

    //makes the username from the notification into seperate elements to split if name is too long
    function getFirstTwoWords(message) {
        const words = message.split(' ');
        if (words.length > 1) {
            return words[0] + ' ' + words[1];
        }
        return message;
    }

    //gets the rest of the message to put into the notification
    function getRestOfMessage(message) {
        const words = message.split(' ');
        if (words.length > 2) {
            return words.slice(2).join(' ');
        }
        return '';
    }

    /**
     * delete a specific notification identified by the pid and message
     * @param message, the message inside the notifiation
     * @returns {Promise<void>}
     */
    async function deleteNotification(message) {
        const pid = '<%= session.getAttribute("pid") %>';

        await fetch('./api/person/delete_notification', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                pid: pid,
                message: message
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log('Notification deleted');
                } else {
                    console.log('Failed to delete notification');
                }
            })
            .catch(error => {
                console.log('Error while deleting notification:', error);
            });
    }


    /**
     * deletes all notifications of a certain person based on the session id
     * @returns {Promise<void>}
     */
    async function deleteAllNotifications() {
        const pid = '<%= session.getAttribute("pid") %>';

        await fetch('./api/person/delete_all_notifications', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                pid: pid,
            })
        })
            .then(response => {
                if (response.ok) {
                    console.log('Notifications deleted');
                } else {
                    console.log('Failed to delete notifications');
                }
            })
            .catch(error => {
                console.log('Error while deleting notifications:', error);
            });
    }

    //for the functionality of the mobile header
    function hamburgerSlide() {
        let nav = document.getElementById("mobile-navbar");
        console.log(nav.style.height);
        if (nav.style.height === "0px") {
            nav.style.height = nav.scrollHeight + 20 + "px";
        } else {
            nav.style.height = "0px";
        }
    }

    /**
     * gets all the notifications from the database
     * @returns {Promise<void>}
     */
    async function getNotifications() {
        const sid = '<%= session.getAttribute("sid") %>';
        const tid = '<%= session.getAttribute("tid") %>';
        let notifications = [];

        //checks if user is a student
        if (Number(sid) !== 0) {
            notifications = await fetch('./api/students/student/get_student_notification', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                },
            }).then(response => response.json()).catch(() => {
                location.href = "./error.html?msg=could not fetch notifications";
            });
        }
        //checks if the user is a teacher
        if (Number(tid) !== 0) {
            notifications = await fetch('./api/teachers/' + tid + '/get_teacher_notification', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                },
            }).then(response => response.json()).catch(() => {
                location.href = "./error.html?msg=could not fetch notifications";
            });
        }
        let nots = "";
        //replaces the placeholders in the notification with actual data from the database
        for (let i = 0; i < notifications.length; i++) {
            let notification = notifications[i];
            const name = getFirstTwoWords(notification.message);
            const restOfMessage = getRestOfMessage(notification.message);
            const formattedMessage = notificationTemplate
                .replace("%name", name)
                .replace("%restOfMessage", restOfMessage)
                .replace("%message", notification.message);
            nots += formattedMessage;
        }
        document.getElementById("notifications").innerHTML = nots;
    }

    /**
     * makes the inbox button drop down a menu when clicked
     */
    function toggleDropdown() {
        let dropdownContent = document.querySelector(".dropdown-content");
        dropdownContent.classList.toggle("show");

        if (dropdownContent.classList.contains("show")) {
            getNotifications();
        }
    }

    window.onclick = function(event) {
        if (!event.target.matches(".dropbtn")) {
            let dropdowns = document.getElementsByClassName("dropdown-content");
            for (let i = 0; i < dropdowns.length; i++) {
                let openDropdown = dropdowns[i];
                if (openDropdown.classList.contains("show")) {
                    openDropdown.classList.remove("show");
                }
            }
        }
    };
</script>


