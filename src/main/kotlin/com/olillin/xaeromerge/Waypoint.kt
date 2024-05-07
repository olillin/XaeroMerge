package com.olillin.xaeromerge.com.olillin.xaeromerge

data class Waypoint(
    val name: String,
    val initials: String,
    val x: Int,
    val y: Int,
    val z: Int,
    val color: Int,
    val disabled: Boolean,
    val type: Int,
    val set: String,
    val rotateOnTp: Boolean,
    val tpYaw: Int,
    val visibilityType: Int,
    val destination: Boolean,
) {
    val location: Location = Location(x, y, z)
    fun asLine(): String = "waypoint:$name:$initials:$x:$y:$z:$color:$disabled:$type:$set:$rotateOnTp:$tpYaw:$visibilityType:$destination"

    companion object {
        fun fromLine(line: String): Waypoint {
            val split: List<String> = line.split(':')
            return Waypoint(
                split[1], // name
                split[2], // initials
                split[3].toInt(), // x
                split[4].toInt(), // y
                split[5].toInt(), // z
                split[6].toInt(), // color
                split[7].toBoolean(), // disabled
                split[8].toInt(), // type
                split[9], // set
                split[10].toBoolean(), // rotateOnTp
                split[11].toInt(), // tpYaw
                split[12].toInt(), // visibility_type
                split[13].toBoolean(), // destination
            )
        }
    }
}