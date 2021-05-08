const TIME_UNTIL_REFRESH_SECONDS = 5

const LOADING_BLOCKS_PER_SIDE = 3

// Function that refreshes the page every 5 seconds.
function refreshPageEvent(event) {
    const secondsLeftSpan = document.getElementById("logic-time-left")
    const loadingBarLists = document.getElementsByClassName("list-loading-bar")
    const timerParagraph = document.getElementById("timer-paragraph")

    let secondsLeft = TIME_UNTIL_REFRESH_SECONDS

    secondsLeftSpan.innerText = secondsLeft
    
    const intervalHandle = setInterval(function() {
        secondsLeftSpan.innerText = --secondsLeft

        //Removes one child from each loadingbar
        for(let i = 0; i < loadingBarLists.length; i++) {
            if(loadingBarLists[i].children.length > 0) {
                loadingBarLists[i].removeChild(loadingBarLists[i].lastElementChild)
            }
        }
        
        if(secondsLeft <= 0) {
            location = '/'
            clearInterval(intervalHandle)
            timerParagraph.innerText = "Refreshing..."
        }
    }, 1000)
}

window.addEventListener("load", refreshPageEvent)