const TIME_UNTIL_REFRESH_SECONDS = 6

const LOADING_BLOCKS_PER_SIDE = 3

// Function that refreshes the page every 6 seconds.
function refreshPageEvent(event) {
    const secondsLeftSpan = document.getElementById("logic-time-left")
    const loadingBarLists = document.getElementsByClassName("list-loading-bar")
    const timerParagraph = document.getElementById("timer-paragraph")

    let secondsLeft = TIME_UNTIL_REFRESH_SECONDS

    secondsLeftSpan.innerText = secondsLeft
    
    const intervalHandle = setInterval(function() {
        secondsLeftSpan.innerText = --secondsLeft
        
        const percentageDone = secondsLeft / TIME_UNTIL_REFRESH_SECONDS
        const barAmountGoal = Math.trunc(percentageDone * LOADING_BLOCKS_PER_SIDE)

        while(loadingBarLists[0].children.length > barAmountGoal) {
            removePointFromLoadingBar(loadingBarLists)
        }
        
        if(secondsLeft <= 0) {
            // Removes the last one
            removePointFromLoadingBar(loadingBarLists)
            
            location = '/'
            clearInterval(intervalHandle)
            timerParagraph.innerText = "Refreshing..."
        }
    }, 1000)
}

// Removes one bar from the graphical loading indicator
function removePointFromLoadingBar(loadingBarLists) {
    //Removes one child from each loadingbar
    for(let i = 0; i < loadingBarLists.length; i++) {
        if(loadingBarLists[i].children.length > 0) {
            loadingBarLists[i].removeChild(loadingBarLists[i].lastElementChild)
        }
    }
}

window.addEventListener("load", refreshPageEvent)