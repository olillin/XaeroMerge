package com.olillin.xaeromerge

import com.olillin.xaeromerge.WorldMapMerger
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path

class App: Application() {

    private val selectFrontGameDirectoryButton = Button()
    private val frontGameDirectoryTextField = TextField().apply {
        prefColumnCount = 60
        textProperty().addListener { _ ->
            updateFrontWorldSelector()
        }
    }

    private val frontWorldSelectorLabel = Label()
    private val frontWorldSelectorComboBox = ComboBox<String>()

    private val selectBackGameDirectoryButton = Button()
    private val backGameDirectoryTextField = TextField().apply {
        prefColumnCount = 60
        textProperty().addListener { _ ->
            updateBackWorldSelector()
        }
    }

    private val backWorldSelectorLabel = Label()
    private val backWorldSelectorComboBox = ComboBox<String>()

    private val mergeWaypointsCheckBox = CheckBox()
    private val mergeWorldMapCheckBox = CheckBox()

    private val mergeButton = Button()

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "XaeroMerge"

        selectFrontGameDirectoryButton.apply {
            text = "Front game directory"
            setOnMouseClicked {
                val chooser = DirectoryChooser()
                val chosenDirectory: File = chooser.showDialog(primaryStage)
                frontGameDirectoryTextField.text = chosenDirectory.absolutePath

                updateFrontWorldSelector()
            }
        }
        selectBackGameDirectoryButton.apply {
            text = "Back game directory"
            setOnMouseClicked {
                val chooser = DirectoryChooser()
                val chosenDirectory: File = chooser.showDialog(primaryStage)
                backGameDirectoryTextField.text = chosenDirectory.absolutePath

                updateBackWorldSelector()
            }
        }

        val frontSelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                selectFrontGameDirectoryButton,
                frontGameDirectoryTextField,
            )
        }

        val backSelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                selectBackGameDirectoryButton,
                backGameDirectoryTextField,
            )
        }


        frontWorldSelectorLabel.apply {
            text = "Front world"
        }

        val frontWorldSelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                frontWorldSelectorLabel,
                frontWorldSelectorComboBox,
            )
        }

        backWorldSelectorLabel.apply {
            text = "Back world"
        }

        val backWorldSelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                backWorldSelectorLabel,
                backWorldSelectorComboBox,
            )
        }

        mergeWaypointsCheckBox.apply {
            text = "Merge waypoints"
            isSelected = true
        }

        mergeWorldMapCheckBox.apply {
            text = "Merge world map"
            isSelected = true
        }

        val optionsChecks = HBox().apply {
            spacing = 4.0
            children.addAll(
                mergeWaypointsCheckBox,
                mergeWorldMapCheckBox,
            )
        }

        mergeButton.apply {
            text = "Merge"
            setOnMouseClicked {
                val frontDirectory = Path.of(frontGameDirectoryTextField.text)
                val frontWorld = frontWorldSelectorComboBox.value
                val backDirectory = Path.of(backGameDirectoryTextField.text)
                val backWorld = backWorldSelectorComboBox.value

                // Merge waypoints
                if (mergeWaypointsCheckBox.isSelected) {
                    // TODO: Add minDistance slider/input
                    WaypointMerger.merge(frontWorld, frontDirectory, backWorld, backDirectory)
                }
                // Merge world map
                if (mergeWorldMapCheckBox.isSelected) {
                    WorldMapMerger.merge(frontWorld, frontDirectory, backWorld, backDirectory)
                }
            }
        }

        val layout = VBox().apply {
            spacing = 8.0
            padding = Insets(8.0)
            children.addAll(
                frontSelector,
                frontWorldSelector,
                backSelector,
                backWorldSelector,
                optionsChecks,
                mergeButton,
            )
        }

        val scene = Scene(layout, 1000.0, 300.0)
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun updateFrontWorldSelector() {
        try {
            val directory = GameDirectory(Path.of(frontGameDirectoryTextField.text))
            val worlds: Set<String> = directory.getWaypointWorlds() intersect directory.getWorldMapWorlds()

            frontWorldSelectorComboBox.items.setAll(worlds)
        } catch (e: InvalidPathException) {
            return
        }
    }

    fun updateBackWorldSelector() {
        try {
            val directory = GameDirectory(Path.of(backGameDirectoryTextField.text))
            val worlds: Set<String> = directory.getWaypointWorlds() intersect directory.getWorldMapWorlds()

            backWorldSelectorComboBox.items.setAll(worlds)
        } catch (e: InvalidPathException) {
            return
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
