package scala

import Alerts.{InvalidCSVFile, NotQuanExtension, WrongExtension}
import Boxes.{CardsBox, ChartBox, SummaryBox}
import CSVParser.{CSVParser, LocalCSVParser, WebCSVParser}
import DashboardHandlers.*
import InputDialog.*
import Tools.{CardTools, ChartTools}
import scalafx.Includes.jfxColor2sfx
import scalafx.application.JFXApp3
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.stage.FileChooser

/** A ScalaFX application for data visualization. It provides a GUI for users to
 * either create a new dashboard or import an existing dashboard from a file.
 */
object GUI extends JFXApp3:

  /** The entry point for the ScalaFX application. Sets up the primary stage
   * with the initial scene.
   */
  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage {
      title = "DataViz Pro"
      scene = createInitialScene()
    }
  end start

  /** Processes a locally selected file, typically chosen through a file picker
   * dialog. It uses the file path to create a FileInput object and proceeds
   * with the data loading process.
   *
   * @param filePath
   *   The full path to the selected file.
   * @param web
   *   A boolean flag indicating if the file source is from the web (unused in
   *   this function).
   */
  private def processSelectedLocalFile(
                                        filePath: String,
                                        web: Boolean = false
                                      ): Unit =

    val (graphBox, mainInterface, mainScene) = initializeInterface()
    var file: Option[CSVParser] = None

    filePath.split('.').last.toLowerCase match
      case "csv" =>
        file = Some(LocalCSVParser(filePath))
        processFile(file, graphBox, mainInterface, mainScene)
      case _ =>
        new WrongExtension().showAndWait()
  end processSelectedLocalFile

  /** Similar to processSelectedLocalFile, this function handles files selected
   * from the web. It expects a URL input and processes the file accordingly.
   *
   * @param input
   *   The user-provided input, which is expected to be a URL pointing to a
   *   file.
   */
  private def processSelectedWebFile(
                                      input: Option[scalafx.scene.control.DConvert[String, String => String]#S]
                                    ): Unit =
    val (graphBox, mainInterface, mainScene) = initializeInterface()
    var file: Option[CSVParser] = None

    input match
      case Some(fileName: String) =>
        file = Some(WebCSVParser(fileName.trim))
        processFile(file, graphBox, mainInterface, mainScene)
      case _ =>
        new WrongExtension().showAndWait()
  end processSelectedWebFile

  /** Initializes the user interface components for the main data visualization
   * screen.
   *
   * This method sets up the primary grid layout that includes the data
   * visualization area (graphBox) and the data manipulation tools
   * (mainInterface). It also configures the column and row constraints for the
   * layout to ensure that the visualization and tools are displayed correctly.
   *
   * @return
   *   A triplet containing the graph box (data visualization area), main
   *   interface (tool area), and the main scene configured with these
   *   components.
   */
  private def initializeInterface(): (GridPane, GridPane, Scene) =
    val mainInterface = new GridPane()

    // Creates a new Scene 1920x1080 pixels containing the mainInterface grid
    val mainScene = Scene(mainInterface, 1920, 1080)

    val graphBox = new GridPane()

    // dividing it into two columns with widths of 80% and 20% of the grid's total width.
    graphBox.columnConstraints = Array(
      new ColumnConstraints {
        percentWidth = 20
      },
      new ColumnConstraints {
        percentWidth = 80
      }
    )

    // Adds graphBox to the mainInterface grid at column index 0, row index 0 and span 1 columns and 1 row.
    mainInterface.add(graphBox, 0, 1, 1, 1)

    mainInterface.rowConstraints = Array(
      new RowConstraints {
        percentHeight = 3
      },
      new RowConstraints {
        percentHeight = 59
      },
      new RowConstraints {
        percentHeight = 38
      }
    )

    (graphBox, mainInterface, mainScene)

  /** Creates the initial scene with tabs for the user interface. There are two
   * tabs: 'New Dashboard' for creating new dashboards, and 'Import Dashboard'
   * for importing existing dashboards from files.
   *
   * @return
   *   The scene with the tabbed interface.
   */
  private def createInitialScene(): Scene =
    val tabPane = new TabPane()

    val dashboardTab = new Tab {
      text = "New Dashboard"
      // sets the content of the tab to the new dashboard view
      content = newDashboardContent()
      closable = false
    }

    val importTab = new Tab {
      text = "Import Dashboard"
      // sets the content of the tab to the import dashboard view
      content = importDashboardContent()
      closable = false
    }

    // adds both tabs to the tab pane
    tabPane.tabs.addAll(dashboardTab, importTab)

    // creates a new scene containing the tab pane
    new Scene(tabPane)
  end createInitialScene

  /** Constructs the content for the 'New Dashboard' tab. It provides a UI for
   * users to import data from a file or the web.
   *
   * @return
   *   A VBox layout with the new dashboard UI components.
   */
  private def newDashboardContent(): VBox =
    val instruction =
      "The programme only supports CSV Files where the first row is comprised of Headers and second row is comprised of Values\n" +
        "The files must follow these templates:\n" +
        " - For bar charts and pie charts, headers are categories.\n" +
        " - For scatter plots, headers are X values (numbers) and values are Y values (numbers).\n" +
        " - For multiple-series scatter plots, headers are of format '%Series_name%;%X_value%' and values are Y values (numbers).\n" +
        " - For time series, headers are Dates (dd/mm/yyyy)."

    val layout = contentTemplate(instruction)

    val nameLabel = new Label("Dashboard Name:")
    val nameInput = new TextField {
      promptText = "Enter dashboard name"
    }

    val importFromFileButton = new Button("Import local CSV") {
      onMouseClicked = _ => handleFileImport()
    }
    val importFromWebButton = new Button("Import online CSV") {
      onMouseClicked = _ => handleWebImport()
    }

    layout.children.addAll(importFromFileButton, importFromWebButton)
    layout
  end newDashboardContent

  /** Constructs the content for the 'Import Dashboard' tab. It provides a UI
   * for users to select and import a .quan file.
   *
   * @return
   *   A VBox layout with the import dashboard UI components.
   */
  private def importDashboardContent(): VBox =
    val fileLabel = new Label("Select a File:")
    val filePathField = new TextField {
      editable = false
      prefWidth = 300
    }

    val browseButton = new Button("Browse...") {
      onAction = _ =>
        val fileChooser = new FileChooser {
          title = "Open Dashboard File"
        }
        val file = fileChooser.showOpenDialog(stage)
        if file != null then filePathField.text = file.getAbsolutePath

    }

    val hBox = new HBox(fileLabel, filePathField, browseButton) {
      spacing = 10
      alignment = Pos.Center
    }

    val importButton = new Button("Import Dashboard") {
      onAction = _ =>
        val fileName = filePathField.getText
        handleImportFromFile(fileName)
        println(s"Importing dashboard from file: ${filePathField.text.value}")

    }

    val instruction =
      "Import a leftover dashboard and continue working on it.\n" +
        "Supported file extension: '.quan'\n"

    val layout = contentTemplate(instruction)
    layout.children.addAll(hBox, importButton)
    layout
  end importDashboardContent

  /** A helper method to create a template for the content of both 'New
   * Dashboard' and 'Import Dashboard' tabs. It adds instructional text to the
   * layout.
   *
   * @param instruction
   *   The instruction text to be displayed at the top of the content.
   * @return
   *   A VBox layout with the formatted instruction label.
   */
  private def contentTemplate(instruction: String): VBox =
    val layout = new VBox():

      prefHeight = 400
      prefWidth = 800
      spacing = 12
      alignment = Pos.Center
      padding = Insets(10)

    val formatInstruction = new Label(instruction) {
      font = Font("Times New Roman", 12)
    }

    // adds the instruction label to the layout
    layout.children.add(formatInstruction)
    layout

  /** Handles the action for importing data from a local file. Opens a file
   * chooser dialog for the user to select a file.
   */
  private def handleFileImport(): Unit =
    val fileChooser = new FileChooser {
      title = "Choose File"
    }
    val selectedFile = fileChooser.showOpenDialog(stage)
    if selectedFile != null then processSelectedLocalFile(selectedFile.getPath)
  end handleFileImport

  /** Handles the action for importing data from the web. Opens a dialog for the
   * user to enter a URL.
   */
  private def handleWebImport(): Unit =
    val urlInput = new FilePathInputDialog
    val input = urlInput.showAndWait()
    processSelectedWebFile(input)
  end handleWebImport

  /** Handles importing a dashboard from a file selected by the user. Supports
   * specific ".quan" file formats.
   */
  private def handleImportFromFile(fileName: String): Unit =
    fileName.split('.').last.toLowerCase match
      case "quan" =>
        val (graphBox, mainInterface, mainScene) = initializeInterface()
        val dashboard = new DashboardImporter(fileName)
        val (cardsBox, chartBox) =
          dashboard.getBoxes
        finalizeInterface(
          cardsBox,
          graphBox,
          mainInterface,
          mainScene,
          Some(chartBox)
        )
      case _ =>
        new NotQuanExtension().showAndWait()

  end handleImportFromFile

  /** Loads data from a file into the application, creating the necessary data
   * structures and UI components for visualization.
   *
   * If the file contains valid data, it creates instances for data handling
   * (CardsBox) visualization (ChartBox). It then binds the UI components to
   * the main interface and displays the scene.
   *
   * @param file
   *   An Option containing a FileInput object representing the user-selected
   *   file.
   * @param graphBox
   *   A GridPane that serves as the container for the chart.
   * @param mainInterface
   *   A GridPane that holds the main user interface components.
   * @param mainScene
   *   The main Scene of the application where the UI components are displayed.
   */
  private def processFile(
                           file: Option[CSVParser],
                           graphBox: GridPane,
                           mainInterface: GridPane,
                           mainScene: Scene
                         ): Unit =
    try
      file.get.readFile()
      val headers = file.get.getHeaders
      val numbers = file.get.getData
      // Creates a new CardsBox instance, passing in the headers and data.
      val cardsBox = new CardsBox(headers, numbers)

      finalizeInterface(cardsBox, graphBox, mainInterface, mainScene)
    catch case _: Throwable => new InvalidCSVFile().showAndWait()
  end processFile

  /** Finalizes the user interface by setting up and displaying all necessary
   * and properly sized components in the main interface. This includes data
   * visualization components, interactive tools.
   *
   * @param cardsBox
   *   The CardsBox instance that holds individual data cards.
   * @param graphBox
   *   The GridPane that serves as the container for the chart or graph
   *   visualization.
   * @param mainInterface
   *   The GridPane that holds the main user interface components including
   *   tools and data visualizations.
   * @param mainScene
   *   The main Scene where the UI components are displayed.
   * @param chartBox
   *   An optional ChartBox instance that if not provided will be instantiated
   *   within this method.
   */
  private def finalizeInterface(
                                 cardsBox: CardsBox,
                                 graphBox: GridPane,
                                 mainInterface: GridPane,
                                 mainScene: Scene,
                                 chartBox: Option[ChartBox] = None
                               ): Unit =

    /** Configures and returns a color picker and a button for changing the
     * color of selected cards.
     *
     * @return
     *   A tuple containing the ColorPicker and the Button configured for
     *   changing card colors.
     */
    def setupColorPicker(): (ColorPicker, Button) =
      val picker = new ColorPicker(Color.color(1, 0.718, 0.808))

      // Button that opens the ColorPicker
      val colorButton = new Button("Change Card Color") {
        onMouseClicked = _ =>
          val newColor = picker.value.value
          cardsBox.getChosenCards.foreach( { card =>
            card.changeColor(newColor)
            card.toggleClicked(false)
          } )
          cardsBox.getChosenCards.clear()
          cardsBox.refreshCardsDisplay()

      }

      (picker, colorButton)

    val summaryBox = new SummaryBox(cardsBox.getAllCards)

    val newChartBox = new ChartBox

    val (picker, colorButton) = setupColorPicker()

    val toolBar = new ToolBar {
      items.addAll(
        new CardTools(cardsBox, summaryBox),
        new ChartTools(chartBox.getOrElse(newChartBox), cardsBox),
        picker,
        colorButton,
        new Button("Import new data") {
          onMouseClicked = _ => stage.scene = createInitialScene()
        }
      )
    }

    // Binds the preferred width of cardsBox to the width of mainInterface, ensuring cardsBox resizes with mainInterface.
    cardsBox.prefWidthProperty().bind(mainInterface.widthProperty())
    // Similar to the previous line, binds the preferred height of cardsBox to the height of mainInterface.
    cardsBox.prefHeightProperty().bind(mainInterface.heightProperty())

    // Adds toolBar to the mainInterface grid, on top of the mainInterface, specifying its grid position and span.
    mainInterface.add(toolBar, 0, 0, 1, 1)
    // Adds the cardsBox component to the mainInterface grid, specifying its position and span within the grid.
    mainInterface.add(cardsBox, 0, 2, 1, 1)

    // Adds chartBox to graphBox, specifying its position and span within the grid layout.
    graphBox.add(chartBox.getOrElse(newChartBox), 1, 0, 1, 2)
    // Adds SummaryBox next to chartBox in graphBox, specifying its grid position and span.
    graphBox.add(summaryBox, 0, 0, 1, 2)

    // Sets the scene of the application stage to mainScene, applying all the UI changes made.
    stage.scene = mainScene
    // Maximizes the application window to fill the screen.
    stage.setMaximized(true)
