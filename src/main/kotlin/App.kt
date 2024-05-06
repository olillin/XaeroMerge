package com.olillin.xaeromerge

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

    private val selectPrimaryGameDirectoryButton = Button()
    private val primaryGameDirectoryTextField = TextField().apply {
        prefColumnCount = 60
        textProperty().addListener { _ ->
            updateWorldSelector()
        }
    }
    private val selectSecondaryGameDirectoryButton = Button()
    private val secondaryGameDirectoryTextField = TextField().apply {
        prefColumnCount = 60
        textProperty().addListener { _ ->
            updateWorldSelector()
        }
    }

    private val worldSelectorLabel = Label()
    private val worldSelectorComboBox = ComboBox<String>()

    private val mergeWaypointsCheckBox = CheckBox()
    private val mergeWorldMapCheckBox = CheckBox()

    private val mergeButton = Button()

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "XaeroMerge"

        selectPrimaryGameDirectoryButton.apply {
            text = "Game directory to merge into"
            setOnMouseClicked {
                val chooser = DirectoryChooser()
                val chosenDirectory: File = chooser.showDialog(primaryStage)
                primaryGameDirectoryTextField.text = chosenDirectory.absolutePath

                updateWorldSelector()
            }
        }
        selectSecondaryGameDirectoryButton.apply {
            text = "Game directory to merge from"
            setOnMouseClicked {
                val chooser = DirectoryChooser()
                val chosenDirectory: File = chooser.showDialog(primaryStage)
                secondaryGameDirectoryTextField.text = chosenDirectory.absolutePath

                updateWorldSelector()
            }
        }

        val primarySelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                selectPrimaryGameDirectoryButton,
                primaryGameDirectoryTextField,
            )
        }

        val secondarySelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                selectSecondaryGameDirectoryButton,
                secondaryGameDirectoryTextField,
            )
        }

        worldSelectorLabel.apply {
            text = "World"
        }

        val worldSelector = HBox().apply {
            spacing = 4.0
            children.addAll(
                worldSelectorLabel,
                worldSelectorComboBox,
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
                val world = worldSelectorComboBox.value
                val primaryDirectory = Path.of(primaryGameDirectoryTextField.text)
                val secondaryDirectory = Path.of(secondaryGameDirectoryTextField.text)

                // Merge waypoints
                if (mergeWaypointsCheckBox.isSelected) {
                    // TODO: Add minDistance slider/input
                    WaypointMerger.merge(world, primaryDirectory, secondaryDirectory)
                }
                // Merge world map
                if (mergeWorldMapCheckBox.isSelected) {
                    WorldMapMerger.merge(world, primaryDirectory, secondaryDirectory)
                }
            }
        }

        val layout = VBox().apply {
            spacing = 8.0
            padding = Insets(8.0)
            children.addAll(
                primarySelector,
                secondarySelector,
                worldSelector,
                optionsChecks,
                mergeButton,
            )
        }

        val scene = Scene(layout, 1000.0, 300.0)
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun updateWorldSelector() {
        try {
            val primaryDirectory = GameDirectory(Path.of(primaryGameDirectoryTextField.text))
            val primaryWorlds: Set<String> = primaryDirectory.getWaypointWorlds() intersect primaryDirectory.getWorldMapWorlds()
            val secondaryDirectory = GameDirectory(Path.of(secondaryGameDirectoryTextField.text))
            val secondaryWorlds: Set<String> = secondaryDirectory.getWaypointWorlds() intersect secondaryDirectory.getWorldMapWorlds()

            val worlds = primaryWorlds intersect secondaryWorlds
            worldSelectorComboBox.items.setAll(worlds)
        } catch (e: InvalidPathException) {
            return
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
