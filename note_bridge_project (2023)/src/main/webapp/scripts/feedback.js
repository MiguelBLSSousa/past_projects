
let rating = document.getElementById('myRange');
let ratingValue = document.getElementById("ratingValue");
//for displaying the value above the slider
ratingValue.innerHTML = rating.value;

rating.oninput = function() {
    //for displaying the value above the slider
    ratingValue.innerHTML = this.value;
}

function getTID(){
    let params = new URLSearchParams(location.search);
    return params.get('tid');
}

/**
 * sends the feedback to the database
 */
function sendFeedback() {
    let rating = document.getElementById('myRange').value;
    let message = document.getElementById('message').value;
    let tid = getTID();

    let feedback = {
        rating: rating,
        review: sanitizeHTML(message),
        tid: tid
    };

    fetch(`api/teachers/${tid}/feedback`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(feedback)
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

function sanitizeHTML(str) {
    str = str.replace('<', '');
    str = str.replace('>', '');
    str = str.replace('"', '');
    return str;
}