package GraphDisplay

import Alerts.WrongDataScatter
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.*

import scala.collection.mutable.Buffer

/** Implements a scatter plot display based on a collection of data cards. It
  * translates each card into a point in the scatter plot based on its header (X
  * value) and value (Y value).
  */
class ScatterChartDisplay(var chosenData: Buffer[Card]) extends ChartDisplay:
  var chart: Option[ScatterChart[Number, Number]] = None
  try
    if chosenData.nonEmpty then
      // Calculates the bounds for the X and Y axes based on the data.
      val displayData = ObservableBuffer() ++ chosenData.map(card =>
        (card.header.toFloat, card.value)
      )

      val upperBoundX = displayData.map(_._1).max + 1
      val lowerBoundX = displayData.map(_._1).min - 1
      val upperBoundY = displayData.map(_._2).max + 1
      val lowerBoundY = displayData.map(_._2).min - 1

      val xAxis = new NumberAxis(
        lowerBound = lowerBoundX,
        upperBound = upperBoundX,
        tickUnit = 0.5
      ) {
        label = "X"
      }
      val yAxis = new NumberAxis(
        lowerBound = lowerBoundY,
        upperBound = upperBoundY,
        tickUnit = 0.5
      ) {
        label = "Y"
      }

      val series = new XYChart.Series[Number, Number] {
        data = displayData.map { case (x, y) =>
          XYChart.Data[Number, Number](x, y)
        }
      }

      // Initializes the scatter chart with the configured axes and adds the series.
      val scatterChart = new ScatterChart(xAxis, yAxis) {
        legendVisible = false
        title = "New Scatter Chart"
      }
      scatterChart.data().add(series)

      chart = Some(scatterChart)
  catch case _: Throwable => new WrongDataScatter().showAndWait()
