package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import org.apache.flink.api.java.functions.KeySelector;

public class TweetKeySelector implements KeySelector<Tweet, Integer> {

  @Override
  public Integer getKey(Tweet tweet) throws Exception {
    return tweet.getId();
  }
}
