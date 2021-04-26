package com.example.conanmoverandroidapp

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties

data class TraveledPath(
    //var id: String = "",
    var CurrentAngle: Int = 0,
    var EndTime: String = "",
    var StoppedByObstacle: Int = 0,
    var TraveledDistance: Int = 0
)