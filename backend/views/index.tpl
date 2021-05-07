<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="public/main.css" type="text/css">
    <script src="public/mowerMap.js"></script>
    <script src="public/refreshUserPage.js"></script>
    <title>Mappy McMapFace 🗺</title>
</head>
<body>
    <header>
        <h1>ConanMower - Current traveled path</h1>
    </header>
    <main>
        <div id="map">
            <svg viewBox="0 0 100 100" id="map-lines">
                
            </svg>
        </div>
    </main>
</body>

<script>
    const allPoints = []
    % for index, point in enumerate(points):
    allPoints.push([{{point[0]}}, {{point[1]}}, "{{point[2]}}"])
    % end
    placeAllPoints(allPoints)

    refreshPage()

</script>

</html>