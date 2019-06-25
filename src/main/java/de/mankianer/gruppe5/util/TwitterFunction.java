package de.mankianer.gruppe5.util;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.async.collector.AsyncCollector;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFunction extends RichAsyncFunction<String, Tuple2<String, String>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Twitter twitter;

	@Override
    public void open(Configuration parameters) throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("VUfux5286WRnlxLwsSkUBAJDU")
		  .setOAuthConsumerSecret("ik8SYfA45vDsZZxMPrwUGgVCU2D4DEBLZviYuSgJ2l2hjnvESO")
		  .setOAuthAccessToken("1130903181578440704-rzvxqjzBEfPanpOtrLcXHcBkSGyXbF")
		  .setOAuthAccessTokenSecret("Ll2aO2JO8bfZUin88CSUqxkOy6Iiw9dozV8gFwVEx6BaT");
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
    }
	
	@Override
	public void asyncInvoke(String input, AsyncCollector<Tuple2<String, String>> collector) throws Exception {
		CompletableFuture.supplyAsync(new Supplier<String>() {

            @Override
            public String get() {
            	try {
					twitter.updateStatus(input);
				} catch (TwitterException e) {
					e.printStackTrace();
					return "Twitter Error - " + e.getErrorMessage();
				}
                return "Twitter Success";
            }
        }).thenAccept( (String result) -> {
        	collector.collect(Collections.singleton(new Tuple2<>(input, result)));
        });
	}

}
