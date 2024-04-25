package Tools

import Alerts.DataError
import Boxes.{CardsBox, ChartBox}
import DashboardHandlers.DashboardExporter
import GraphDisplay.*
import InputDialog.*
import scalafx.scene.chart.Chart
import scalafx.scene.control.{MenuButton, MenuItem, Tooltip}
import scalafx.scene.text.Font
import scalafx.util.Duration

import java.io.File

/** Provides interactive tools for generating and managing charts in the
  * dashboard. Allows users to create various types of charts, save the
  * dashboard, and manipulate data visualization.
  *
  * @param chartBox
  *   The container where the generated charts will be displayed.
  * @param cardsBox
  *   The container holding the data used for chart generation.
  */
class ChartTools(val chartBox: ChartBox, val cardsBox: CardsBox)
    extends MenuButton("Chart Tools"):

  private val chosenData = cardsBox.getChosenCards
  // Configuration and initialization of chart generation buttons.
  private val barChartButton = createChartButton(
    "Bar Chart",
    () => new BarChartDisplay(cardsBox.getChosenCards),
    Some(createTooltip("Create new bar chart"))
  )
  private val pieChartButton = createChartButton(
    "Pie Chart",
    () => new PieChartDisplay(cardsBox.getChosenCards),
    Some(createTooltip("Create new pie chart"))
  )
  private val scatterChartButton = createChartButton(
    "Scatter Chart",
    () => new ScatterChartDisplay(cardsBox.getChosenCards),
    Some(createTooltip("Create new scatter chart"))
  )

  private val timeSeriesScatterButton = createChartButton(
    "Time Series Line Chart",
    () => new LineChartTimeSeriesDisplay(cardsBox.getChosenCards),
    Some(createTooltip("Headers must be of format 'dd/mm/yyyy'"))
  )
  private val timeSeriesBarButton = createChartButton(
    "Multiple Series Scatter Chart",
    () => new MultiSeriesScatterChartDisplay(cardsBox.getChosenCards),
    Some(createTooltip("Headers must be of format '%Series%;%X_values%'"))
  )

  /** Configures and handles the functionality for saving the current dashboard
    * state to a file.
    */
  private val saveButton = new MenuItem("Save To File") {
    font = Font("Times New Roman")
    wrapText = true
    tooltip = createTooltip("Save this dashboard to file")
    onAction = _ => saveDashboard()
  }
  // Adding all the interactive buttons to the VBox.
  private val components = Array(
    barChartButton,
    pieChartButton,
    scatterChartButton,
    timeSeriesScatterButton,
    timeSeriesBarButton,
    saveButton
  )
  items = components

  /** Creates a tooltip with the specified message and an optional delay.
    *
    * @param message
    *   The message to be displayed in the tooltip.
    * @param delay
    *   The delay before showing the tooltip. Defaults to immediate display.
    * @return
    *   A Tooltip instance with the specified configuration.
    */
  private def createTooltip(
    message: String,
    delay: Duration = Duration(0)
  ): Tooltip =
    new Tooltip(message) {
      showDelay = delay
    }

  /** Creates a button for generating a specific type of chart.
    *
    * @param label
    *   The text label of the button.
    * @param chartType
    *   A function that returns a new instance of ChartDisplay.
    * @param providedTooltip
    *   An optional tooltip for the button.
    * @return
    *   A configured MenuItem instance.
    */
  private def createChartButton(
    label: String,
    chartType: () => ChartDisplay,
    providedTooltip: Option[Tooltip] = None
  ): MenuItem =
    val item = new MenuItem(label) {
      font = Font("Times New Roman")
      wrapText = true
      tooltip =
        providedTooltip.getOrElse(createTooltip("Save this dashboard to file"))
      onAction = _ => generateChart(chartType)
    }
    item

  /** Generates a chart using the provided chart type function and updates the
    * UI accordingly.
    *
    * @param chartType
    *   A function that returns a new instance of ChartDisplay.
    */
  private def generateChart(chartType: () => ChartDisplay): Unit =
    val chartDisplay = chartType()
    chartDisplay.chart match {
      case Some(chart: Chart) =>
        chartBox.setChartDisplay(chartDisplay)
        chartBox.children = Array(chart)
      case _ =>
        new DataError().showAndWait()
    }

  /** Opens a file chooser dialog allowing the user to specify a file location
    * and name for saving the dashboard.
    */
  private def saveDashboard(): Unit =
    val savedChart = chartBox.getChartDisplay.getOrElse(None) match
      case bcd: BarChartDisplay                => "1"
      case mss: MultiSeriesScatterChartDisplay => "2"
      case pcd: PieChartDisplay                => "3"
      case lcts: LineChartTimeSeriesDisplay    => "4"
      case scd: ScatterChartDisplay            => "5"
      case _                                   => "0"

    val dialog = new SaveDashboardInputDialog()
    val filePathOption = dialog.showAndWait()
    filePathOption match
      case Some(filePath: String) =>
        val file = new File(filePath)
        val saver =
          new DashboardExporter(file, cardsBox.getAllCards, savedChart)
        saver.saveFile()
      case _ => new DataError().showAndWait()

  end saveDashboard
