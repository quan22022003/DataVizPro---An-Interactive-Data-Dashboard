package InputDialog

import scalafx.Includes.*
import scalafx.scene.control.*
import scalafx.scene.layout.VBox
import scalafx.util.Duration

/** A generic trait for creating input dialogs in ScalaFX applications. It
  * provides common functionality for different types of input dialogs.
  *
  * T: The type of value that the dialog returns upon submission.
  */
trait InputDialog[T](action: String) extends Dialog[T]:
  // Protected variable for the dialog's input box with spacing between elements
  protected val inputBox = new VBox(spacing = 10)
  // Protected variable for the dialog's save button type
  protected val saveButtonType =
    new ButtonType(action, ButtonBar.ButtonData.OKDone)
  // Create and configure the save button
  protected val saveButton = createButtonAndConfigure()
  // Set the content of the dialog pane to be the input box
  this.getDialogPane.content = inputBox

  /** Creates and configures the dialog's buttons, adding them to the dialog
    * pane. It disables the save button initially to prevent submission without
    * input.
    *
    * @return
    *   The save button for further configuration or use.
    */
  protected def createButtonAndConfigure() =
    // Add Cancel and Save button types to the dialog pane
    this.getDialogPane.buttonTypes ++= Seq(ButtonType.Cancel, saveButtonType)
    // Lookup the save button and set it to be initially disabled
    val saveButton = this.getDialogPane
      .lookupButton(saveButtonType)
      .asInstanceOf[javafx.scene.control.Button]
    saveButton.disable = true
    saveButton
  end createButtonAndConfigure

  /** Configures a tooltip for a TextField.
    *
    * @param field
    *   The TextField to which the tooltip will be applied.
    * @param message
    *   The message that will be displayed in the tooltip.
    */
  protected def configureTooltip(field: TextField, message: String): Unit =
    // Set the tooltip with the provided message
    field.tooltip = new Tooltip(message) {
      showDelay = Duration(0)
    }
  end configureTooltip

  /** Adds text change listeners to one or more TextFields to control the save
    * button's disabled state. The save button is enabled only when all fields
    * are non-empty.
    *
    * @param saveButton
    *   The button to be enabled or disabled based on the input fields' states.
    * @param fields
    *   The TextFields to be monitored for changes.
    */
  protected def addChangeListeners(
    saveButton: Button,
    fields: TextField*
  ): Unit =
    // Add a listener to each text field
    fields.foreach { field =>
      field.text.onChange { (_, _, newValue) =>
        // Enable the save button only if all fields are non-empty
        saveButton.disable = !fields.forall(_.text.value.trim.nonEmpty)
      }
    }
  end addChangeListeners
