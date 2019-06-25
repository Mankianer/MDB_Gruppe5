package de.mankianer.gruppe5;

import java.util.Properties;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import de.mankianer.gruppe5.analysis.AnalyseReduceFunction;
import de.mankianer.gruppe5.analysis.LengthAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.WordCountAnalyseMapFunction;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.WordCountAnalyse;
import de.mankianer.gruppe5.util.StringToTweetFlatMapFunction;
import de.mankianer.gruppe5.util.TweetSerializationSchema;


public class TwitterAnalyseStreamStarter {
	public static void main(String ...args) throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		//TOKEN: 201091374:AAEy8-w2aXNyoMaN23tSFqddBPPt5t0YT1A
		
		Properties propertiesIn = new Properties();
		propertiesIn.setProperty("bootstrap.servers", "localhost:9092");
		propertiesIn.setProperty("zookeeper", "localhost:2181");

		//propertiesIn.setProperty("group.id", "value");
		
		DataStream<String> inputStream = 
				env.addSource(new FlinkKafkaConsumer09<>("tweets3", new SimpleStringSchema(), propertiesIn));
		
		if(inputStream == null) {
			System.err.println("Stream fehler");
			return;
		}
//		MyTelegramBot.init();
		
//		inputStream.map(new CountMap()).timeWindowAll(Time.seconds(10)).reduce(new ReduceText());
		
		// apply the async I/O transformation
		DataStream<Tweet> tweetInStream = inputStream.flatMap(new StringToTweetFlatMapFunction());
		tweetInStream.print();
		
		SingleOutputStreamOperator<Analyse> lengthAnalyseStream = tweetInStream.map(new LengthAnalyseMapFunction());
		SingleOutputStreamOperator<Analyse> wordCountAnalyseStream = tweetInStream.map(new WordCountAnalyseMapFunction());
		
		DataStream<Analyse> analyseStream = lengthAnalyseStream.union(wordCountAnalyseStream);
		
		
		analyseStream.keyBy("tweetID");
		
		tweetInStream.addSink(new FlinkKafkaProducer09<Tweet>("elastic", new TweetSerializationSchema(), propertiesIn));
		
		
//		MyTelegramBot myTelegramBot = new MyTelegramBot();
//		myTelegramBot.init();
		
//		inputStream.writeToSocket("localhost", 8080, new SimpleStringSchema());
		
//		inputStream.addSink(myTelegramBot);
		
//		DataStream<Tuple2<String, Integer>> outputStream = 
//				inputStream.map(new CountMap()).keyBy(1).window(TumblingProcessingTimeWindows.of(Time.seconds(3))).reduce(new ReduceCounter());
		
//		DataStreamSink<String> outStreamTopic = inputStream.addSink(new FlinkKafkaProducer09<String>("testOut", new SimpleStringSchema(), propertiesIn));
		
		
		//resultStream.print();
		
		
		env.execute("Tweet verarbeitung Starten");
		
		
		
	}
	
}
