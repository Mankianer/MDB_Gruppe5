package de.mankianer.gruppe5.util;

import de.mankianer.gruppe5.analysis.AnalyseToTweetMap;
import de.mankianer.gruppe5.analysis.LengthAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.TweetKeySelector;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;

public class AnalyseStreamBuilder {

	private DataStream<Tweet> inPutStream;
	private TreeMap<Integer, LinkedList<FlatMapFunction<Tweet, Analyse>>> analysenTurnMap;

	@Getter
	@Setter
	private Time defaultTime;

	private HashMap<Integer, Time> timeTurnMap;

	public static AnalyseStreamBuilder getOfStringStream(DataStream<String> inPutStream) {
		return new AnalyseStreamBuilder(inPutStream.flatMap(new StringToTweetFlatMapFunction()));
	}

	public AnalyseStreamBuilder(DataStream<Tweet> inPutStream) {
		this.inPutStream = inPutStream;
		analysenTurnMap = new TreeMap<>();
		timeTurnMap = new HashMap<>();
		defaultTime = Time.seconds(10);
	}

	public DataStream<Tweet> build() {
		DataStream<Tweet>[] tweetStream = new DataStream[] { inPutStream };

		analysenTurnMap.entrySet().forEach(e -> {
			DataStream<Tweet> analysenAsTweetStream = addAnalysenToStream(tweetStream[0],
					e.getValue().toArray(new MapFunction[0])).map(new AnalyseToTweetMap());

			tweetStream[0] = analysenAsTweetStream.union(inPutStream).keyBy(new TweetKeySelector())
					.timeWindow(timeTurnMap.getOrDefault(e.getKey(), defaultTime)).reduce(Tweet::reduce);
		});
		
		
		return tweetStream[0];
	}

	private DataStream<Analyse> addAnalysenToStream(DataStream<Tweet> in, MapFunction<Tweet, Analyse>... analysen) {
		if (analysen.length > 0) {

			DataStream<Analyse> ret = in.map(analysen[0]);
			for (int i = 1; i < analysen.length; i++) {
				ret = ret.union(in.map(analysen[i]));
			}
//			ret.addSink(new FlinkKafkaProducer09<Analysen>("analysen", new AnalysenSerializationSchema(), propertiesIn));
			return ret;
		}
		return in.map(new LengthAnalyseMapFunction());
	}

	public AnalyseStreamBuilder setTurnTime(int turn, Time time) {
		timeTurnMap.put(turn, time);

		return this;
	}

	public AnalyseStreamBuilder addAnalyse(int turn, FlatMapFunction<Tweet, Analyse>... analysen) {
		for (FlatMapFunction<Tweet, Analyse> analyse : analysen) {

			if (!analysenTurnMap.containsKey(turn)) {
				analysenTurnMap.put(turn, new LinkedList<>());
			}

			analysenTurnMap.get(turn).add(analyse);
		}

		return this;
	}
}
