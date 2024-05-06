package com.olillin.xaeromerge

import java.nio.file.Path
import kotlin.math.pow

object WaypointMerger {
    /**
     * Appends waypoints in the [frontWorld] from [backDirectory] into the
     * [backWorld] of [frontDirectory]. Waypoints in [backDirectory] that
     * are closer than [minDistance] blocks to any waypoint in [frontDirectory]
     * are ignored. If [minDistance] is negative no filtering is done.
     *
     * Directory paths should point to a Minecraft game directory.
     */
    fun merge(
        frontWorld: String,
        frontDirectory: Path,
        backWorld: String,
        backDirectory: Path,
        minDistance: Double = 0.1
    ) {
        val frontGameDirectory = GameDirectory(frontDirectory)
        val frontWaypoints = frontGameDirectory.getWaypoints(frontWorld)
        val backGameDirectory = GameDirectory(backDirectory)
        val backWaypoints = backGameDirectory.getWaypoints(backWorld)

        println(frontWaypoints)
        println(backWaypoints)

        val waypoints: MutableMap<String, List<Waypoint>> = frontWaypoints.toMutableMap()
        for ((dim, dimWaypoints) in backWaypoints) {
            if (waypoints.containsKey(dim)) {
                val existingWaypoints = waypoints[dim]!!
                val newWaypoints: MutableList<Waypoint> = mutableListOf()
                if (minDistance < 0.0) {
                    newWaypoints.addAll(dimWaypoints)
                } else {
                    // Filter according to maxOverlayDistance
                    check@for (waypoint in dimWaypoints) {
                        val waypointLocation = waypoint.location
                        val sqrMinDistance = minDistance.pow(2.0)
                        for (other in existingWaypoints) {
                            val sqrDistance = waypointLocation.sqrDistance(other.location)
                            if (sqrDistance < sqrMinDistance) continue@check
                        }
                        newWaypoints.add(waypoint)
                    }
                }
                waypoints[dim] = existingWaypoints + newWaypoints
            } else {
                waypoints[dim] = dimWaypoints
            }
        }

        println(waypoints)
        frontGameDirectory.setWaypoints(frontWorld, waypoints)
    }
}