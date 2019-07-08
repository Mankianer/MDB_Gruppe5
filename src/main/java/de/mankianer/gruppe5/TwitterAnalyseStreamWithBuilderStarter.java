package de.mankianer.gruppe5;

import java.util.Map;
import java.util.Properties;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import de.mankianer.gruppe5.analysis.AnalyseToTweetMap;
import de.mankianer.gruppe5.analysis.CharCountAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.ClassificationMapAnalyse;
import de.mankianer.gruppe5.analysis.EntityMapFunction;
import de.mankianer.gruppe5.analysis.FleschAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.LengthAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.NameMapFunction;
import de.mankianer.gruppe5.analysis.SentimentAnalyseMapFunction;
import de.mankianer.gruppe5.analysis.WordCountAnalyseMapFunction;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.util.AnalyseStreamBuilder;
import de.mankianer.gruppe5.util.AnalysenSerializationSchema;
import de.mankianer.gruppe5.util.TelegramFunction;
import de.mankianer.gruppe5.util.TweetSerializationSchema;
import de.mankianer.gruppe5.util.TweetToAnalyseFlatMapFunction;

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

    AnalyseStreamBuilder analyseStreamBuilder = AnalyseStreamBuilder.getOfStringStream(inputStream);
    
//    analyseStreamBuilder.setTurnTime(1, Time.seconds(10));
    analyseStreamBuilder.addAnalyse(1,new LengthAnalyseMapFunction(), new WordCountAnalyseMapFunction(), new CharCountAnalyseMapFunction(), new SentimentAnalyseMapFunction());
    analyseStreamBuilder.addAnalyse(1,new SentimentAnalyseMapFunction(), new ClassificationMapAnalyse(), new NameMapFunction(), new FleschAnalyseMapFunction(), new EntityMapFunction());
//    	analyseStreamBuilder.addAnalyse(1, new LengthAnalyseMapFunction());
//    analyseStreamBuilder.addAnalyse(2,);
    
//    analyseStreamBuilder = new AnalyseStreamBuilder(CharCountAnalyseBuilder.build(analyseStreamBuilder.build(), Time.seconds(5)));
    

    DataStream<Tweet> build = analyseStreamBuilder.build();
    build.addSink(new FlinkKafkaProducer09<Tweet>("elastic2", new TweetSerializationSchema(), propertiesIn));
    build.flatMap(new TweetToAnalyseFlatMapFunction()).addSink(new FlinkKafkaProducer09<Map<String,Analyse>>("analysen3", new AnalysenSerializationSchema(), propertiesIn));
	build.addSink(new TelegramFunction());

    env.execute("Tweet verarbeitung Starten");



  }
}
