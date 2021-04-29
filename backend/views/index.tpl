<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="public/main.css" type="text/css">
    <script src="public/mowerMap.js"></script>
    <title>🗺</title>
</head>
<body>
    <header>
        <h1>ConanMower - Current traveled path</h1>
    </header>
    <main>
        <div id="map">
        </div>
    </main>
</body>

<script>
    % for index, point in enumerate(points):
    placePoint("point{{index}}", {{point[0]}}, {{point[1]}})
    % end
    % for edge in edges:
    placeEdgeBetweenPoints("point{{edge[0]}}", "point{{edge[1]}}")
    % end
</script>

</html>