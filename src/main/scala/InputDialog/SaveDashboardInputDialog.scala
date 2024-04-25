package InputDialog

import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Dialog, Label, TextField}
import scalafx.scene.layout.HBox
import scalafx.stage.FileChooser

import scala.GUI.stage

/** A dialog for saving a dashboard with a custom file extension, providing a
  * user interface for selecting a file path and name where the dashboard will
  * be saved.
  *
  * @constructor
  *   Create a new save dashboard input dialog.
  */
class SaveDashboardInputDialog() extends InputDialog[String]("Save"):
  private val fileLabel = new Label("Select a directory:")

  // Text field for displaying the chosen file path. The field is editable
  // and has a predefined width.
  private val filePathField = new TextField {
    editable = true
    prefWidth = 300
  }

  // Button to open the file chooser dialog. It allows the user to browse
  // their file system and select a location to save the dashboard.
  private val browseButton = new Button("Browse...") {
    onAction = _ =>
      val fileChooser = new FileChooser {
        title = "Save dashboard to..."
        extensionFilters.add(
          new FileChooser.ExtensionFilter("Custom File", "*.quan")
        )
      }
      val file = fileChooser.showSaveDialog(stage)
      if file != null then filePathField.text = file.getAbsolutePath
  }

  private val hBox = new HBox(fileLabel, filePathField, browseButton) {
    spacing = 10
    alignment = Pos.Center
  }

  inputBox.children.addAll(hBox)

  // Add a listener to the URL input field to control the save button's state
  addChangeListeners(this.saveButton, filePathField)

  // Set the result converter to output the directory text if the save button
  // was pressed, otherwise return null.
  this.resultConverter = dialogButton =>
    if dialogButton == saveButtonType then filePathField.text.value else null
