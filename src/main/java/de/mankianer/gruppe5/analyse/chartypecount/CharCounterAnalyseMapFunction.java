package de.mankianer.gruppe5.analyse.chartypecount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.WordCountAnalyse;

public class CharCounterAnalyseMapFunction implements FlatMapFunction<Tweet, Tuple3<Character, Integer, Long>>{


	@Override
	public void flatMap(Tweet value, Collector<Tuple3<Character, Integer, Long>> out) throws Exception {
		if(value.getText() != null) {
			for (char c : value.getText().toCharArray()) {
				out.collect(new Tuple3<Character, Integer, Long>(c,1, value.getId()));
			}
		}
	}

	
}
