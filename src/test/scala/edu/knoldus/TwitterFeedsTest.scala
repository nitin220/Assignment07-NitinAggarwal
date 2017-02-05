package edu.knoldus

import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest.FunSuite

class TwitterFeedsTest extends FunSuite {

  val check = new TwitterFeeds

  test("Testing getTweets function") {
    assert(Await.result(check.getTweets, Duration.Inf) != List())
  }

  test("Testing getNumberOfTweets function") {
    assert(Await.result(check.getNumberOfTweets, Duration.Inf) == 100)
  }

  test("Testing getAverageTweetsPerDay function") {
    assert(Await.result(check.getAverageTweetsPerDay, Duration.Inf) == 0.2739726f)
  }

  test("Testing getAverageLike function") {
    assert(Await.result(check.getAverageLike, Duration.Inf) == 0.47f)
  }

  test("Testing getAverageRetweets function") {
    assert(Await.result(check.getAverageRetweets, Duration.Inf) == 2.6f)
  }

}
