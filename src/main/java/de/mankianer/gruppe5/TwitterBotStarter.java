package de.mankianer.gruppe5;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;


public class TwitterBotStarter {
	public static void main(String ...args) throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		//TOKEN: 201091374:AAEy8-w2aXNyoMaN23tSFqddBPPt5t0YT1A
		
		Properties propertiesIn = new Properties();
		propertiesIn.setProperty("bootstrap.servers", "localhost:9092");
		propertiesIn.setProperty("zookeeper", "localhost:2181");

		//propertiesIn.setProperty("group.id", "value");
		
		DataStream<String> inputStream = 
				env.addSource(new FlinkKafkaConsumer09<>("TwitterBot", new SimpleStringSchema(), propertiesIn));
		
		if(inputStream == null) {
			System.err.println("Stream fehler");
			return;
		}
		
		// apply the async I/O transformation
//		DataStream<Tuple2<String, String>> resultStream =
//		    AsyncDataStream.unorderedWait(inputStream, new TwitterFunction(), 1000, TimeUnit.MILLISECONDS, 100);
		
		
		
//		MyTelegramBot myTelegramBot = new MyTelegramBot();
//		myTelegramBot.init();
		
//		inputStream.writeToSocket("localhost", 8080, new SimpleStringSchema());
		
//		inputStream.addSink(myTelegramBot);
		
//		DataStream<Tuple2<String, Integer>> outputStream = 
//				inputStream.map(new CountMap()).keyBy(1).window(TumblingProcessingTimeWindows.of(Time.seconds(3))).reduce(new ReduceCounter());
		
//		DataStreamSink<String> outStreamTopic = inputStream.addSink(new FlinkKafkaProducer09<String>("testOut", new SimpleStringSchema(), propertiesIn));
		
		
//		resultStream.print();
		
		
		env.execute("Twitter Sender Starten");
		
		
		
	}
}
