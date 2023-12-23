async function loadTeacher() {
    //html for a single review
    const reviewTemplate = "<div class=\"review\">\n" +
        "     <div>\n" +
        "        <h2 class=\"name\">%name</h2>\n" +
        "        <h2 class=\"rating\">%rating / 5</h2>\n" +
        "    </div>\n" +
        "    <p class=\"text\">%text</p>\n" +
        "</div>";

    const tid = document.getElementById("tid").value;

    if (tid.length === 0) {
        location.href = "./profile.jsp";
    }

    const name = document.getElementById("name");
    const city = document.getElementById("location");
    const rating = document.getElementById("rating");
    const rate = document.getElementById("rate");
    const instruments = document.getElementById("teacher-instruments");
    const availability = document.getElementById("availability");
    const reviews = document.getElementById("reviews");
    const teacherPicture = document.getElementById("teacher-picture");
    const video = document.getElementById("video");
    const teacher = await fetch('./api/teachers/' + tid, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => response.json()).catch(() => { location.href = "./profile.jsp" });

    const reviewsJson = await  fetch('./api/teachers/' + tid + "/review", {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        }
    }).then(response => response.json()).catch((e) => { console.log("ERROR WITH REVIEWS\n" + e.toString()) });

    //for displaying the teacher information on their profile page
    name.innerText = teacher.firstname + " " + teacher.surname;
    city.innerText = teacher.city;
    rating.innerText = teacher.rating + " / 5";
    rate.innerText = "€" + teacher.rate + "/hour";
    teacherPicture.src = "."+teacher.picture;

    //for adding videos
    const source = document.createElement('source');
    source.setAttribute('src', "."+teacher.video);
    video.appendChild(source);
    video.load();
    instruments.innerHTML = instrumentsToText(teacher.instruments);

    //for displaying the availability on the profile page
    for (let i = 0; i < availability.children.length; i++) {
        const day = availability.children[i];
        day.innerText = getMinMaxTime(teacher.availabilities, i + 1); // Call the getMinMaxTime function with the appropriate arguments
    }

    //for displaying the three lowest reviews on the profile page
    let rvs = "";
    for (let i = 0; i < Math.min(3, reviewsJson.length); i++) {
        let review = reviewsJson[i];
        rvs += reviewTemplate.replace("%name", review.reviewer).replace("%rating", review.rating).replace("%text", review.review);
    }

    reviews.innerHTML = rvs;
}

function getTID(){
    let params = new URLSearchParams(location.search);
    return params.get('tid');
}

//for displaying all the instruments on the profile page
function instrumentsToText(instruments) {
    let res = ""
    for (let i = 0; i < instruments.length; i++) {
        res += instruments[i]
        if (i < instruments.length - 1) {
            res += " <span>&</span> ";
        }
    }
    return res;
}

/**
 * for displaying the begin- and endtime of the teacher availability on the profile page
 * @param days, all the days you want to get the min and max time for
 * @param dayOfWeek, the current day of the week
 * @returns {string}, the minimum and maximum availability on a day seperated by a '-'
 */
function getMinMaxTime(days, dayOfWeek) {
    const allTimes = [];
    for (let i = 0; i < days.length; i++) {
        const day = days[i];
        if (day.dayOfWeek === dayOfWeek) {
            allTimes.push(day);
        }
    }
    if (allTimes.length === 0) {
        return "-"
    }
    let minTime = '23:59:59';
    let maxTime = '00:00:00';

    for (const day of allTimes) {
        if (day.startTime < minTime) {
            minTime = day.startTime;
        }

        if (day.endTime > maxTime) {
            maxTime = day.endTime;
        }
    }

    const minTimeHours = minTime.slice(0, 2);
    const maxTimeHours = maxTime.slice(0, 2);

    return minTimeHours + " - " + maxTimeHours;
}

loadTeacher();