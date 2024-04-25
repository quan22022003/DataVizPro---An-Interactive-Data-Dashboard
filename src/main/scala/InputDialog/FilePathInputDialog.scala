package InputDialog

import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.*
import scalafx.scene.layout.{HBox, VBox}

/** Dialog for web input, allowing the user to enter a URL string. Extends the
  * InputDialog trait with specific configurations for handling URL input.
  */
class FilePathInputDialog extends InputDialog[String]("Fetch"):

  // Create a text field for URL input
  private val inputField = new TextField()

  // Add a label and the text field to the input box
  private val pathInput =
    new HBox(new Label("Link to the file: "), inputField) {
      spacing = 10
      alignment = Pos.Center
    }

  inputBox.children = Seq(pathInput)

  // Add a listener to the URL input field to control the save button's state
  addChangeListeners(saveButton, inputField)

  // Set the result converter to output the URL text
  this.resultConverter = dialogButton =>
    if dialogButton == saveButtonType then inputField.text.value else null

  def getInputBox: VBox = inputBox
