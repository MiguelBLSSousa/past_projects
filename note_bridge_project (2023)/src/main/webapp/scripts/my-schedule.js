/** fetches the lesson info, the teachers name and the time.
 */
fetch('./api/students/lesson')
    .then(response => response.json())
    .then(data => {
        let resultElement = document.getElementById('result');
        console.log(data);

        let lessons = {};

        data.forEach(lesson => {
            let dayId = `${lesson.dayofweek}-${lesson.weekofyear}-${lesson.year}`;
            if (!lessons[dayId]) {
                lessons[dayId] = [];
            }
            lessons[dayId].push(lesson);
        });

        //sorts the week and day number so the days are in order
        let sortedDays = Object.keys(lessons).sort((a, b) => {
            let [dayOfWeekA, weekOfYearA] = a.split('-').map(Number);
            let [dayOfWeekB, weekOfYearB] = b.split('-').map(Number);


            if (weekOfYearA !== weekOfYearB) {
                return weekOfYearA - weekOfYearB;
            }


            return dayOfWeekA - dayOfWeekB;
        });

        //sorts the lessons in order of time
        for (let dayId in lessons) {
            lessons[dayId].sort((a, b) => {

                let startTimeA = a.starttime.slice(0, 5);
                let startTimeB = b.starttime.slice(0, 5);
                return startTimeA.localeCompare(startTimeB);
            });
        }




        // uses the sorted days and lessons and creates the overview
        sortedDays.forEach(dayId => {
            let day = document.getElementById(dayId);

            if (!day) {
                createDay(dayId);
                day = document.getElementById(dayId);
            } else {
                const lessonsList = day.getElementsByClassName("lesson");
                lessonsList[1].style.marginTop = "20px";
                lessonsList[2].style.marginTop = "20px";
                lessonsList[3].style.marginTop = "20px";
            }

            let dayLessons = lessons[dayId];
            dayLessons.forEach(lesson => {
                let html = '<div class="lesson" id="lesson">';
                html += `<p>Time: ${lesson.starttime.slice(0, 5)} - ${lesson.endtime.slice(0, 5)}</p>`;
                html += `<p>Teacher: ${lesson.firstname} ${lesson.surname} </p>`;

                if (!lesson.paid) {
                    html += `<button class="upCLessonButtonPayment" id="upCLessonButtonPayment" onclick="redirectToPayment(${lesson.lid},${lesson.tid})">Pay</button>`;
                    html += `<button class="upCLessonButtonCancel" id="upCLessonButtonCancel" onclick="sendCancelNotification(${lesson.lid},${lesson.tid}).then(() => cancelLesson(${lesson.lid},${lesson.tid}));">Cancel</button>`;
                } else {
                    html += "<p>Paid</p>"
                }
                html += '</div>';


                day.innerHTML += html;
            });
        });
    })
    .catch(error => console.log(error));

function  redirectToPayment(lid, tid) {
    location.href="./payment.jsp?tid=" + tid + "&lid=" + lid;
}
/**
 * takes the day ID to create a different day for each one of them by making the title and the day element to which lessons are appended
 * @param dayId
 */
function createDay(dayId) {
    let buttonText = showDate(dayId);


    let html = `
        <div id="${dayId}" class="days">
            <button class="lesson-button">${buttonText}</button>
        </div>
    `;

    let resultElement = document.getElementById('result');
    resultElement.innerHTML += html;
}


/**
 * takes the day id
 * @param dayId, which is the date
 * @returns {string} of the date in a clear way
 */
function showDate(dayId) {
    let [dayOfWeek, weekOfYear, year] = dayId.split('-').map(Number);
    let date = new Date(year, 0, 1);

    date.setDate(date.getDate() + (weekOfYear - 1) * 7);
    date.setDate(date.getDate() + (dayOfWeek - date.getDay()));

    let day = date.getDate();
    let month = date.getMonth() + 1;

    let daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    let dayOfWeekText = daysOfWeek[date.getDay()];

    let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    let monthText = months[date.getMonth()];

    return dayOfWeekText + ", " + day + "/" + monthText + "/" + year;
}


/**
 * Takes the lesson and teacher id to delete one specific lesson, this is tied to the cancel button.
 * @param lid
 * @param tid
 * @returns {Promise<void>}
 */
async function cancelLesson(lid, tid) {
    try {
        const url = `./api/students/student/cancelLesson?lid=${lid}&tid=${tid}`;
        await fetch(url, {
            method: 'DELETE'
        });

        window.location.reload();
    } catch (error) {
        console.error('An error occurred:', error);
    }
}

/**
 * Takes the lesson and teacher ID to send a notification if a lesson is canceled
 * @param lid
 * @param tid
 * @returns {Promise<void>}
 */
async function sendCancelNotification(lid, tid) {
    let notification = {
        tid: tid,
        message: "Lesson booked",
        from: 0
    };

    fetch(`./api/teachers/${tid}/teacher_cancel_notification?lid=${lid}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(notification)
    })
        .then(response => {
            if (response.ok) {
                console.log('Notification sent!');
                console.log(lid)
            } else {
                console.error('Failed to send notification.');
                console.log(lid)
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


