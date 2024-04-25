package GraphDisplay

import Alerts.WrongDataTimeSeries
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import scala.collection.mutable

/** Implements a time series display based on a collection of data cards.
  */
class LineChartTimeSeriesDisplay(var chosenData: mutable.Buffer[Card])
    extends ChartDisplay:
  var chart: Option[LineChart[String, Number]] = None
  if chosenData.nonEmpty then
    val DisplayedData = chosenData.map(card => (card.header, card.value))
    try
      val sortedData = DisplayedData
        .map((x, y) => (LineChartTimeSeriesDisplay.parseDate(x.trim), y))
        .sortBy(_._1)

      val xAxis = new CategoryAxis() {
        label = "Time"
      }
      val yAxis = new NumberAxis() {
        label = "Value"
      }

      val seriesData = ObservableBuffer() ++ sortedData

      val series = new XYChart.Series[String, Number] {
        name = "Time Series Data"
        data = seriesData.map { case (x, y) =>
          XYChart.Data[String, Number](x.toString, y)
        }
      }

      val lineChart = new LineChart[String, Number](xAxis, yAxis) {
        title = "Time Series"
      }
      lineChart.data().add(series)
      chart = Some(lineChart)

    catch case _: Throwable => new WrongDataTimeSeries().showAndWait()
end LineChartTimeSeriesDisplay

/** Utility object for parsing dates from strings. It uses a specific date
  * format pattern ("dd/MM/yyyy") to convert strings into LocalDate objects.
  */
object LineChartTimeSeriesDisplay:
  // Date formatter to parse the strings
  private lazy val formatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US)

  // Function to safely parse dates
  private def parseDate(dateStr: String): LocalDate =
    LocalDate.parse(dateStr, formatter)
end LineChartTimeSeriesDisplay
