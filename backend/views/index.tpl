<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="public/main.css" type="text/css">
    <script src="public/scripts/mowerMap.js"></script>
    <script src="public/scripts/refreshUserPage.js"></script>
    <title>Mappy McMapFace 🗺</title>
</head>
<body>
    <header>
        <h1>Conan Mower - Traveled path</h1>
    </header>
    <main>
        <div id="map">
            <svg viewBox="{{mapCenterPoint[0]}} {{mapCenterPoint[1]}} {{mapSize[0]}} {{mapSize[1]}}" id="map-lines"></svg>
        </div>
        <p>Refreshing map in <span id="logic-time-left"></span> sec.</p>
    </main>
</body>

<script>
    const allPoints = []
    % for index, point in enumerate(points):
    allPoints.push([{{point[0]}}, {{point[1]}}, "{{point[2]}}"])
    % end
    placeAllPoints(allPoints)
</script>

</html>