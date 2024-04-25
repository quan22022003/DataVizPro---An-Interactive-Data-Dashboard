package Boxes

import GraphDisplay.ChartDisplay
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, VBox}
import scalafx.scene.paint.Color

/** A visual container for diagrams/charts in the data dashboard. Sets a
  * specific background and alignment for the diagrams area.
  */
class ChartBox extends VBox():

  /** A helper method to set a new chart display for the chart box.
    * @param chartDisplay
    *   A chart display to be set.
    */
  def setChartDisplay(chartDisplay: ChartDisplay): Unit =
    maybeChartDisplay = Some(chartDisplay)

  /** A helper method to get the chart display of the chart box.
    * @return
    *   maybeChartDisplay An object of class Option[ChartDisplay]
    */
  def getChartDisplay: Option[ChartDisplay] = maybeChartDisplay

  private var maybeChartDisplay: Option[ChartDisplay] = None
  this.setBackground(
    Background(
      Array(new BackgroundFill(Color.White, CornerRadii.Empty, Insets.Empty))
    )
  )
  this.setAlignment(Pos.Center)
