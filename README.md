# DataViz Pro

DataViz Pro is an interactive data visualization application designed to assist users in representing data and deriving insights through a user-friendly interface. It allows for the visualization of data using various chart types and provides tools for aesthetic customization.

## Features

- **Versatile Data Input**: Supports importing data from CSV files, URLs, and direct keyboard input.
- **Interactive Dashboards**: Create new or open existing dashboards with support for custom `.quan` file format.
- **Error Handling**: Intelligent error warnings for data import issues, ensuring data integrity.
- **Advanced Data Visualization**: Includes cards for key data points and supports scatter plots, column charts, pie charts, and line charts.
- **Aesthetic Customization**: Offers tools to customize the aesthetics of the data visualization to suit user preferences.

## Getting Started

### Prerequisites

Ensure you have the following installed:
- Java SDK (version 8 or above)
- Scala Build Tool (SBT)

### Installation

Clone the repository to your local machine:

```bash
git clone https://version.aalto.fi/gitlab/tranq8/data-dashboard.git
```

Navigate to the project directory:

```bash
cd data-dashboard
```

Run the application using SBT:

```bash
sbt run
```

## Usage

Upon launching DataViz Pro, users are presented with options to either create a new dashboard or open an existing one.

### Creating a New Dashboard

Users can import data from:
- A CSV file through a file browser window.
- A URL by entering the data source's web address in a dialog window.

### Opening an Existing Dashboard

The application supports opening previously saved dashboards from `.quan` files.

### Navigation and Tools

- The application provides a seamless way to navigate through different functionalities, offering tools for data manipulation and visualization.
- Users can customize the appearance of data cards and charts within the dashboard.

## Program Structure

DataViz Pro's architecture is divided into five principal components for ease of maintenance and scalability:
- **File Handlers**: For importing and exporting data.
- **Data Display**: Managing the visualization of data points.
- **Charts Display**: Handling various types of statistical charts.
- **Tools**: Interactive functionalities for dashboard customization.
- **User Prompt**: Input dialogs and error alerts.

## Testing

The application was rigorously tested with various CSV files to ensure robust error handling and functionality. Non-GUI components were unit tested to validate their operations against expected results.

## Known Issues and Limitations

As of the current release, there are no known bugs. However, limitations include:
- The pie chart output does not start from the top, and slices are not ordered.
- Support for a limited set of CSV templates.
- Imported dashboards are immutable.

## Final Evaluation

DataViz Pro is aimed at making data analysis accessible while ensuring the visual appeal of data representation. It stands out for its intuitive interface and the flexibility it offers in data visualization.

## References

Special thanks to Hung Nguyen for the inspiration and reference provided from his Covid-19 Dashboard project. For more information and examples, visit [Hungreeee/Covid-19-Dashboard](https://github.com/Hungreeee/Covid-19-Dashboard).

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
