package Tools

import Boxes.CardsBox
import scalafx.Includes.jfxMouseEvent2sfx
import scalafx.geometry.Insets
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, Pane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

/** A transparent pane serves as a dynamic overlay for CardsBox, enabling
  * interactive selection of contained cards. It introduces a selection
  * rectangle, visually represented by a Rectangle shape, which becomes visible
  * upon mouse click and drag, allowing users to select multiple cards based on
  * the rectangle's bounds.
  *
  * @param cardsBox
  *   The container holding the data used for chart generation.
  */
class SelectionOverlay(cardsBox: CardsBox) extends Pane:

  /** A Rectangle instance, initially invisible, used to represent the user's
    * selection area. Its visibility and dimensions are dynamically updated
    * based on user interaction.
    */
  private val selectionRect = new Rectangle {
    fill = Color.Transparent
    stroke = Color.Blue
    strokeWidth = 1
    visible = false // Initially invisible
  }

  /** A private Boolean flag indicating whether the selection overlay is
    * currently active and ready to register user input for card selection.
    */
  private var _isActive = false
  private var startX = 0.0
  private var startY = 0.0

  // Add the rectangle to the selectionOverlay instead of the DataBox
  children.add(selectionRect)

  /** Public getter method for the _isActive property, indicating the current
    * activation status of the selection overlay.
    * @return
    *   The current status of the selection overlay
    */
  def isActive: Boolean = _isActive

  /** Toggles the _isActive status, enabling or disabling the overlay's
    * responsiveness to user input.
    */
  def toggleStatus(): Unit =
    _isActive = !_isActive

  /** Captures the initial mouse coordinates at the start of a click-and-drag
    * operation, initializing the selection rectangle's position and making it
    * visible.
    * @param event
    *   The MouseEvent
    */
  private def handleMousePressed(event: MouseEvent): Unit =
    // Capture the initial mouse position
    startX = event.x
    startY = event.y

    // Initialize the rectangle's properties based on the initial mouse position
    selectionRect.x = startX
    selectionRect.y = startY
    selectionRect.width = 0
    selectionRect.height = 0
    selectionRect.visible = true

  /** Updates the selection rectangle's dimensions as the mouse is dragged,
    * extending or contracting the selection area in real-time.
    * @param event
    *   The MouseEvent
    */
  private def handleMouseDragged(event: MouseEvent): Unit =
    // Calculate the width and height based on current mouse position
    val width = math.abs(event.x - startX)
    val height = math.abs(event.y - startY)

    // Update the rectangle's dimensions
    selectionRect.width = width
    selectionRect.height = height
    // Support drawing the rectangle in any direction
    selectionRect.x = math.min(event.x, startX)
    selectionRect.y = math.min(event.y, startY)

    // Select cards that intersect with the selection rectangle
    selectIntersectingCards(selectionRect)

  /** Selects cards that intersect with the given selection rectangle.
    *
    * @param selectionRect
    *   The rectangle used for selection.
    */
  private def selectIntersectingCards(selectionRect: Rectangle): Unit =
    cardsBox.getAllCards.foreach { card =>
      // Use `.value` to directly access the Bounds object from the ReadOnlyObjectProperty
      if (
        card.boundsInParent.value.intersects(selectionRect.boundsInParent.value)
      ) then
        if !cardsBox.getChosenCards.contains(card) then
          cardsBox.selectCard(card)
      else if cardsBox.getChosenCards.contains(card) then
        cardsBox.unselectCard(card)
    }

  /** Hides the selection rectangle upon release of the mouse button, finalizing
    * the selection process.
    */
  private def handleMouseReleased(): Unit =
    // Make the rectangle invisible after selection is done
    selectionRect.visible = false

  // Ensure it doesn't obstruct or alter the appearance of DataBox
  background = Background(
    Array(BackgroundFill(Color.Transparent, CornerRadii.Empty, Insets.Empty))
  )
  // Set to be as big as the DataBox or the container it's in
  this.prefWidth <== cardsBox.width
  this.prefHeight <== cardsBox.height

  // Event handler for when the mouse is pressed down
  this.onMousePressed = event => handleMousePressed(event)

  // Event handler for when the mouse is dragged
  this.onMouseDragged = event => handleMouseDragged(event)

  // Event handler for when the mouse is released
  this.onMouseReleased = _ => handleMouseReleased()
