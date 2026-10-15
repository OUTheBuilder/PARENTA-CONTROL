package com.parentguard.data

data class GeoZone(
    val id: String = "",
    val name: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val radiusMeters: Float = 100f
)
