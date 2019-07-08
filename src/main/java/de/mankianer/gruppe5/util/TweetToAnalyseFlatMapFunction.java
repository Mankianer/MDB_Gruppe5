package de.mankianer.gruppe5.util;

import java.util.Map;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;

public class TweetToAnalyseFlatMapFunction implements FlatMapFunction<Tweet, Map<String,Analyse>>{

	@Override
	public void flatMap(Tweet value, Collector<Map<String,Analyse>> out) throws Exception {
		if(!value.getAnalysen().isEmpty()) {
			out.collect(value.getAnalysen());
		}
	}

}
