package DashboardHandlers

import Alerts.InvalidQuanFile
import Boxes.{CardsBox, ChartBox}
import GraphDisplay.*
import scalafx.scene.paint.Color

import java.io.{BufferedReader, FileReader}

/** Loads and processes the data from a file into a dashboard format, ready for
  * display in the application.
  *
  * @param fileName
  *   Path to the file containing the data to be imported.
  */
class DashboardImporter(fileName: String):

  // Use ImportDashboardUtils to get the parsed data
  private val (color, graphType, headers, data) =
    DashboardImporter.parseFile(fileName)

  // Creates a new ChartBox, a component for displaying diagrams or charts.
  private val chartBox = new ChartBox
  // Create the visual elements of the dashboard with the parsed data
  private val cardsBox = new CardsBox(headers, data)

  // Set the color of the cards
  cardsBox.getAllCards.foreach(_.changeColor(color))
  // add chart to the chartBox
  addChart(cardsBox, graphType, color)

  /** Retrieves the main GUI components used for data display within the
    * application.
    *
    * This method provides access to the primary visual elements used to present
    * data, enabling other parts of the application to interact with or modify
    * these components.
    *
    * @return
    *   A tuple containing:
    *   - `CardsBox`: A component for displaying data cards. These cards are
    *     visual containers for data points or information sets, allowing for
    *     structured data presentation.
    *   - `ChartBox`: A component responsible for rendering charts. This box
    *     supports various types of data visualizations and is crucial for
    *     graphical data analysis.
    */
  def getBoxes: (CardsBox, ChartBox) =
    (this.cardsBox, this.chartBox)

  /** Add chart to the ChartBox
    *
    * @param cardsBox
    *   The CardsBox whose data cards to be displayed.
    * @param graphType
    *   The type of graph to display.
    * @param color
    *   The color to apply to the data cards.
    */
  private def addChart(
    cardsBox: CardsBox,
    graphType: String,
    color: Color
  ): Unit =

    val cards = cardsBox.getAllCards
    // Select and create the appropriate chart based on the graph type
    val chartDisplay = graphType match
      case "1" => new BarChartDisplay(cards)
      case "2" => new MultiSeriesScatterChartDisplay(cards)
      case "3" => new PieChartDisplay(cards)
      case "4" => new LineChartTimeSeriesDisplay(cards)
      case "5" => new ScatterChartDisplay(cards)
      case _ =>
        Alerts.InvalidGraph().showAndWait()
        throw new Exception("The graph type is invalid.")

    chartBox.children.add(chartDisplay.chart.get)
    chartBox.setChartDisplay(chartDisplay)
  end addChart

/** Handles reading and parsing of data from files for the dashboard.
  */
object DashboardImporter:

  /** Parses the specified file, extracting the color, graph type, headers, and
    * data.
    *
    * @param fileName
    *   Path to the file to be parsed.
    * @return
    *   A tuple containing the extracted color, graph type, headers, and data.
    */
  def parseFile(fileName: String): (Color, String, Seq[String], Seq[Float]) =
    var headers = Seq.empty[String]
    var data = Seq.empty[Float]
    var color = Color.White
    var graphType = ""

    val fileObject = new FileReader(fileName)
    val reader = new BufferedReader(fileObject)
    try
      val colorLine = reader.readLine()
      if (colorLine == null)
        new InvalidQuanFile().showAndWait()
        throw new IllegalArgumentException(
          "File is empty or does not start with a color line."
        )
      color = parseColor(colorLine)

      val graphTypeLine = reader.readLine()
      if (graphTypeLine == null)
        new InvalidQuanFile().showAndWait()
        throw new IllegalArgumentException(
          "File does not contain a graph type line following the color line."
        )
      graphType = graphTypeLine.trim

      val headersLine = reader.readLine()
      if (headersLine == null)
        new InvalidQuanFile().showAndWait()
        throw new IllegalArgumentException(
          "File does not contain headers following the graph type line."
        )
      headers = headersLine.split(',').toSeq

      val dataLines =
        reader.readLine()
      if (dataLines.isEmpty)
        new InvalidQuanFile().showAndWait()
        throw new IllegalArgumentException("No data lines found in the file.")
      data = dataLines.trim.split(',').map(_.toFloat)
    catch
      case e: Exception =>
        // Log the exception
        println(s"Failed to parse file: ${e.getMessage}")
        // Re-throw the exception to propagate the error up the call stack
        throw e
    finally
      reader.close()
      fileObject.close()

    (color, graphType, headers, data)

  /** Parses the RGB color values from a comma-separated string.
    *
    * @param colorString
    *   The string containing color values.
    * @return
    *   The Color object parsed from the string.
    */
  def parseColor(colorString: String): Color =
    val rgb = colorString.trim.split(',').map(_.toDouble)
    Color.color(rgb(0), rgb(1), rgb(2))
