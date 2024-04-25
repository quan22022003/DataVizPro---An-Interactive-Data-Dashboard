package Alerts

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType

/** Represents an alert for when there is invalid data input.
  */
class DataError extends Alert(AlertType.ERROR):
  this.setTitle("Invalid Data Input")
  this.setContentText("No Cards Chosen or Wrong Data Format")

/** Represents an alert for when there is invalid data input.
  */
class NoCardsError extends Alert(AlertType.ERROR):
  this.setTitle("No Cards Chosen")
  this.setContentText("Please choose some cards before plotting charts")

/** Represents an alert for when there is invalid data input for the scatter
  * plot.
  */
class WrongDataScatter extends Alert(AlertType.ERROR):
  this.setTitle("Invalid Data Input For Scatter Plot")
  this.setContentText(
    "No Data Chosen or the Headers (X values) are not numbers."
  )

/** Represents an alert for when there is invalid data input for the
  * multiple-series scatter plot.
  */
class WrongDataMultiScatter extends Alert(AlertType.ERROR):
  this.setTitle("Invalid Data Input For Multi-series Scatter Plot")
  this.setContentText(
    "No Data Chosen or the Headers (X values) are not of format '%Series%;%X_values%'."
  )

/** Represents an alert for when there is invalid data input for the scatter
  * plot.
  */
class WrongDataTimeSeries extends Alert(AlertType.ERROR):
  this.setTitle("Invalid Data Input For Time Series")
  this.setContentText(
    "No Data Chosen or the Headers (Dates ) are not of format 'dd/mm/yyyy'."
  )

/** Represents an alert for when an invalid file is input, specifically non-CSV
  * files.
  */
class WrongExtension extends Alert(AlertType.ERROR):
  setTitle("Invalid File Input")
  setContentText("Please only choose CSV files")

/** Represents an alert for when an invalid file format is input, specifically
  * non-.quan files.
  */
class NotQuanExtension extends Alert(AlertType.ERROR):
  setTitle("Invalid File Format")
  setContentText("Please only choose .quan files")

/** Represents an alert for when an invalid .quan files is input.
  */
class InvalidQuanFile extends Alert(AlertType.ERROR):
  setTitle("Wrong Quan File")
  setContentText("Please only choose valid .quan files")

/** Represents an alert for when an invalid .quan files is input.
  */
class InvalidCSVFile extends Alert(AlertType.ERROR):
  setTitle("Invalid CSV File")
  setContentText("Please only choose valid csv files")

/** Represents an alert for when an invalid graph files is input.
  */
class InvalidGraph extends Alert(AlertType.ERROR):
  setTitle("Invalid Graph")
  setContentText("The saved graph in the file is invalid.")
