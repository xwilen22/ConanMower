const SVG_NAMESPACE = "http://www.w3.org/2000/svg"

// Place the points on the map.
function placeAllPoints(pointList) {
    const lineElement = document.getElementById("map-lines")
    
    const allSvgPoints = []

    let svgPolyline = document.createElementNS(SVG_NAMESPACE, "polyline")
    // POLYLINE ATTRIBUTES
    // Construct the list of points to a string for points attribute
    let polyLinePathString = String()
    pointList.forEach((pointParameters, index) => {
        const xPosition = pointParameters[0]
        const yPosition = pointParameters[1]
        const isObstaclePoint = String(pointParameters[2]).toLowerCase() == "true"

        polyLinePathString += `${xPosition},${yPosition} `

        switch(index) {
            //Start graphic
            case 0:
                allSvgPoints.push(getStartGraphic(xPosition, yPosition))
                break
            //End graphic
            case pointList.length - 1:
                allSvgPoints.push(getMowerGraphic(xPosition, yPosition))
                break
            //Default graphic between end and start
            default:
                allSvgPoints.push(getPointGraphic(xPosition, yPosition, isObstaclePoint))
                break
        }
    });

    svgPolyline.setAttribute("stroke", "black")
    svgPolyline.setAttribute("fill", "none")
    svgPolyline.setAttribute("points", polyLinePathString)

    lineElement.appendChild(svgPolyline)

    for(let pointGraphic of allSvgPoints) {
        lineElement.appendChild(pointGraphic)
    }
}

// Get a point graphic from the SVG namespace. 
function getPointGraphic(xPosition, yPosition, isObstaclePoint) {
    let point = document.createElementNS(SVG_NAMESPACE, "circle")
    
    point.setAttribute("cx", xPosition)
    point.setAttribute("cy", yPosition)
    point.setAttribute("r", 2)
    point.setAttribute("stroke", "black")
    point.setAttribute("fill", isObstaclePoint ? "red" : "blue")

    return point
}

// Constructs a triangle to represent the mower
function getMowerGraphic(xPosition, yPosition) {
    let mowerPoint = document.createElementNS(SVG_NAMESPACE, "polygon")

    const triangleSize = 3

    let trianglePolygonPoints = [
        [String(xPosition + triangleSize), String(yPosition + triangleSize)], 
        [String(xPosition - triangleSize), String(yPosition - triangleSize)], 
        [String(xPosition - (triangleSize + 2)), String(yPosition + (triangleSize + 2))]
    ]
    let polygonPointString = String()
    for(let polyPoint of trianglePolygonPoints) {
        polygonPointString += `${polyPoint[0]},${polyPoint[1]} `
    }

    mowerPoint.setAttribute("points", polygonPointString)
    mowerPoint.setAttribute("r", 1)
    mowerPoint.setAttribute("stroke", "black")
    mowerPoint.setAttribute("fill", "white")

    return mowerPoint
}

// Get the start coordinate, which is the dot in green.
function getStartGraphic(xPosition, yPosition) {
    let point = document.createElementNS(SVG_NAMESPACE, "circle")
    
    point.setAttribute("cx", xPosition)
    point.setAttribute("cy", yPosition)
    point.setAttribute("r", 2)
    point.setAttribute("stroke", "black")
    point.setAttribute("fill", "green")

    return point
}