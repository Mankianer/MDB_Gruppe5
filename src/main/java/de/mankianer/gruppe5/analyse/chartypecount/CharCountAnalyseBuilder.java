package de.mankianer.gruppe5.analyse.chartypecount;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.time.Time;

import de.mankianer.gruppe5.analysis.AnalyseToTweetMap;
import de.mankianer.gruppe5.analysis.TweetKeySelector;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.CharCountAnalyse;

public class CharCountAnalyseBuilder {

	public static DataStream<Tweet> build(DataStream<Tweet> input, Time windowSize) {

		return input.keyBy(new TweetKeySelector()).flatMap(new CharCounterAnalyseMapFunction()).keyBy(0).sum(1)
				.map(new MapFunction<Tuple3<Character, Integer, Long>, CharCountAnalyse>() {
					@Override
					public CharCountAnalyse map(Tuple3<Character, Integer, Long> t) throws Exception {
						return new CharCountAnalyse(t);
					}
				}).keyBy(ca -> ca.getTweetID()).reduce(CharCountAnalyse::reduce)
				.map(ca -> {
					Tweet t = new Tweet();
					t.setId(ca.getTweetID());
					t.addAnalyse(ca);
					return t;
				}).union(input).keyBy(new TweetKeySelector()).reduce(Tweet::reduce);
	}
}
