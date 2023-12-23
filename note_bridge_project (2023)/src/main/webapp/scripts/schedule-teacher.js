let currentMonth = 0;
let currentYear = 0;

let scheduleWeek = 0;
let scheduleYear = 0;

/**
 * Generates the calendar
 * @param year the year for the calendar
 * @param month the month for the calendar
 */
function generateCalendar(year, month) {
    // Get the first day of the month
    const firstDay = new Date(year, month, 1);
    // Get the number of days in the month
    const lastDay = new Date(year, month + 1, 0).getDate();

    // Clear the calendar grid
    const calendarGrid = document.querySelector('.my-calendar-grid');
    calendarGrid.innerHTML = '';

    // starting day
    const startDay = (firstDay.getDay() - 1);

    // Add empty cells for days before the starting day of the month
    for (let i = 0; i < startDay; i++) {
        const emptyCell = document.createElement('div');
        emptyCell.classList.add('day-cell');
        calendarGrid.appendChild(emptyCell);
    }

    // Add cells for each date of the month

    for (let date = 1; date <= lastDay; date++) {
        const dayCell = document.createElement('div');
        dayCell.classList.add('day-cell');
        if (month === getCurrentDate().month && date === getCurrentDate().date) {
            dayCell.classList.add('day-cell-today');
        }
        dayCell.textContent = date.toString();
        dayCell.addEventListener('click', () => switchToWeek(year, month, date));
        calendarGrid.appendChild(dayCell);
    }

    const currentMonth = new Date(year, month).toLocaleString('default', {month: 'long', year: 'numeric'});
    document.getElementById("current-month-year").textContent = currentMonth;
}

/**
 * Shows the current month.
 */
function showMonth() {
    const currentDate = getCurrentDate();
    currentMonth = currentDate.month;
    currentYear = currentDate.year;

    generateCalendar(currentYear, currentMonth);
}

/**
 * Show the current week in the schedule.
 */
function showWeek(){
    const currentDate = getCurrentDate();

    scheduleWeek = getWeekNumber(new Date());
    scheduleYear = new Date().getFullYear();

    loadWeek(scheduleWeek, currentYear);
    switchToWeek(currentDate.year, currentDate.month, currentDate.date);
}

/**
 * Gets the current date.
 * @returns {{date: number, month: number, year: number, monthName: string}}
 */
function getCurrentDate() {
    const today = new Date();
    return {
        year: today.getFullYear(),
        month: today.getMonth(),
        date: today.getDate(),
        monthName: today.toLocaleString('en-us', { month: 'long' })
    };
}

/**
 * Switches to the next or previous month depending on the direction (1 or -1).
 * @param direction the direction
 */
function switchMonth(direction) {
    if (currentYear === 0) {
        const currentDate = getCurrentDate();
        currentYear = currentDate.year;
    }

    currentMonth += direction;

    if (currentMonth < 0) {
        currentYear--;
        currentMonth = 11;
    } else if (currentMonth > 11) {
        currentYear++;
        currentMonth = 0;
    }
    generateCalendar(currentYear, currentMonth);
}

/**
 * Opens the calendar.
 */
function openCalendar() {
    document.getElementById("calendar").style.display = null;
}

/**
 * Closes the calendar when the blackish space is pressed.
 * @param e the event to check what is pressed
 */
function closeCalendar(e) {
    if (e.target === document.getElementById("calendar"))
        document.getElementById("calendar").style.display = "none";
}

/**
 * Switches the schedule to the week given.
 * @param year the year
 * @param month the month
 * @param date the day
 */
//switches to the correct week if a date-cell is clicked on
function switchToWeek(year, month, date) {
    const targetDate = new Date(year, month, date);
    const startOfWeek = new Date(targetDate);
    const dayOfWeek = startOfWeek.getDay();

    const diff = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
    startOfWeek.setDate(startOfWeek.getDate() - diff);

    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(endOfWeek.getDate() + 6);

    const schedule = document.getElementById("schedule");
    let currentDate = new Date(startOfWeek);


    scheduleWeek = getWeekNumber(targetDate);
    scheduleYear = year;
    loadWeek(scheduleWeek, year);

    while (currentDate <= endOfWeek) {
        const dayText = currentDate.toLocaleString('default', {day: '2-digit', month: 'long'});
        const dayOfWeek = (currentDate - startOfWeek) / (1000 * 60 * 60 * 24);
        if (currentDate.getDate() === getCurrentDate().date) {
            schedule.children[dayOfWeek + 1].getElementsByClassName("type")[0].style.background = "var(--button-selected-color)";
        } else {
            schedule.children[dayOfWeek + 1].getElementsByClassName("type")[0].style.background = null;
        }
        schedule.children[dayOfWeek + 1].getElementsByClassName("date")[0].innerText = dayText;

        // Move to the next day
        currentDate.setDate(currentDate.getDate() + 1);
    }
}

/**
 * A function that will execute when a slot is clicked.
 * @param element the slot
 */
function onSlot(element) {
    const time = element.getAttribute("data-time");
    const day = element.getAttribute("data-day");
    const date = element.parentElement.getElementsByClassName("date")[0].innerText;

    if (element.classList.contains("active")) {
        element.classList.toggle("active",false);
        deleteSchedule(day, scheduleWeek, scheduleYear, time, parseInt(time.slice(0, 2)) + 1 + time.slice(2));
    } else {
        element.classList.toggle("active",true);
        createAvailability(day, scheduleWeek, scheduleYear, time, parseInt(time.slice(0, 2)) + 1 + time.slice(2));
    }
}

/**
 * Gets the time slot with the day of the week and the time.
 * @param day the day of the week
 * @param time the time of the slot
 * @returns {Element} the time slot
 */
function getSlot(day, time) {
    return document.querySelector(`.slot[data-day="${day}"][data-time="${time}"]`);
}

/**
 * Gets the week-number of the given date.
 * @param date the date
 * @returns {number} the week number
 */
function getWeekNumber(date) {
    return Math.ceil((((date - new Date(date.getFullYear(), 0, 1)) / 86400000) + new Date(date.getFullYear(), 0, 1).getDay() + 1) / 7);
}

/**
 * Loads the availability of the week.
 * @param weekNumber the week number
 * @param year the year
 * @returns {Promise<void>}
 */
async function loadWeek(weekNumber, year) {
    let tid = document.getElementById("tid").value;

    const data = {
        weekNumber: weekNumber,
        year: year
    };

    const params = new URLSearchParams(data).toString();

    const availabilities = await fetch(`./api/teachers/${tid}/availability?${params}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => response.json()).catch((e) => { console.log(e.toString()) });

    document.querySelectorAll(`.slot[data-day][data-time]`).forEach((slot) => {
        slot.classList.toggle("active", false);
        slot.classList.toggle("closed", false);
    })

    for (let i = 0; i < availabilities.length; i++) {
        const availability = availabilities[i];
        const slot = getSlot(availability.dayOfWeek, availability.startTime.slice(0, 5));

        if (availability.open) {
            slot.classList.toggle("active", true);
        } else {
            slot.classList.toggle("closed", true);
        }
    }
}

async function createAvailability(day, week, year, startTime, endTime) {
    const data = {
        "dayOfWeek" : day,
        "weekOfYear" : week,
        "year" : year,
        "startTime" : startTime + ":00",
        "endTime" : endTime + ":00"
    }

    await fetch(`./api/teachers/availability`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).catch((e) => {console.log(e.toString())});
}

async function deleteSchedule(day, week, year, startTime, endTime) {
    const data = {
        "dayOfWeek" : day,
        "weekOfYear" : week,
        "year" : year,
        "startTime" : startTime + ":00",
        "endTime" : endTime + ":00"
    }

    await fetch(`./api/teachers/availability`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).catch((e) => {console.log(e.toString())});
}