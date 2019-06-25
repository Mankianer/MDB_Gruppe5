package de.mankianer.gruppe5;

import java.util.Properties;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import de.mankianer.gruppe5.util.CountMap;
import de.mankianer.gruppe5.util.ReduceCounter;


public class Praktikum2 {
	public static void main(String ...args) throws Exception{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		Properties propertiesIn = new Properties();
		propertiesIn.setProperty("bootstrap.servers", "localhost:9092");
		propertiesIn.setProperty("zookeeper", "localhost:2181");

		//propertiesIn.setProperty("group.id", "value");
		
		DataStream<String> inputStream = 
				env.addSource(new FlinkKafkaConsumer09<>("test", new SimpleStringSchema(), propertiesIn));
		
		if(inputStream == null) {
			System.err.println("Stream fehler");
			return;
		}
		
		DataStream<Tuple2<String, Integer>> outputStream = 
				inputStream.map(new CountMap()).keyBy(1).window(TumblingProcessingTimeWindows.of(Time.seconds(3))).reduce(new ReduceCounter());
		
		DataStreamSink<String> outStreamTopic = inputStream.addSink(new FlinkKafkaProducer09<String>("testOut", new SimpleStringSchema(), propertiesIn));
		
		
		outputStream.print();
		
		
		env.execute("Tupel lesen");
		
		
		
	}
}
