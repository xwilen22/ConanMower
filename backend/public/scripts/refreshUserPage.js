const REFRESH_INTERVAL_MILLISECONDS = 5000

// Function that refreshes the page every 5 seconds.
function refreshPage(event) {
    setTimeout(function() {
        location = '/'
      }, REFRESH_INTERVAL_MILLISECONDS)
}

window.addEventListener("load", refreshPage)