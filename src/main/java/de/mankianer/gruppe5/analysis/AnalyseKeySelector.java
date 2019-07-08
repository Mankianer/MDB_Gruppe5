package de.mankianer.gruppe5.analysis;

import de.mankianer.gruppe5.model.analyse.Analyse;
import org.apache.flink.api.java.functions.KeySelector;

/**
 * keySelector für Analysen
 * 
 * @author Marvin Wölk
 *
 */
public class AnalyseKeySelector implements KeySelector<Analyse, Long> {

  @Override
  public Long getKey(Analyse analyse) throws Exception {
    return analyse.getTweetID();
  }
}
