package scala

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Tooltip}
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.util.Duration

/** Represents a graphical card that displays a value and a header. The card can
  * be in a clicked or unclicked state, which changes its appearance.
  *
  * @param header
  *   The header text of the card.
  * @param value
  *   The numerical value displayed on the card.
  */

class Card(val header: String, var value: Float) extends VBox:
  // Initial card colors
  private var unclickedColor: Color = Color.color(1, 0.718, 0.808)
  private val clickedColor = Color.color(1, 0.525, 0.675)
  // Tooltip setup
  private val infoTooltip = new Tooltip(s"$header: $value") {
    showDelay = Duration(0)
  }

  // Current background starts as unclicked
  this.updateBackgroundColor(unclickedColor)
  // Label setup
  private val numLabel = new Label(value.toString) {
    tooltip = infoTooltip
    font = Font("Times New Roman", 18)
  }

  numLabel.autosize()

  this.prefWidth = 100
  this.prefHeight = 75
  this.padding = Insets(8)
  this.alignment = Pos.Center

  this.children.add(numLabel)
  // Track whether the card is in clicked state
  private var _isClicked = false

  /** Returns string of the default unclicked color of this card.
    *
    * @return
    *   The color shown when the card is unclicked
    */
  def getDefaultColor: Color = unclickedColor

  /** Updates the card's background color based on its clicked state.
    *
    * @param isClicked
    *   Boolean indicating whether the card is clicked.
    */
  private def updateBackgroundColor(color: Color): Unit =
    this.background = Background(
      Array(BackgroundFill(color, CornerRadii.Empty, Insets.Empty))
    )

  /** Toggles the card's clicked state and updates its background color.
    *
    * @param clicked
    *   Boolean indicating the new clicked state.
    */
  def toggleClicked(clicked: Boolean): Unit = {
    _isClicked = clicked
    updateBackgroundColor(if (_isClicked) clickedColor else unclickedColor)
  }

  /** Changes the card's color to a specified new color.
    *
    * @param newColor
    *   The new Color object to apply to the card's background.
    */
  def changeColor(newColor: Color): Unit =
    updateBackgroundColor(newColor)
    this.unclickedColor = newColor
  end changeColor

  // Getters for clicked state
  def isClicked: Boolean = _isClicked
