package scala
import Boxes.SummaryBox
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.Platform

import scala.collection.mutable

class SummaryBoxSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {
  override def beforeAll(): Unit = {
    // Initialize JavaFX environment
    Platform.startup(() => {})
  }

  val tolerance: Float = 0.0001f

  "SummaryBox" should "calculate correct statistics for given cards" in {
    val cards: mutable.Buffer[Card] = mutable.Buffer(new Card("A", 1), new Card("B", 2), Card("C", 3))
    val summary = new SummaryBox(cards)

    summary.getSum should be (6)
    summary.getMin should be (cards(0))
    summary.getMax should be (cards(2))
    summary.getAverage should be (2)
    summary.getStandardDeviation should be (0.8164966f +- tolerance)
  }

  it should "handle empty array of cards" in {
    val cards = mutable.Buffer[Card]()
    val summary = new SummaryBox(cards)

    summary.getSum should be (0)
    summary.getMin should be (null)
    summary.getMax should be (null)
    summary.getAverage.isNaN should be (true)
    summary.getStandardDeviation.isNaN should be (true)
  }

  it should "handle single card" in {
    val cards = mutable.Buffer(new Card("A", 1))
    val summary = new SummaryBox(cards)

    summary.getSum should be (1)
    summary.getMin should be (cards(0))
    summary.getMax should be (cards(0))
    summary.getAverage should be (1)
    summary.getStandardDeviation should be (0)
  }
}
