const TIME_UNTIL_REFRESH_SECONDS = 5

// Function that refreshes the page every 5 seconds.
function refreshPageEvent(event) {
    const secondsLeftSpan = document.getElementById("logic-time-left")
    const loadingBarLists = document.getElementsByClassName("list-loading-bar")
    
    let secondsLeft = TIME_UNTIL_REFRESH_SECONDS

    secondsLeftSpan.innerText = secondsLeft
    
    setInterval(function() {
        secondsLeft--
        secondsLeftSpan.innerText = secondsLeft

        const lastLoadingBarChildren = [
            loadingBarLists[0].childNodes[loadingBarLists[0].childNodes.length - 1],
            loadingBarLists[1].childNodes[loadingBarLists[1].childNodes.length - 1]
        ]

        lastLoadingBarChildren.forEach(loadingBarNode => {
            if(loadingBarNode != undefined) {
                loadingBarNode.remove()
            }
        });

        if(secondsLeft <= 0) {
            location = '/'
        }
    }, 1000)
}

window.addEventListener("load", refreshPageEvent)