window.addEventListener("load", (event) => {

})

function placeAllPoints(pointList) {
    const lineElement = document.getElementById("map-lines")
    let svgPolyline = document.createElementNS("http://www.w3.org/2000/svg", "polyline")
    console.log(pointList)

    // POLYLINE ATTRIBUTES
    // Construct the list of points to a string for points attribute
    let polyLinePathString = ""
    for(pointPair of pointList) {
        polyLinePathString += `${pointPair[0]},${pointPair[1]} `
    }
    svgPolyline.setAttribute("stroke", "black")
    svgPolyline.setAttribute("fill", "none")
    svgPolyline.setAttribute("points", polyLinePathString)

    lineElement.appendChild(svgPolyline)
}