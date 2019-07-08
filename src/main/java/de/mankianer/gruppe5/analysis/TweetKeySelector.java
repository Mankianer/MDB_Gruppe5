package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import org.apache.flink.api.java.functions.KeySelector;

/**
 * Selektor für Tweets nach ID
 * 
 * @author Marvin Wölk
 *
 */
public class TweetKeySelector implements KeySelector<Tweet, Long> {

  @Override
  public Long getKey(Tweet tweet) throws Exception {
    return tweet.getId();
  }
}
