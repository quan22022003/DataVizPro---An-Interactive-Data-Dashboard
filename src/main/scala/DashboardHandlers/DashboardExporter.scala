package DashboardHandlers

import scalafx.scene.paint.Color

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable

/** Handles saving of the dashboard state to a file.
  *
  * @param file
  *   The file where the data will be saved.
  * @param chosenData
  *   The data to save.
  * @param chartType
  *   The type of chart represented by the data.
  */
class DashboardExporter(
  file: File,
  chosenData: mutable.Buffer[Card],
  chartType: String
):
  private val color = chosenData.head.getDefaultColor
  private val headers = chosenData.map(_.header)
  private val values = chosenData.map(_.value.toString)

  /** Saves the serialized data to the file.
    */
  def saveFile(): Unit =
    val writer = new BufferedWriter(new FileWriter(file))
    try
      writer.write(DashboardExporter.colorToString(color))
      writer.newLine()
      writer.write(chartType)
      writer.newLine()
      writer.write(headers.mkString(","))
      writer.newLine()
      writer.write(values.mkString(","))
    finally writer.close()
end DashboardExporter

/** Utility object for parsing colors to string
  */
object DashboardExporter:
  /** Transforms the color to a string representation suitable for file storage.
    *
    * @param color
    *   The color to transform.
    * @return
    *   A string representation of the color.
    */
  private def colorToString(color: Color): String =
    f"${color.red},${color.green},${color.blue}"
