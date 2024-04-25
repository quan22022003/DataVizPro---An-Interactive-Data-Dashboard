package scala

import DashboardHandlers.DashboardImporter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.scene.paint.Color

class FileDataParserSpec extends AnyFlatSpec with Matchers {

  "parseColor" should "correctly parse RGB strings into Color objects" in {
    val colorString = "0.5,0.5,0.5"
    val expectedColor = Color(0.5, 0.5, 0.5, 1.0) // Opacity is always 1.0

    DashboardImporter.parseColor(colorString) shouldEqual expectedColor
  }

  "ImportDashboardUtils" should "extract correct information from valid data file" in {
    val (color, graphType, headers, data) = DashboardImporter.parseFile("src/tests/resources/test.quan")

    color shouldEqual Color.color(1.0, 0.718, 0.808)
    graphType shouldEqual "3"
    headers shouldBe Seq("Feb-2015", " Mar-2015")
    data should contain theSameElementsAs Array(1.0, 3.5)
  }


  it should "handle completely empty files" in {
    // Wrap the operation in a try-catch block to verify it handles the error as expected
    val thrown = intercept[IllegalArgumentException] {
      DashboardImporter.parseFile("src/tests/resources/emptyFile.quan")
    }

    // Assuming the error message for an empty file starts with "File is empty"
    // This checks that the appropriate exception is thrown for completely empty files
    thrown.getMessage should startWith ("File is empty")
  }
}
