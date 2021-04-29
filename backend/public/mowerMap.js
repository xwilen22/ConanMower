window.addEventListener("load", (event) => {

})

function placePoint(pointId, xPosition, yPosition) {
    const currentMap = document.getElementById("map")
    
    let newPoint = document.createElement("span")
    newPoint.id = pointId
    newPoint.classList.add("map-node")
    newPoint.style.top = `${yPosition}px`
    newPoint.style.left = `${xPosition}px`

    currentMap.appendChild(newPoint)
}

function lineDistance(x, y, x0, y0){
    return Math.sqrt((x -= x0) * x + (y -= y0) * y);
}

function placeEdgeBetweenPoints(pointOneId, pointTwoId) {
    const currentMap = document.getElementById("map")
    
    const pointOne = document.getElementById(pointOneId)
    const pointTwo = document.getElementById(pointTwoId)

    const edgeAngle = Math.atan2(parseInt(pointTwo.style.top) - parseInt(pointOne.style.top), parseInt(pointTwo.style.left) - parseInt(pointOne.style.left)) * 180 / Math.PI
    const edgeLength = lineDistance(parseInt(pointOne.style.left), parseInt(pointOne.style.top), parseInt(pointTwo.style.left), parseInt(pointTwo.style.top))

    let lineElement = document.createElement("div")
    lineElement.classList.add("map-edge")
    
    lineElement.style.transform = `rotate(${edgeAngle}deg)`
    lineElement.style.width = `${edgeLength}px`
    
    lineElement.style.top = `${parseInt(pointOne.style.top) + parseInt(pointOne.offsetHeight) / 2}px`
    if(pointTwo.left < pointOne.left) {
        lineElement.style.left = `${parseInt(pointTwo.style.left) + parseInt(pointTwo.offsetWidth) / 2}px`
    } else {
        lineElement.style.left = `${parseInt(pointOne.style.left) + parseInt(pointOne.offsetWidth) / 2}px`
    }

    console.log("LineElement ", lineElement.style)
    currentMap.appendChild(lineElement)
}
/* 
var pointA = $(a).offset();
  var pointB = $(b).offset();
  var pointAcenterX = $(a).width() / 2;
  var pointAcenterY = $(a).height() / 2;
  var pointBcenterX = $(b).width() / 2;
  var pointBcenterY = $(b).height() / 2;
  var angle = Math.atan2(pointB.top - pointA.top, pointB.left - pointA.left) * 180 / Math.PI;
  var distance = lineDistance(pointA.left, pointA.top, pointB.left, pointB.top);

  // INFO
  $('.info .point-a').text('Point-A: Left: ' + pointA.left + ' Top: ' + pointA.top);
  $('.info .point-b').text('Point-B: Left: ' + pointB.left + ' Top: ' + pointB.top);
  $('.info .angle').text('Angle: ' + angle);
  $('.info .distance').text('Distance: ' + distance);

  // Set Angle
  $(line).css('transform', 'rotate(' + angle + 'deg)');

  // Set Width
  $(line).css('width', distance + 'px');
                  
  // Set Position
  $(line).css('position', 'absolute');
  if(pointB.left < pointA.left) {
    $(line).offset({top: pointA.top + pointAcenterY, left: pointB.left + pointBcenterX});
  } else {
    $(line).offset({top: pointA.top + pointAcenterY, left: pointA.left + pointAcenterX});
  }
*/