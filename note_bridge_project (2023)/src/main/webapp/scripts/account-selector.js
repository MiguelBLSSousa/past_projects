const teacherAccount = document.getElementById("teacher-account");
const studentAccount = document.getElementById("student-account");
const address = document.getElementById("address");
const teacherRegister = document.getElementById("teacher");
const studentRegister = document.getElementById("student");
const instrumentsPage = document.getElementById("instruments-page");
const page1 = document.getElementById("page-1");
const accountValue = document.getElementById("account-value");
const pageNext = document.getElementById("page-next");
const pageBack = document.getElementById("page-back");
const submitButton = document.getElementById("submit");
const progressbar = document.getElementById("progress-bar-slider");

accountValue.value = "student";
pageBack.style.display = "none";
submitButton.style.display = "none";
instrumentsPage.style.display = "none";

function toggleAccount(accountType) {
    if (accountType === "teacher") {
        accountValue.value = "teacher";
        if (!teacherAccount.classList.contains("selected")) {
            teacherAccount.classList.add("selected");
            studentAccount.classList.remove("selected");
        }
    } else if (accountType === "student") {
        accountValue.value = "student";
        if (!studentAccount.classList.contains("selected")) {
            studentAccount.classList.add("selected");
            teacherAccount.classList.remove("selected");
        }
    }
}

function nextPage() {
    if (accountValue.value === "student") {
        if (address.style.display === "none") {
            if (!checkpage1()) return;
            setProgress(137);
            teacherAccount.style.display = "none";
            address.style.display = null;
            pageBack.style.display = null;
            page1.style.display = "none";
        } else {
            if (!checkaddress()) return;
            setProgress(275);
            studentRegister.style.display = null;
            address.style.display = "none";
            submitButton.style.display = null;
            pageNext.style.display = "none";
            instrumentsPage.style.display = null;
        }
    } else if (accountValue.value === "teacher") {
        if (address.style.display === "none") {
            if (!checkpage1()) return;
            setProgress(137);
            studentAccount.style.display = "none";
            address.style.display = null;
            pageBack.style.display = null;
            page1.style.display = "none";
        } else {
            if (!checkaddress()) return;
            setProgress(275);
            teacherRegister.style.display = null;
            address.style.display = "none";
            submitButton.style.display = null;
            pageNext.style.display = "none";
            instrumentsPage.style.display = null;
        }
    }
}

function prevPage() {
    if (accountValue.value === "student") {
        if (studentRegister.style.display === "none") {
            setProgress(30);
            teacherAccount.style.display = null;
            address.style.display = "none";
            pageBack.style.display = "none";
            page1.style.display = null;
            submitButton.style.display = "none";
        } else {
            setProgress(137);
            studentRegister.style.display = "none";
            address.style.display = null;
            submitButton.style.display = "none";
            instrumentsPage.style.display = "none";
        }
        pageNext.style.display = null;
    } else if (accountValue.value === "teacher") {
        if (teacherRegister.style.display === "none") {
            setProgress(30);
            studentAccount.style.display = null;
            address.style.display = "none";
            pageBack.style.display = "none";
            page1.style.display = null;
            submitButton.style.display = "none";
        } else {
            setProgress(137);
            teacherRegister.style.display = "none";
            address.style.display = null;
            submitButton.style.display = "none";
            instrumentsPage.style.display = "none";
        }
        pageNext.style.display = null;
    }
}

let instruments = null;

fetch('./json/instruments.json', {
    method: 'GET',
    headers: {
        'Accept': 'application/json',
    },
}).then(response => response.json())
    .then(response => {
            instruments = response;
        }
    ).then(loadInstruments);

function loadInstruments() {
    let filter = document.getElementById("instruments-filter");
    let result = "";
    for (let i = 0; i < instruments.length; i++) {
        let instrument = instruments[i];
        result += "<li><div class=\"map-filter-icon\" style=\"background: url('" + instrument.url + "'); background-size: contain; background-position: center; background-repeat: no-repeat;\"></div>";
        result += "<p id=\"test\">" + instrument.name.charAt(0).toUpperCase() + instrument.name.slice(1) + "</p>";
        result += "<input type=\"checkbox\" name='instruments[]' value='" + instrument.name + "' oninput='updateSelectedInstruments()'></li>";
    }
    filter.innerHTML = result;
    updateSelectedInstruments();
}

function updateSelectedInstruments() {
    let selectedInstruments = [];
    let checkboxes = document.querySelectorAll("input[name='instruments[]']:checked");
    checkboxes.forEach(function (checkbox) {
        selectedInstruments.push(checkbox.value);
    });
    document.getElementById("instruments-input").value = selectedInstruments.join(",");
    console.log(selectedInstruments)
}

function searchInstruments() {
    let search = document.getElementById("search").value;
    let instrumentFilter = document.getElementById("instruments-filter");
    for (let index = 0; index < instrumentFilter.childElementCount; index++) {
        const instrument = instrumentFilter.children[index].getElementsByTagName("p")[0].innerText;
        if (!instrument.toLowerCase().includes(search.toLowerCase())) {
            instrumentFilter.children[index].style.display = "none";
        } else {
            instrumentFilter.children[index].style.display = null;
        }
    }
}
loadInstruments();

function setProgress(value) {
    progressbar.style.width = value + "px";
}

function checkpage1() {
    let sid = document.getElementById("account-value").value;
    let firstname = document.getElementsByName("firstname")[0].value;
    let surname = document.getElementsByName("surname")[0].value;
    let phone = document.getElementsByName("phone")[0].value;
    let email = document.getElementsByName("email")[0].value;
    let password1 = document.getElementsByName("password")[0].value;
    let password2 = document.getElementsByName("passwordcheck")[0].value;

    let regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return (sid === "student" || sid === "teacher")
        && (firstname !== undefined && firstname !== "")
        && (surname !== undefined && surname !== "")
        && (phone !== undefined && phone !== "")
        && email.match(regex) &&
        (password1 === password2 && password1 !== undefined && password1.length > 8);
}

function checkaddress() {
    let street = document.getElementsByName("street")[0].value;
    let housenumber = document.getElementsByName("housenumber")[0].value;
    let city = document.getElementsByName("city")[0].value;
    let zip = document.getElementsByName("zip")[0].value;

    return (street !== undefined && street !== "")
        && (housenumber !== undefined && housenumber !== "")
        && (city !== undefined && city !== "")
        && (zip !== undefined && zip !== "");
}
