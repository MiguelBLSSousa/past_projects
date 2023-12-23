async function loadStudent() {
    const name = document.getElementById("student-name");
    const city = document.getElementById("student-location");
    const instruments = document.getElementById("student-instruments");
    const schedule = document.getElementById("schedule");
    const studentPicture = document.getElementById("student-picture");
    //gets the right profile based on the session id, for security reasons
    const student = await fetch('./api/students/profile', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => response.json()).catch(() => { location.href = "./error.html?msg=could not show profile page" });
    //for displaying student information
    name.innerText = student.firstname + " " + student.surname;
    city.innerText = student.city;
    instruments.innerHTML = createInstruments(student.instruments);
    studentPicture.src = "."+student.picture;
    //displays the begin- and endtime of the student's schedule for every day
    for (let i = 0; i < schedule.children.length; i++) {
        const day = schedule.children[i];
        day.innerText = getMinMaxTime(student.lessons, i + 1); // Call the getMinMaxTime function with the appropriate arguments
    }
}

function createInstruments(instruments) {
    const levels = ["beginner", "intermediate", "advanced", "expert"];

    //html for 1 instrument inside the expertise element
    const instrument = `
      <div class="item">
        <div class="image" style="background-size: contain; background-position: center; background-repeat: no-repeat; background-image: URL(.%iconpath)" alt=""></div>
        <div>
          <div class="instrument-level">
            <p class="instrument">%instrument</p>
            <p class="level">%level</p>
          </div>
          <div class="progress-bar">
            <input type="range" min="0" max="243" step="27" value="%master" class="slider"/>
          </div>
        </div>
      </div>`;

    //gets the value of the expertise and displays which level the student is at
    let res = "";
    for (let i = 0; i < instruments.length; i++) {
        let levelIndex = parseInt(instruments[i].split(':')[1]);
        let level = "";
        if (levelIndex >= 0 && levelIndex <= 2) {
            level = levels[0]; // beginner
        } else if (levelIndex >= 3 && levelIndex <= 5) {
            level = levels[1]; // intermediate
        } else if (levelIndex >= 6 && levelIndex <= 8) {
            level = levels[2]; // advanced
        } else if (levelIndex >= 9 && levelIndex <= 10) {
            level = levels[3]; // expert
        }
        //replaces the placeholders in the html with actual values
        res += instrument.replace("%instrument", instruments[i].split(':')[0])
            .replace("%level", level)
            .replace("%master", (levelIndex * 27).toString())
            .replace("%iconpath", instruments[i].split(':')[2]);
    }
    return res;
}

/**
 * updates the expertise to the database using the values of the instrument and the slider
 * @param instruments, all the instruments you want to update
 */
function updateExpertise(instruments) {
    for (let i = 0; i < instruments.length; i++) {
        let instrumentElement = instruments[i];
        let instrument = instrumentElement.getElementsByClassName('instrument')[0].textContent;
        let levelElement = instrumentElement.getElementsByClassName('slider')[0];
        let level = levelElement.value / 27;

        const update = {
            instrument: instrument,
            expertise: level,
        };

        fetch(`api/students/student/expertise`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(update)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Feedback sent!');
                } else {
                    console.error('Failed to send feedback.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}
/**
 * for displaying the begin- and endtime of the teacher availability on the profile page
 * @param days, all the days you want to get the min and max time for
 * @param dayOfWeek, the current day of the week
 * @returns {string}, the minimum and maximum schedule on a day seperated by a '-'
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

loadStudent();