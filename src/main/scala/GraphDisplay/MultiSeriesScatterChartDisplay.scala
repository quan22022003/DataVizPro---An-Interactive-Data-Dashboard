package GraphDisplay

import Alerts.WrongDataMultiScatter
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.*

import scala.collection.mutable

/** Implements a multi-series scatter plot display based on a collection of data
  * cards.
  */
class MultiSeriesScatterChartDisplay(var chosenData: mutable.Buffer[Card])
    extends ChartDisplay:
  var chart: Option[ScatterChart[Number, Number]] = None
  try
    if chosenData.nonEmpty then
      val displayData = ObservableBuffer() ++ chosenData.map(card =>
        (
          card.header.split(";")(0),
          card.header.split(";")(1).toFloat,
          card.value
        )
      )

      val upperBoundX = displayData.map(_._2).max + 1
      val lowerBoundX = displayData.map(_._2).min - 1
      val upperBoundY = displayData.map(_._3).max + 1
      val lowerBoundY = displayData.map(_._3).min - 1

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

      val scatterChart = new ScatterChart(xAxis, yAxis) {
        title = "New Multiple-Series Scatter Chart"
      }

      val displayDataBySeries = displayData.groupBy(_._1)
      displayDataBySeries.keys.foreach(seriesHeader => {
        val addedData =
          displayDataBySeries(seriesHeader).map((_, x, y) => (x, y))
        val series = new XYChart.Series[Number, Number] {
          name = seriesHeader
          data = addedData.map { case (x, y) =>
            XYChart.Data[Number, Number](x, y)
          }
        }
        scatterChart.data().add(series)
      })

      chart = Some(scatterChart)
  catch case _: Throwable => new WrongDataMultiScatter().showAndWait()
