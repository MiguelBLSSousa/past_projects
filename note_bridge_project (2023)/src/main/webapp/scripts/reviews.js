
//html for a single review
async function loadReviews(){
    const reviewTemplate = "<div class=\"review\">\n" +
        "     <div>\n" +
        "        <h2 class=\"name\">%name</h2>\n" +
        "        <h2 class=\"rating\">%rating / 5</h2>\n" +
        "    </div>\n" +
        "    <p class=\"text\">%text</p>\n" +
        "</div>";


    let searchParams = new URLSearchParams(location.search);
    let tid = searchParams.get("tid");

const reviewsJson = await  fetch('./api/teachers/' + tid + "/review", {
    method: 'GET',
    headers: {
        'Accept': 'application/json',
    }
}).then(response => response.json()).catch((e) => { console.log("ERROR WITH REVIEWS\n" + e.toString()) });


//create a new review element for every review we get from the fetch
let rvs = "";
for (let i = 0; i < reviewsJson.length; i++) {
    let review = reviewsJson[i];
    rvs += reviewTemplate.replace("%name", review.reviewer).replace("%rating", review.rating).replace("%text", review.review);
}
reviews.innerHTML = rvs;
}

loadReviews();