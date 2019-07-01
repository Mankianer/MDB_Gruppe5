package de.mankianer.gruppe5.util;

import de.mankianer.gruppe5.analysis.AnalyseToTweetMap;
import de.mankianer.gruppe5.analysis.LengthAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.TweetKeySelector;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

public class AnalyseStreamBuilder {

    private DataStream<String> inPutStream;
    private TreeMap<Integer, LinkedList<MapFunction<Tweet, Analyse>>> analysenTurnMap;

    @Getter
    @Setter
    private Time defaultTime;

    private HashMap<Integer, Time> timeTurnMap;

    public AnalyseStreamBuilder(DataStream<String> inPutStream) {
        this.inPutStream = inPutStream;
        analysenTurnMap = new TreeMap<>();
        timeTurnMap = new HashMap<>();
        defaultTime = Time.seconds(15);
    }

    public DataStream<Tweet> build() {
        //String in Tweets umwandeln
        DataStream<Tweet>[] tweetStream = new DataStream[]{
                inPutStream.flatMap(new StringToTweetFlatMapFunction())};

        analysenTurnMap.entrySet().forEach(e -> {
            DataStream<Tweet> analysenAsTweetStream = addAnalysenToStream(tweetStream[0],
                    e.getValue().toArray(new MapFunction[0])).map(new AnalyseToTweetMap());

            tweetStream[0] = analysenAsTweetStream.keyBy(new TweetKeySelector())
                    .timeWindow(timeTurnMap.getOrDefault(e.getKey(), defaultTime)).reduce(Tweet::reduce);
        });

        return tweetStream[0];
    }

    private DataStream<Analyse> addAnalysenToStream(DataStream<Tweet> in,
                                                    MapFunction<Tweet, Analyse>... analysen) {
        if (analysen.length > 0) {

            DataStream<Analyse> ret = in.map(analysen[0]);
            for (int i = 1; i < analysen.length; i++) {
                ret = ret.union(in.map(analysen[i]));
            }
            return ret;
        }
        return in.map(new LengthAnalyseMapFunction());
    }

    public AnalyseStreamBuilder setTurnTime(int turn, Time time) {
        timeTurnMap.put(turn, time);

        return this;
    }

    public AnalyseStreamBuilder addAnalyse(int turn, MapFunction<Tweet, Analyse>... analysen) {
        for (MapFunction<Tweet, Analyse> analyse : analysen
                ) {

            if (!analysenTurnMap.containsKey(turn)) {
                analysenTurnMap.put(turn, new LinkedList<>());
            }

            analysenTurnMap.get(turn).add(analyse);
        }

        return this;
    }
}
