const profileTemplate = "<div class=\"teacher\" onclick=\"teacherprofile(%tid)\">\n" +
"  <div class=\"teacher-content\">\n" +
"    <div>\n" +
"      <p class=\"name\">%name</p>\n" +
"      <img src=\".%picture\" alt=\"profile-pic\"/>\n" +
"    </div>\n" +
"    <p class=\"rating\">%rating / 5</p>\n" +
"    <p class=\"instruments\">%instruments</p>\n" +
"    <p class=\"location\">%location</p>\n" +
"  </div>\n" +
"</div>"

const scroll = document.getElementById("teachers");

function teacherprofile(tid) {
    location.href = "./profile.jsp?tid=" + tid;
}

let teachers = null;

async function loadTeachers() {
    teachers = await fetch('./api/teachers', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => response.json());

    let t = "";
    for (let i = 0; i < teachers.length; i++) {
        let teacher = teachers[i];
        t += profileTemplate.replace("%tid", teacher.tid)
            .replace("%name", teacher.firstname + " " + teacher.surname)
            .replace("%rating", teacher.rating)
            .replace("%rate", teacher.rate)
            .replace("%instruments", teacher.instruments.map(instrument => { return instrument.charAt(0).toUpperCase() + instrument.slice(1)}).join(", "))
            .replace("%location", teacher.city)
            .replace("%picture", teacher.picture);
    }
    scroll.innerHTML = t;
}

function filterTeachers() {
    let nameFilter = document.getElementById("name").value;
    let locationFilter = document.getElementById("location").value;
    let instrumentFilter = document.getElementById("instrument").value;
    let ratingMinFilter = document.getElementById("rating-min").value;
    let ratingMaxFilter = document.getElementById("rating-max").value;
    let teachers = document.getElementsByClassName("teacher");
    for (let index = 0; index < teachers.length; index++) {
        let teacher = teachers[index];
        let name = teacher.getElementsByClassName("name")[0].innerText;
        let location = teacher.getElementsByClassName("location")[0].innerText;
        let instruments = teacher.getElementsByClassName("instruments")[0].innerText;
        let rating = parseFloat(teacher.getElementsByClassName("rating")[0].innerText);
        if (name.toLocaleLowerCase().includes(nameFilter.toString().toLocaleLowerCase())
            && location.toLocaleLowerCase().includes(locationFilter.toString().toLocaleLowerCase())
            && instruments.toLocaleLowerCase().includes(instrumentFilter.toString().toLocaleLowerCase())
            && ratingMinFilter <= rating && ratingMaxFilter >= rating){
            teacher.style.display = null;
        } else {
            teacher.style.display = "none";
        }
    }
}