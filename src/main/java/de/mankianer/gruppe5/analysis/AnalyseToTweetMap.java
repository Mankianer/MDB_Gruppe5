package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;

/**
 * MapFunktion um Analysen in Tweets zurück zuwandeln
 * @author Marvin Wölk
 *
 */
public class AnalyseToTweetMap implements MapFunction<Analyse, Tweet> {


  @Override
  public Tweet map(Analyse analyse) throws Exception {
    Tweet ret = new Tweet();
    ret.setId(analyse.getTweetID());
    ret.addAnalyse(analyse);
    return ret;
  }
}
