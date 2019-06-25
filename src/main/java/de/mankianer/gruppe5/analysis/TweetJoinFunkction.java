package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import org.apache.flink.api.common.functions.JoinFunction;

public class TweetJoinFunkction implements JoinFunction<Tweet, Tweet, Tweet> {

  @Override
  public Tweet join(Tweet tweet, Tweet tweet2) throws Exception {
    return tweet.reduce(tweet2);
  }
}
