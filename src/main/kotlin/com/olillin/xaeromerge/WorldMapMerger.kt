package com.olillin.xaeromerge.com.olillin.xaeromerge

import com.github.entropy5.XaeroRegionMerger
import java.nio.file.Path

object WorldMapMerger {
    /**
     * Adds the map data from [backWorld] from [backDirectory] behind the
     * map of [frontWorld] from [frontDirectory].
     *
     * Directory paths should point to a Minecraft game directory.
     */
    fun merge(frontWorld: String, frontDirectory: Path, backWorld: String, backDirectory: Path) {
        XaeroRegionMerger.main(arrayOf(""))
    }
}