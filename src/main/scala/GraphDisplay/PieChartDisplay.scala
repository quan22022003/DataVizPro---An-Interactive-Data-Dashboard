package GraphDisplay

import Alerts.DataError
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.*

import scala.collection.mutable

/** Creates a pie chart display from a collection of data cards. Similar to
  * BarChartDisplay, it aggregates data based on 'header', but represents it in
  * a pie chart format.
  */
class PieChartDisplay(chosenData: mutable.Buffer[Card]) extends ChartDisplay:
  var chart: Option[PieChart] = None
  try
    if chosenData.nonEmpty then // Proceeds only if data is available.
      // Prepare data by grouping by header and summing up the values.
      val pieChartData = ObservableBuffer(
        chosenData
          .groupBy(_.header)
          .view
          .mapValues(_.map(_.value).sum)
          .map { case (header, sum) => PieChart.Data(header, sum) }
          .toSeq*
      )
      val pieChart = new PieChart {
        data = pieChartData
        title = "New Pie Chart"
      }
      chart = Some(pieChart)
  catch case _: Throwable => new DataError().showAndWait()
