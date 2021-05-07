const TIME_UNTIL_REFRESH_SECONDS = 5

const LOADING_BLOCKS_PER_SIDE = 3

// Function that refreshes the page every 5 seconds.
function refreshPageEvent(event) {
    const secondsLeftSpan = document.getElementById("logic-time-left")
    const loadingBarLists = document.getElementsByClassName("list-loading-bar")
    
    let secondsLeft = TIME_UNTIL_REFRESH_SECONDS
    const blockSeconds = secondsLeft / LOADING_BLOCKS_PER_SIDE

    secondsLeftSpan.innerText = secondsLeft
    
    setInterval(function() {
        secondsLeft--
        secondsLeftSpan.innerText = secondsLeft

        //Removes one child from each loadingbar
        if(loadingBarLists[0].children.length > 0 && loadingBarLists[1].children.length > 0) {
            loadingBarLists[0].removeChild(loadingBarLists[0].lastElementChild)
            loadingBarLists[1].removeChild(loadingBarLists[1].lastElementChild)
        }

        if(secondsLeft <= 0) {
            location = '/'
        }
    }, 1000)
}

window.addEventListener("load", refreshPageEvent)