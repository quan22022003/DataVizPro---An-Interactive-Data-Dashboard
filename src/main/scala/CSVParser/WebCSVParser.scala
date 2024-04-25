package CSVParser

import java.io.{BufferedReader, InputStreamReader}
import java.net.URL

/** Handles reading and processing data from an online .csv file.
  *
  * @param filePath
  *   The URL of the data source.
  */
class WebCSVParser(filePath: String) extends CSVParser:
  def readFile(): Boolean =
    // Initialize BufferedReader to read from a URL
    val url = URL(filePath)
    val reader = BufferedReader(InputStreamReader(url.openStream()))
    readBufferedReader(reader)
