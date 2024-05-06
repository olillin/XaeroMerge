package com.olillin.xaeromerge

import java.nio.file.Path
import kotlin.math.pow

object WaypointMerger {
    /**
     * Appends waypoints in the [world] from [secondaryDirectory] into the [world] of
     * [primaryDirectory]. Waypoints in [secondaryDirectory] that are closer than
     * [minDistance] blocks to any waypoint in [primaryDirectory] are ignored.
     * If [minDistance] is negative no filtering is done.
     */
    fun merge(world: String, primaryDirectory: Path, secondaryDirectory: Path, minDistance: Double = 0.1) =
        merge(world, primaryDirectory, world, secondaryDirectory, minDistance)

    /**
     * Appends waypoints in the [primaryWorld] from [secondaryDirectory] into the
     * [secondaryWorld] of [primaryDirectory]. Waypoints in [secondaryDirectory] that
     * are closer than [minDistance] blocks to any waypoint in [primaryDirectory]
     * are ignored. If [minDistance] is negative no filtering is done.
     *
     * Directory paths should point to a Minecraft game directory.
     */
    fun merge(
        primaryWorld: String,
        primaryDirectory: Path,
        secondaryWorld: String,
        secondaryDirectory: Path,
        minDistance: Double = 0.1
    ) {
        val primaryGameDirectory = GameDirectory(primaryDirectory)
        val primaryWaypoints = primaryGameDirectory.getWaypoints(primaryWorld)
        val secondaryGameDirectory = GameDirectory(secondaryDirectory)
        val secondaryWaypoints = secondaryGameDirectory.getWaypoints(secondaryWorld)

        println(primaryWaypoints)
        println(secondaryWaypoints)

        val waypoints: MutableMap<String, List<Waypoint>> = primaryWaypoints.toMutableMap()
        for ((dim, dimWaypoints) in secondaryWaypoints) {
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
        primaryGameDirectory.setWaypoints(primaryWorld, waypoints)
    }
}