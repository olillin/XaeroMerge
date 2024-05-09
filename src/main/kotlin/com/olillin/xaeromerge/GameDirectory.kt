package com.olillin.xaeromerge

import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*

/** Read and write Xaero's waypoint and world map data from a game [directory]. */
class GameDirectory(private val directory: Path) {
    fun getWaypointWorlds(): List<String> {
        val waypointsDirectory = getWaypointsDirectory()
        return waypointsDirectory.listDirectoryEntries()
            .filter { it.isDirectory() }
            .map { it.name }
    }

    /**
     * Get all waypoints from [world] in a game [directory].
     */
    fun getWaypoints(world: String): Map<String, List<Waypoint>> {
        val path = getWaypointsDirectory(world)
        val dimensionDirectories = path.listDirectoryEntries().filter {
            it.isDirectory() && it.name.startsWith("dim%")
        }

        val waypoints: MutableMap<String, List<Waypoint>> = mutableMapOf()
        dimensionDirectories.forEach { dimensionDirectory ->
            val dimWaypoints: MutableList<Waypoint> = mutableListOf()
            val dim: String = dimensionDirectory.name
                .split("%")[1]

            val waypointsFilePath: Path = dimensionDirectory.listDirectoryEntries()[0]
            val waypointsFile = File(waypointsFilePath.absolutePathString())
            val scanner = Scanner(waypointsFile)

            try {
                while (scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    if (!line.startsWith("waypoint:")) continue

                    dimWaypoints.add(Waypoint.fromLine(line))
                }
            } finally {
                scanner.close()
            }

            waypoints[dim] = dimWaypoints
        }
        return waypoints
    }

    /**
     * Get the directory that contains waypoints of a [world].
     * @throws FileNotFoundException if the directory does not exist
     */
    fun getWaypointsDirectory(world: String): Path {
        val waypointsDirectory = getWaypointsDirectory()
        val worldPath = waypointsDirectory.resolve(world)
        if (!worldPath.isDirectory()) {
            throw FileNotFoundException("$directory does not contain the world $world in $WAYPOINTS_DIRECTORY")
        }
        return worldPath
    }

    fun setWaypoints(world: String, waypoints: Map<String, List<Waypoint>>) {
        for ((dim, value) in waypoints.entries) {
            setWaypoints(world, dim, value)
        }
    }

    fun setWaypoints(world: String, dim: String, waypoints: List<Waypoint>) {
        val worldDirectory = getWaypointsDirectory(world)
        val dimensionDirectory = worldDirectory.resolve("dim%$dim")
        if (!dimensionDirectory.isDirectory()) {
            dimensionDirectory.createDirectory()
        }

        val waypointsFilePath: Path = if (dimensionDirectory.listDirectoryEntries().isEmpty()) {
            dimensionDirectory.resolve("waypoints.txt")
        } else {
            dimensionDirectory.listDirectoryEntries()[0]
        }
        val fileWriter = FileWriter(waypointsFilePath.absolutePathString())
        try {
            fileWriter.write(WAYPOINTS_HEADER + "\n" + waypoints.joinToString("\n") { it.asLine() })
        } finally {
            fileWriter.close()
        }
    }

    /**
     * Get the waypoints directory.
     */
    fun getWaypointsDirectory(): Path {
        val waypointsPath = directory.resolve(WAYPOINTS_DIRECTORY)
        if (!waypointsPath.isDirectory()) {
            throw FileNotFoundException("$directory does not contain $WAYPOINTS_DIRECTORY")
        }
        return waypointsPath
    }

    fun getWorldMapWorlds(): List<String> {
        val worldMapDirectory = getWorldMapDirectory()
        return worldMapDirectory.listDirectoryEntries()
            .filter { it.isDirectory() }
            .map { it.name }
    }

    /**
     * Get the directory that contains worldMap of a [world].
     * @throws FileNotFoundException if the directory does not exist
     */
    fun getWorldMapDirectory(world: String): Path {
        val worldMapDirectory = getWorldMapDirectory()
        val worldPath = worldMapDirectory.resolve(world)
        if (!worldPath.isDirectory()) {
            throw FileNotFoundException("$directory does not contain the world $world in $WORLD_MAP_DIRECTORY")
        }
        return worldPath
    }

    /**
     * Get the worldMap directory.
     */
    fun getWorldMapDirectory(): Path {
        val worldMapPath = directory.resolve(WORLD_MAP_DIRECTORY)
        if (!worldMapPath.isDirectory()) {
            throw FileNotFoundException("$directory does not contain $WORLD_MAP_DIRECTORY")
        }
        return worldMapPath
    }

    companion object {
        const val WAYPOINTS_DIRECTORY: String = "XaeroWaypoints"
        const val WORLD_MAP_DIRECTORY: String = "XaeroWorldMap"

        const val WAYPOINTS_HEADER: String = "#\n" +
                "#waypoint:name:initials:x:y:z:color:disabled:type:set:rotate_on_tp:tp_yaw:visibility_type:destination\n" +
                "#"
    }
}