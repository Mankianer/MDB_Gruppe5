package de.mankianer.gruppe5;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import de.mankianer.gruppe5.util.CountMap;
import de.mankianer.gruppe5.util.MyTelegramBot;
import de.mankianer.gruppe5.util.ReduceCounter;
import de.mankianer.gruppe5.util.ReduceText;
import de.mankianer.gruppe5.util.TelegramFunction;
import de.mankianer.gruppe5.util.TweetMapToId;


public class TelegramBotStarter {
	public static void main(String ...args) throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		//TOKEN: 201091374:AAEy8-w2aXNyoMaN23tSFqddBPPt5t0YT1A
		
		Properties propertiesIn = new Properties();
		propertiesIn.setProperty("bootstrap.servers", "localhost:9092");
		propertiesIn.setProperty("zookeeper", "localhost:2181");

		//propertiesIn.setProperty("group.id", "value");
		
		DataStream<String> inputStream = 
				env.addSource(new FlinkKafkaConsumer09<>("tweets2", new SimpleStringSchema(), propertiesIn));
		
		if(inputStream == null) {
			System.err.println("Stream fehler");
			return;
		}
//		MyTelegramBot.init();
		
//		inputStream.map(new CountMap()).timeWindowAll(Time.seconds(10)).reduce(new ReduceText());
		
		// apply the async I/O transformation
//		DataStream<Tuple2<String, String>> resultStream =
//		    AsyncDataStream.unorderedWait(inputStream, new TelegramFunction(), 20000, TimeUnit.MILLISECONDS, 100);
		
		
//		MyTelegramBot myTelegramBot = new MyTelegramBot();
//		myTelegramBot.init();
		
//		inputStream.writeToSocket("localhost", 8080, new SimpleStringSchema());
		
//		inputStream.addSink(myTelegramBot);
		
//		DataStream<Tuple2<String, Integer>> outputStream = 
//				inputStream.map(new CountMap()).keyBy(1).window(TumblingProcessingTimeWindows.of(Time.seconds(3))).reduce(new ReduceCounter());
		
//		DataStreamSink<String> outStreamTopic = inputStream.addSink(new FlinkKafkaProducer09<String>("testOut", new SimpleStringSchema(), propertiesIn));
		
		
		//resultStream.print();
		
		
		env.execute("Telegram Sender Starten");
		
		
		
	}

}
