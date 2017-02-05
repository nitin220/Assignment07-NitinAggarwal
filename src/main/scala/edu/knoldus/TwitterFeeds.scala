package edu.knoldus

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Query, Twitter, TwitterFactory}
import scala.collection.JavaConverters._
import twitter4j.TwitterException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterFeeds {

  /**
    * This function returns the total number of tweets as Future[Int]
    */
  def getNumberOfTweets: Future[Int] = {
    val result = getTweets
    val sizeFuture = result map (_.size)
    sizeFuture
  }

  /**
    * This function returns the number of tweets per day as Future[Float]
    */
  def getAverageTweetsPerDay: Future[Float] = {
    val tweets = getTweets
    val averageFuture = tweets.map(_.size / 365.toFloat)
    averageFuture
  }

  /**
    * This function returns the average number of likes per tweet as Future[Int]
    */
  def getAverageLike: Future[Float] = {
    val tweets = getTweets
    val listOfLikes = tweets.map(_.map(element => element.like))
    val totalLikes = listOfLikes.map(_.foldLeft(0)(_ + _))
    val totalTweets = getNumberOfTweets
    val aversgeLikes = totalTweets.map(x => divideFuture(x, totalLikes))
    val aversgeLike = aversgeLikes.flatMap(_.map(x => x))
    aversgeLike
  }

  /**
    * This function returns the average number of retweets per tweet as Future[Int]
    */
  def getAverageRetweets: Future[Float] = {
    val tweets = getTweets
    val listOfRetweets = tweets.map(_.map(element => element.retweet))
    val totalRetweets = listOfRetweets.map(_.foldLeft(0)(_ + _))
    val totalTweets = getNumberOfTweets
    val aversgeRetweets = totalTweets.map(x => divideFuture(x, totalRetweets))
    val aversgeRetweet = aversgeRetweets.flatMap(_.map(x => x))
    aversgeRetweet
  }

  /**
    * This function will calculate the average and return Future[Int]
    */
  def divideFuture(element: Int, futureOfInt: Future[Int]): Future[Float] = {
    futureOfInt.map(_ / element.toFloat)
  }

  /**
    * This function returns List of tweets, according to provided hashtag as Future[List[Mytweets]]
    * where as MyTweets is a case class
    */
  def getTweets: Future[List[MyTweets]] = Future {
    try {
      val consumerKey = ???
      val consumerSecretKey = ???
      val accessToken = ???
      val accessTokenSecret = ???
      val configurationBuilder = new ConfigurationBuilder
      configurationBuilder.setDebugEnabled(false)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecretKey)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret)
      val twitter: Twitter = new TwitterFactory(configurationBuilder.build).getInstance
      val query = new Query("#scala")
      val maxTweet = 100
      query.setCount(maxTweet)
      query.setSince("2017-01-24")
      val list = twitter.search(query)
      val tweets = list.getTweets.asScala.toList
      val allTweets = tweets.map {
        tweet =>
          MyTweets(tweet.getText, tweet.getUser.getScreenName, tweet.getCreatedAt, tweet.getFavoriteCount, tweet.getRetweetCount)
      }
      allTweets.sortBy(_.date)
    }
    catch {
      case exception: TwitterException => List()
    }

  }

}
