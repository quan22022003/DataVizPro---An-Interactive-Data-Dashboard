package InputDialog

import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.*
import scalafx.scene.layout.HBox

/** Dialog for local input, allowing the entry of a header and a numeric value.
  * Extends the InputDialog trait with specific configurations for handling user
  * inputs.
  */
class DataPointInputDialog extends InputDialog[(String, Float)]("Add"):
  // Text fields for the header (string) and value (float) input by the user.
  private val headerField = new TextField()
  private val valueField = new TextField()

  // Configure tooltips for the header and value fields
  configureTooltip(headerField, "Enter the header of the datapoint")
  configureTooltip(valueField, "Enter a number (default: 0)")

  private val headerBox = new HBox(new Label("Header"), headerField) {
    spacing = 10
    alignment = Pos.Center
  }
  private val valueBox = new HBox(new Label("Value"), valueField) {
    spacing = 10
    alignment = Pos.Center
  }
  // Add labels and text fields to the input box
  inputBox.children = Seq(headerBox, valueBox)

  // Add listeners to the text fields to control the save button's state
  addChangeListeners(saveButton, headerField, valueField)

  // Set the result converter to output a tuple of the header and value
  resultConverter = dialogButton =>
    if dialogButton == saveButtonType && valueField.text.value.trim.nonEmpty
    then
      // Get the text from the header field
      val header = headerField.text.value
      // Try to convert the text from the value field to a Float, defaulting to 0f if not possible
      val value = valueField.text.value.toFloatOption.getOrElse(0f)
      // Return the header and value as a tuple
      (header, value)
    else
      // Return null if the dialog was canceled
      null
