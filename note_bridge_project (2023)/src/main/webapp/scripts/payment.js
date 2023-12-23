let searchParams = new URLSearchParams(location.search);
let tid = searchParams.get("tid");
let lid = searchParams.get('lid');
const page1 = document.getElementById("page-1");
const page2 = document.getElementById("page-2");
const pageBack = document.getElementById("page-back");
const pageNext = document.getElementById("page-next");
const buttonSubmit = document.getElementById("submit");
const pageError = document.getElementById("page-error");
window.onload = sendBack();
const bankSelector = document.getElementById("bank-selector");
const bankCode = bankSelector.value;
console.log(bankCode);

async function checkLessonStatus(){
    let status = false;
    await fetch(`api/students/student/is_paid?lid=${lid}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            }
    }
    )
        .then(response => response.json())
        .then(result => status = result)
        .catch(error => {
            console.error('Error:', error);
        });
    console.log(status);
    return status;
}

async function sendBack(){
    await getStudentEmail();
    await getTeacherRate();
    if(await checkLessonStatus() === true){
        page1.style.display = "none";
        page2.style.display = "none";
        pageNext.style.display = "none";
        pageError.style.display = null;
    }
}
function getPID() {
    let params = new URLSearchParams(location.search);
    return params.get('pid');
}

function getInfo(){
    let student =  fetch('./api/students/profile', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
        },
    }).then(response => response.json()).catch(() => {
        location.href = "./error.html?msg=could not show profile page"
    });

    let person = {
        firstname: student.firstname,
        lastname: student.surname,
        email: getStudentEmail()
    }

    return person;
}

async function getTeacherRate(){
    let rate = 0;
    await fetch(`api/teachers/${tid}/getrate`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            }
        }
    )
        .then(response => response.json())
        .then(result => rate = result)
        .catch(error => {
            console.error('Error:', error);
        });
    console.log(rate);
    return rate;
}

async function getStudentEmail(){
    let studentEmail = "";
    await fetch(`api/students/student/getstudentemail`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            }
        }
    )
        .then(response => response.text())
        .then(result => studentEmail = result)
        .catch(error => {
            console.error('Error:', error);
        });
    console.log(studentEmail);
    return studentEmail
}

let paymentInfo = null;

async function fetchPayment(){
    let amount = await getTeacherRate();
    let amountString = amount.toString();
    paymentInfo = null;
    var myHeaders = new Headers();
    myHeaders.append("authorization", "Basic MTAwMTpNZVlhU2cxWE9vaEllWm9USkdiMU43dHkxV3E4VkpQQ2dwY3o3V05LUEhPOWdNMXRVY0hSQUNYZXFCME5WOG9OcWE0dQ==");
    myHeaders.append("content-type", "application/json");
    myHeaders.append("accept", "application/json");
    myHeaders.append("Host", "secure-staging.curopayments.net");

    //creating the parameters to send to the payment provider
    var raw = JSON.stringify({
        "site_id": "1015",
        "currency_id": "EUR",
        "amount": amountString,
        "reference": "Reference 1",
        "description": "Notebridge lesson payment",
        "url_success": "http://www.example.com/ok.html",
        "url_pending": "http://www.example.com/pending.html",
        "url_failure": "http://www.example.com/wrong.html",
        "url_callback": "http://www.example.com/callback.html",
        "issuer_id": bankCode,
        "recurring": 1,
        "country_id": "NL",
        "language_id": "nl",
        "ip": "192.168.0.1",
        "consumer": getInfo(),
        "cartitems": [
            {
                "quantity": 1,
                "sku": "nb_lesson",
                "name": "Notebridge lesson",
                "price": amount,
                "vat": 21,
                "vat_inc": true,
                "type": 1
            }
        ]
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    let res = await fetch("https://secure-staging.curopayments.net/rest/v1/curo/payment/ideal/", requestOptions)
        .then(response => response.json())
        .then(result => paymentInfo = result)
        .catch(error => console.log('error', error));
}

async function getPaymentStatus(){
    await fetchPayment();
    console.log(paymentInfo)
    return paymentInfo.success;
}

//page navigation
async function nextPage() {
    var e = document.getElementById("bank-selector");
    var value = e.value;
    var text = e.options[e.selectedIndex].text;
    if (page2.style.display === "none") {
        page1.style.display = "none";
        page2.style.display = null;
        document.getElementById("bank").innerHTML ="Bank: " + text;
        document.getElementById("rate").innerHTML ="Lesson price: " + await getTeacherRate();
        pageNext.style.display = "none";
        buttonSubmit.style.display = null;
        pageBack.style.display = null;
    }
}

function prevPage(){
    if(page1.style.display === "none") {
        page1.style.display = null;
        buttonSubmit.style.display = "none";
        pageNext.style.display = null;
        pageBack.style.display = "none";
    }
}
//submit the payment information and await the response from the provider
async function submit() {
    let isPaid = await getPaymentStatus();
    await fetch(`api/students/student/paid?lid=${lid}&isPaid=${isPaid}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
        }
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
    if(isPaid){
        location.href="my-schedule.jsp"
    }else {
        page2.style.display = "none";
        page1.style.display = "none";
        buttonSubmit.style.display = "none";
        pageNext.style.display = "none";
        pageBack.style.display = null;
    }
}



