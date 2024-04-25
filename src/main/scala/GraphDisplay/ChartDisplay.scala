package GraphDisplay

import scalafx.scene.chart.*
import scalafx.scene.paint.Color

/** A trait representing the essential structure and functionality required for
  * all chart displays within the application. It enforces a common interface
  * for chart objects.
  */
trait ChartDisplay:
  // Defines an optional Chart object, allowing for the possibility of no chart being present (e.g., due to empty data).
  def chart: Option[Chart]

  // Default background color for all charts.
  val backgroundColor: Color = Color.White
