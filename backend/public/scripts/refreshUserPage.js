const TIME_UNTIL_REFRESH_SECONDS = 5

// Function that refreshes the page every 5 seconds.
function refreshPage(event) {
    const secondsLeftSpan = document.getElementById("logic-time-left")
    let secondsLeft = TIME_UNTIL_REFRESH_SECONDS

    secondsLeftSpan.innerText = secondsLeft
    
    setInterval(function() {
        secondsLeft--
        secondsLeftSpan.innerText = secondsLeft

        if(secondsLeft <= 0) {
            location = '/'
        }
    }, 1000)
}

window.addEventListener("load", refreshPage)