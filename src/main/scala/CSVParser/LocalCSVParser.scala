package CSVParser

import java.io.{BufferedReader, FileReader}

/** Handles reading and processing data from a local .csv file.
  *
  * @param filename
  *   The path to the file.
  */
class LocalCSVParser(filename: String) extends CSVParser:
  def readFile(): Boolean =
    // Initialize BufferedReader to read from a file
    val reader = BufferedReader(FileReader(filename))
    readBufferedReader(reader)
