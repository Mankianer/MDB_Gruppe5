package de.mankianer.gruppe5.analysis;

import org.apache.flink.api.common.functions.MapFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.WordCountAnalyse;

public class WordCountAnalyseMapFunction implements MapFunction<Tweet, Analyse>{

	@Override
	public Analyse map(Tweet value) throws Exception {
		return value.addAnalyse(new WordCountAnalyse("" + value.getText().split("\\b[^\\s]+\\b").length));
	}

}
