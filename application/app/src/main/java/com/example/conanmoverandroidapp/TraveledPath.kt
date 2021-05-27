package com.example.conanmoverandroidapp

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties

data class TraveledPath(
    var EndTime: String = "",
    var CurrentAngle: Int = 0,
    var StoppedByObstacle: Int = 0,
    var TraveledDistance: Int = 0
)

data class TraveledPathSession(
    var sessionName: String,
    var traveledPaths: MutableList<TraveledPath>
)

data class Coordinates(
    var xCoordinate: Float,
    var yCoordinate: Float
)