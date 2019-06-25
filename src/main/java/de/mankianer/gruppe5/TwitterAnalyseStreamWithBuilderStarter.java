package de.mankianer.gruppe5;

import de.mankianer.gruppe5.analysis.AnalyseKeySelector;
import de.mankianer.gruppe5.analysis.AnalyseToTweetMap;
import de.mankianer.gruppe5.analysis.LengthAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.TweetKeySelector;
import de.mankianer.gruppe5.analysis.WordCountAnalyseMapFunction;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.util.AnalyseStreamBuilder;
import de.mankianer.gruppe5.util.TweetSerializationSchema;
import java.util.Properties;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

public class TwitterAnalyseStreamWithBuilderStarter {
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

    AnalyseStreamBuilder analyseStreamBuilder = new AnalyseStreamBuilder(inputStream);

    analyseStreamBuilder.addAnalyse(1,new LengthAnalyseMapFunction(), new WordCountAnalyseMapFunction());

    analyseStreamBuilder.build().addSink(new FlinkKafkaProducer09<Tweet>("elastic", new TweetSerializationSchema(), propertiesIn));

    env.execute("Tweet verarbeitung Starten");



  }
}
