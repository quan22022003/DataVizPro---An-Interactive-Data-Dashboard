package GraphDisplay

import Alerts.DataError
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.*

import scala.collection.mutable

/** Creates a bar chart display from a collection of data cards. It groups data
  * by the 'header' field of each card and sums up the 'value' fields for cards
  * with the same header.
  */
class BarChartDisplay(chosenData: mutable.Buffer[Card]) extends ChartDisplay:
  var chart: Option[BarChart[String, Number]] = None

  try
    if (chosenData.nonEmpty) // Checks if there is any data to display.
      val displayData =
        chosenData // Group data by header and sum values, preparing for display.
          .groupBy(_.header)
          .map((header, cards) => (header, cards.map(_.value).sum))

      // Configure the X and Y axes with appropriate labels.
      val xAxis = new CategoryAxis {
        label = "Headers"
      }
      val yAxis = new NumberAxis() {
        label = "Numbers"
        tickUnit = 0.5
      }
      // Initialize the bar chart with the configured axes.
      val barChart = new BarChart(xAxis, yAxis) {
        legendVisible = false
        title = "New Bar Chart"
      }

      // Create and add a series with the display data to the chart.
      val series = new XYChart.Series[String, Number] {
        data = (ObservableBuffer() ++ displayData).map { case (x, y) =>
          XYChart.Data[String, Number](x, y)
        }
      }

      barChart.data().add(series)
      barChart.style = "-fx-bar-fill: #00FFFA;"

      chart = Some(barChart)
  catch case _: Throwable => new DataError().showAndWait()
