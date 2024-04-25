package CSVParser

import java.io.BufferedReader

/** An abstract class designed for parsing CSV (Comma-Separated Values) files.
  * This class provides a framework for reading data from various sources into a
  * standardized format, specifically focusing on extracting headers and
  * numerical data from CSV files.
  */
abstract class CSVParser:
  /** Headers extracted from the CSV file, representing column names.
    */
  private var headers: Seq[String] = Seq.empty

  /** Numerical data extracted from the CSV file, represented as a sequence of
    * sequences of floats. Each inner sequence corresponds to a row in the CSV
    * file, with each float representing a cell value.
    */
  private var data: Seq[Float] = Seq.empty

  /** Getter for headers.
    *
    * @return
    *   A sequence of strings representing the headers of the CSV file.
    */
  def getHeaders: Seq[String] = headers

  /** Getter for data.
    *
    * @return
    *   A sequence of floats representing the numerical data from the CSV file.
    */
  def getData: Seq[Float] = data

  /** Abstract method to read and process data from a specific source.
    * Implementations should handle the opening of the data source, call
    * `readBufferedReader` to process the data, and ensure any resources are
    * closed or released appropriately.
    *
    * @return
    *   Boolean indicating whether the file was successfully read and processed.
    */
  def readFile(): Boolean

  /** Helper method to process CSV data from a BufferedReader. Utilizes the
    * shared utility function `readLines` from the CSVParser companion object to
    * process lines into headers and numerical data.
    *
    * @param reader
    *   The BufferedReader to read from.
    * @return
    *   Boolean indicating whether the data was successfully processed.
    */
  protected def readBufferedReader(reader: BufferedReader): Boolean =
    try
      val (headersRead, dataRead) = CSVParser.readLines(reader)
      if headersRead.nonEmpty && dataRead.length == headersRead.length
      then
        headers = headersRead
        data = dataRead
        true
      else false
    finally reader.close()

/** Companion object for the CSVParser class, providing utility functions to
  * support CSV file parsing.
  */
object CSVParser:
  /** Static method to read and process lines from a BufferedReader into headers
    * and numerical data. The first line is read as the headers (column names),
    * and subsequent line is processed as data row.
    *
    * @param reader
    *   The BufferedReader to read from.
    * @return
    *   A tuple containing the headers (Seq[String]) and the data (Seq[Float]).
    */
  private def readLines(reader: BufferedReader): (Seq[String], Seq[Float]) =
    try
      val firstLine = Option(reader.readLine()).getOrElse("")
      val secondLine = Option(reader.readLine()).getOrElse("")
      val headers = firstLine.trim.split(",").toSeq
      val data = secondLine.trim.split(",").map(_.toFloat).toSeq
      (headers, data)
    catch case _: NumberFormatException => (Seq.empty, Seq.empty)
