package com.olillin.xaeromerge

import kotlin.math.pow
import kotlin.math.sqrt

data class Location(val x: Int, val y: Int, val z: Int) {
    fun distance(other: Location): Double = sqrt(sqrDistance(other))
    fun sqrDistance(other: Location): Double =
        (x - other.x).toDouble().pow(2.0) +
        (y - other.y).toDouble().pow(2.0) +
        (z - other.z).toDouble().pow(2.0)
}