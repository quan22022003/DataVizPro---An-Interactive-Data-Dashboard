package Boxes

import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Tooltip}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.util.Duration

import scala.collection.mutable

/** SummaryBox class displays a summary of statistical data from an array of
  * Card objects. It extends HBox to arrange summary cards horizontally with
  * customizable spacing and background.
  *
  * @param dataCards
  *   An array of Card objects containing the data for summary calculation.
  */
class SummaryBox(dataCards: mutable.Buffer[Card]) extends VBox:
  // Setting background color for the whole HBox
  this.setBackground(
    Background(
      Array(
        new BackgroundFill(
          Color.color(0, 0, 0),
          CornerRadii.Empty,
          Insets.Empty
        )
      )
    )
  )
  this.setSpacing(20) // Setting spacing between elements
  padding = Insets(20)

  // Calculating summary statistics
  private val sum = dataCards.map(_.value).sum
  private val minCard =
    if (dataCards.nonEmpty) dataCards.minBy(_.value) else null
  private val maxCard =
    if (dataCards.nonEmpty) dataCards.maxBy(_.value) else null
  private val average = sum / dataCards.length
  private val variance = dataCards
    .foldLeft(0.0)((acc, card) =>
      acc + math.pow(card.value - average, 2)
    ) / dataCards.length
  private val standardDeviation = math.sqrt(variance).toFloat

  /** Returns the sum of all the values in the data cards.
    *
    * @return
    *   The sum of values as a Float.
    */
  def getSum: Float = sum

  /** Returns the card with the minimum value.
    *
    * @return
    *   The `Card` object with the minimum value. If the allCards array is
    *   empty, returns `null`.
    */
  def getMin: Card = minCard

  /** Returns the card with the maximum value.
    *
    * @return
    *   The `Card` object with the maximum value. If the allCards array is
    *   empty, returns `null`.
    */
  def getMax: Card = maxCard

  /** Returns the average of all the values in the data cards.
    *
    * @return
    *   The average of values as a Float. If the allCards array is empty, the
    *   result is `NaN`.
    */
  def getAverage: Float = average

  /** Returns the standard deviation of all the values in the data cards.
    * Standard deviation measures the amount of variation or dispersion of a set
    * of values.
    *
    * @return
    *   The standard deviation of values as a Float. If the allCards array is
    *   empty, the result is `NaN`.
    */
  def getStandardDeviation: Float = standardDeviation

  /** Helper method to create a VBox with a specified label and tooltip. This
    * method is used to generate individual statistic cards for the summary.
    *
    * @param labelText
    *   The text to display on the label of the card.
    * @param tooltipText
    *   The text to display in the tooltip when hovering over the card.
    * @return
    *   A VBox configured as a summary card.
    */
  private def createCard(labelText: String, tooltipText: String): HBox =
    val label = new Label(labelText) {
      wrapText = true
      font = Font("Georgia", 15)
    }

    label.setTooltip(new Tooltip(tooltipText) {
      showDelay = Duration(0)
    })
    val card = new HBox(label) {
      alignment = Pos.Center
      // #ffb7ce
      background = Background(
        Array(
          BackgroundFill(
            Color.color(1.0, 0.718, 0.808),
            CornerRadii(0),
            Insets(0)
          )
        )
      )
    }
    card.prefWidthProperty().bind(this.widthProperty)
    card.prefHeightProperty().bind(this.heightProperty)
    card
  end createCard

  // Create cards for each statistic
  private val minCardVBox = createCard(
    f"Min: ${if (minCard != null) f"${minCard.value}%.2f" else "N/A"}",
    s"Data point having minimum value: ${
        if (minCard != null) f"(${minCard.header}, ${minCard.value})" else "N/A"
      }"
  )
  private val sumCard = createCard(f"Sum: $sum%.2f", "Total sum of values")
  private val maxCardVBox = createCard(
    f"Max: ${if (maxCard != null) f"${maxCard.value}%.2f" else "N/A"}",
    s"Data point having maximum value: ${
        if (maxCard != null) f"(${maxCard.header}, ${maxCard.value})" else "N/A"
      }"
  )
  private val averageCard =
    createCard(f"Average: $average%.2f", f"Mean of values is $average%.2f")
  private val sdCard = createCard(
    f"Standard Deviation: $standardDeviation%.2f",
    f"Standard deviation of values is $standardDeviation%.2f"
  )

  // Add all cards to the HBox
  children = Seq(sumCard, minCardVBox, maxCardVBox, averageCard, sdCard)
