package Boxes

import scalafx.geometry.Insets
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, TilePane}
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.collection.mutable.Buffer

/** A container for displaying data as cards in a tile format.
  *
  * @param headers
  *   The headers for each column of data.
  * @param numbers
  *   The numerical data corresponding to each header, organized as rows.
  */
class CardsBox(headers: Seq[String], numbers: Seq[Float]) extends TilePane:
  // Set the initial appearance and layout properties of the TilePane
  background = Background(
    Array(BackgroundFill(Color.Black, CornerRadii.Empty, Insets.Empty))
  )
  padding = Insets(10)
  hgap = 5.0
  vgap = 5.0
  prefColumns = 8
  prefTileWidth = 80
  prefTileHeight = 50

  // Initialize storage for data cards and selected (chosen) data cards
  private val allCards = mutable.Buffer[Card]()
  private val chosenCards = mutable.Buffer[Card]()

  /** Toggles the selection state of a card, adding or removing it from the
    * chosen data.
    *
    * @param card
    *   The card to toggle.
    */
  def toggleCardSelection(card: Card): Unit =
    if card.isClicked then unselectCard(card)
    else selectCard(card)

  /** A getter to get allCards
    *
    * @return
    *   allCards The collection of all data cards in CardsBox
    */
  def getAllCards: mutable.Buffer[Card] =
    allCards

  /** A getter to get chosenCards
    *
    * @return
    *   chosenCards The collection of all chosen cards in CardsBox
    */
  def getChosenCards: mutable.Buffer[Card] =
    chosenCards

  /** Adds a new card to the collection of all cards.
    *
    * @param newCard
    *   The new Card object to be added to `allCards`.
    */
  def addNewCards(newCard: Card): Unit =
    allCards += newCard

  /** Selects a new card by adding it to the collection of chosen cards.
    *
    * @param newCard
    *   The new Card object to be added to `chosenCards`.
    */
  def selectCard(newCard: Card): Unit =
    chosenCards += newCard
    newCard.toggleClicked(true)

  /** Unselects a new card by removing it from the collection of chosen cards.
    *
    * @param newCard
    *   The new Card object to be added to `chosenCards`.
    */
  def unselectCard(newCard: Card): Unit =
    chosenCards -= newCard
    newCard.toggleClicked(false)

  /** Removes all cards from `allCards` that are currently selected (i.e., in
    * `chosenCards`). The `chosenCards` buffer holds references to the cards
    * that have been interactively selected by the user.
    */
  def removeChosenCards(): Unit =
    allCards --= chosenCards
    chosenCards.clear()

  /** Helper method to refresh the display of cards in the CardsBox
    */
  def refreshCardsDisplay(): Unit =
    children = allCards

  // Populate the CardsBox with cards based on the provided headers and data
  headers.indices.foreach { i =>
    val newCard = new Card(headers(i), numbers(i))
    addNewCards(newCard)
    // Attach a click event handler to toggle the card's selection state
    newCard.onMouseClicked = _ => toggleCardSelection(newCard)
  }

  refreshCardsDisplay()
end CardsBox
