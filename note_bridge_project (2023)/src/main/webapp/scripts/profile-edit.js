const profilePage = document.getElementById("profile");
const preferencesPage = document.getElementById("address");

let instruments = null;
let instrumentSelection;

async function sendUpdate() {
    let firstName = document.getElementsByName("firstname")[0].value;
    let surname = document.getElementsByName("surname")[0].value;
    let street = document.getElementsByName("street")[0].value;
    let newHouseNumber = document.getElementsByName("new-house-number")[0].value;
    let city = document.getElementsByName("city")[0].value;
    let newZip = document.getElementsByName("new-zip")[0].value;
    let phoneNumber = document.getElementsByName("phone-number")[0].value;
    let email = document.getElementsByName("new-email")[0].value;
    let currentPassword = document.getElementsByName("current-password")[0].value;
    let newPassword = document.getElementsByName("new-password")[0].value;
    let tid; let sid;
    const profilePicture = document.getElementsByName("avatar")[0];
    const video = document.getElementsByName("video")[0];

    if (document.getElementById("tid") != null && document.getElementById("tid").value !== undefined) {
        tid = document.getElementById("tid").value;
    }
    else if (document.getElementById("sid") != null && document.getElementById("sid").value !== undefined) {
        sid = document.getElementById("sid").value;
    }

    if (profilePicture.files.length > 0) {
        const formData = new FormData();
        formData.append('picture', profilePicture.files[0]);
        console.log("uploading picture");
        console.log('fetching picture url');
        await fetch(`api/person/update-profile-picture`, {
            method: 'PUT',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    console.log('Update sent!');
                } else {
                    console.error('Failed to send update.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    if (tid !== null && tid !== 0 && tid !== undefined) {
        let update = {
            tid: tid,
            firstname: firstName,
            surname: surname,
            phoneNum: phoneNumber,
            email: email,
            street: street,
            newHouseNum: newHouseNumber,
            city: city,
            newPostalCode: newZip,
            instruments: instrumentSelection
        }

        console.log('fetching url');
        await fetch(`api/teachers/${tid}/update`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(update)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Update sent!');
                } else {
                    console.error('Failed to send update.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });

        if (newPassword !== null && newPassword !== undefined && newPassword !== "") {
            let password = currentPassword + ' ' + newPassword;
            console.log(password);
            console.log('fetching password url');
            await fetch(`api/teachers/${tid}/update-password`, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(password)
            })
                .then(response => {
                    if (response.ok) {
                        console.log('Update sent!');
                    } else {
                        console.error('Failed to send update.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }

        if (video.files.length > 0) {
            const formData = new FormData();
            formData.append('video', video.files[0]);
            console.log("uploading video");
            console.log('fetching video url');
            await fetch(`api/teachers/${tid}/update-video`, {
                method: 'PUT',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        console.log('Update sent!');
                    } else {
                        console.error('Failed to send update.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }
    else if (sid !== null && sid !== 0 && sid !== undefined) {
        let update = {
            sid: sid,
            firstname: firstName,
            surname: surname,
            phoneNum: phoneNumber,
            email: email,
            street: street,
            newHouseNum: newHouseNumber,
            city: city,
            newPostalCode: newZip,
            instruments: instrumentSelection
        }

        console.log('fetching url');
        await fetch(`api/students/student/update`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(update)
        })
            .then(response => {
                if (response.ok) {
                    console.log('Update sent!');
                } else {
                    console.error('Failed to send update.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });

        if (newPassword !== null && newPassword !== undefined && newPassword !== "") {
            let password = currentPassword + " " + newPassword;
            console.log(password);
            console.log('fetching password url');
            await fetch(`api/students/student/update-password`, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(password)
            })
                .then(response => {
                    if (response.ok) {
                        console.log('Update sent!');
                    } else {
                        console.error('Failed to send update.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }
    location.href = 'profile.jsp';
}

function instrumentsToText(instruments) {
    let res = ""
    for (let i = 0; i < instruments.length; i++) {
        res += instruments[i]
        if (i < instruments.length - 1) {
            res += " ";
        }
    }
    return res;
}

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
    if (instruments !== null) {
        for (let i = 0; i < instruments.length; i++) {
            let instrument = instruments[i];
            result += "<li><div class=\"map-filter-icon\" style=\"background: url('" + instrument.url + "'); background-size: contain; background-position: center; background-repeat: no-repeat;\"></div>";
            result += "<p id=\"test\">" + instrument.name.charAt(0).toUpperCase() + instrument.name.slice(1) + "</p>";
            result += "<input type=\"checkbox\" name='instruments[]' value='" + instrument.name + "' oninput='updateSelectedInstruments()'></li>";
        }
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
    instrumentSelection = selectedInstruments.join(" ");
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