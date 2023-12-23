const initialPage = document.getElementById("page-forget-form");
const validEmailPage = document.getElementById("valid-email");
const buttonPage = document.getElementById("page-buttons");
const page1Header = document.getElementById("page1-header");

function validEmail(){
    //check for valid email
    return true;
}

function buttonForget(){
    if(validEmail()===true){
        validEmailPage.style.display = null;
        initialPage.style.display = "none";
        buttonPage.style.display = "none"
        page1Header.style.display = "none"
    }
    else{
        validEmailPage.style.display = "none";
        initialPage.style.display = "none";
        buttonPage.style.display = "none"
        page1Header.style.display = "none"
    }
}