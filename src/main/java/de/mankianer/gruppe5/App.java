package de.mankianer.gruppe5;

import java.util.Properties;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		Properties propertiesIn = new Properties();
		propertiesIn.setProperty("bootstrap.servers", "localhost:9092");
		// propertiesIn.setProperty("group.id", "value");

		DataStream<String> inputStream = env.socketTextStream("172.22.205.153", 8080);
//				.addSource(new FlinkKafkaConsumer09<>("test", new SimpleStringSchema(), propertiesIn));

		if (inputStream == null) {
			System.err.println("Stream fehler");
			return;
		}

//		DataStream<Tuple2<String, Integer>> outputStream = inputStream.map(new CountMap()).keyBy(0).sum(1);

		inputStream.print();

		env.execute("Tupel lesen");
	}
}
