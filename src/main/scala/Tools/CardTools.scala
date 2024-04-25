package Tools

import Alerts.DataError
import Boxes.{CardsBox, SummaryBox}
import InputDialog.*
import scalafx.Includes.jfxGridPane2sfx
import scalafx.scene.control.{MenuButton, MenuItem}
import scalafx.scene.text.Font

/** Provides tools for interacting with cards and summaries in the dashboard.
  * This includes functionality to change card sizes, colors, add new data,
  * duplicate, remove, and perform rectangle selection on cards.
  *
  * @param cardsBox
  *   An instance of CardsBox containing the data box.
  * @param summary
  *   An instance of SummaryBox containing the data box.
  */
class CardTools(cardsBox: CardsBox, summary: SummaryBox)
    extends MenuButton("Card Tools"):

  // Create a transparent Pane that will serve as the canvas for the rectangle
  private val selectionOverlay = new SelectionOverlay(cardsBox)

  // Buttons for increasing and decreasing card size, leveraging the common button creation method
  private val increaseSize =
    createButton("Increase Card Size", () => adjustCardSize(1.25))
  private val decreaseSize =
    createButton("Decrease Card Size", () => adjustCardSize(0.75))
  // Button for adding new data, invoking a dialog to collect user input
  private val addData = createButton("Add Data", handleAddData)

  // Button for duplicating selected cards
  private val duplicate =
    createButton("Duplicate Chosen Cards", handleDuplicateCards)
  // Button for removing selected cards
  private val remove = createButton("Remove Chosen Cards", handleRemoveCards)
  // Button and mechanism for rectangle selection of cards
  private val rectangleSelectionTool =
    createButton("Turn on rectangle selection", handleRectangleSelection)

  // Button for hiding and showing selected cards
  private val hide = createButton("Hide Chosen Cards", toggleHideShow)
  private var isHidden = false

  /** Creates a button with common styling and a click event handler.
    *
    * @param text
    *   The text displayed on the button.
    * @param handler
    *   The function to call when the button is clicked.
    * @return
    *   A configured MenuItem instance.
    */
  private def createButton(text: String, handler: () => Unit): MenuItem =
    new MenuItem(text) {
      font = Font("Times New Roman")
      onAction = _ => handler()
    }

  /** Adjusts the size of the cards in the data box.
    *
    * @param factor
    *   The factor by which to adjust the card size. Greater than 1 increases
    *   size, less than 1 decreases size.
    */
  private def adjustCardSize(factor: Double): Unit =
    cardsBox.prefTileHeight = cardsBox.prefTileHeight.toDouble * factor
    cardsBox.prefTileWidth = cardsBox.prefTileHeight.toDouble * factor

  /** Handles the addition of new data through a dialog interface. Adds a new
    * card to the data box if valid data is provided.
    */
  private def handleAddData(): Unit =
    val dialog = new DataPointInputDialog
    dialog.showAndWait() match
      case Some((header: String, value: Float)) => addNewCard(header, value)
      case _                                    => new DataError().showAndWait()

  /** Adds a new card with the specified header and value to the data box and
    * refreshes the summary.
    *
    * @param header
    *   The header text for the new card.
    * @param value
    *   The value for the new card.
    */
  private def addNewCard(header: String, value: Float): Unit =
    val newCard = new Card(header, value)
    setupCardClickBehavior(
      newCard
    ) // Ensures new card has click behavior for selection
    cardsBox.addNewCards(newCard)
    cardsBox.refreshCardsDisplay()
    updateSummary() // Updates the summary to reflect the change in data

  // Helper method for setting up the behavior when a card is clicked
  private def setupCardClickBehavior(card: Card): Unit =
    card.onMouseClicked = _ => cardsBox.toggleCardSelection(card)

  /** Toggles the visibility of selected cards. If cards are currently shown,
    * hides them, and vice versa.
    */
  private def toggleHideShow(): Unit =
    if isHidden then
      cardsBox.children = cardsBox.getAllCards
      hide.text = "Hide Chosen Cards"
      isHidden = false
    else
      cardsBox.children = cardsBox.getAllCards.diff(cardsBox.getChosenCards)
      hide.text = "Show Hidden Cards"
      isHidden = true

  /** Duplicates the currently selected cards, adding them to the data box and
    * deselecting them.
    */
  private def handleDuplicateCards(): Unit =
    val duplicatedCards =
      cardsBox.getChosenCards.map(card => new Card(card.header, card.value))
    duplicatedCards.foreach { newCard =>
      setupCardClickBehavior(newCard)
      cardsBox.addNewCards(newCard)
    }
    cardsBox.getChosenCards.foreach(_.toggleClicked(false))
    cardsBox.getChosenCards.clear()
    cardsBox.refreshCardsDisplay()
    updateSummary()
  end handleDuplicateCards

  /** Removes the currently selected cards from the data box and updates the
    * summary.
    */
  private def handleRemoveCards(): Unit =
    cardsBox.removeChosenCards()
    cardsBox.refreshCardsDisplay()
    updateSummary()
  end handleRemoveCards

  /** Updates the summary to reflect the current state of the data cards.
    */
  private def updateSummary(): Unit =
    summary.children = Seq(new SummaryBox(cardsBox.getAllCards))
  end updateSummary

  /** Enables selection of cards using a rectangular selection area.
    */
  private def handleRectangleSelection(): Unit =
    // Get the parent of the cardsBox to put an overlay over it
    val container =
      cardsBox.parent.value.asInstanceOf[javafx.scene.layout.GridPane]
    if selectionOverlay.isActive then
      container.children.remove(selectionOverlay)
      selectionOverlay.toggleStatus()
      rectangleSelectionTool.text = "Turn on rectangle selection"
    else
      container.add(
        selectionOverlay,
        columnIndex = 0,
        rowIndex = 2,
        colspan = 1,
        rowspan = 1
      )
      selectionOverlay.toggleStatus()
      rectangleSelectionTool.text = "Turn off rectangle selection"

  // Aggregate all components and bind their properties for layout
  private val comps = Array(
    increaseSize,
    decreaseSize,
    addData,
    remove,
    hide,
    duplicate,
    rectangleSelectionTool
  )

  items = comps
end CardTools
