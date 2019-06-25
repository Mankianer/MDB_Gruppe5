package de.mankianer.gruppe5.util;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.concurrent.Future;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.async.collector.AsyncCollector;

public class TelegramFunction extends RichAsyncFunction<String, Tuple2<String, String>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The database specific client that can issue concurrent requests with callbacks */
//    private transient DatabaseClient client;
	MyTelegramBot bot;

    @Override
    public void open(Configuration parameters) throws Exception {
    	bot = new MyTelegramBot();
    }

    @Override
    public void close() throws Exception {
//        client.close();
    }

	@Override
	public void asyncInvoke(String input, AsyncCollector<Tuple2<String, String>> collector) throws Exception {
		// issue the asynchronous request, receive a future for result
//        final Future<String> result = client.query(key);

        // set the callback to be executed once the request by the client is complete
        // the callback simply forwards the result to the result future
        CompletableFuture.supplyAsync(new Supplier<String>() {

            @Override
            public String get() {
            	bot.send(input);
                return "Telegram Success";
            }
        }).thenAccept( (String result) -> {
        	collector.collect(Collections.singleton(new Tuple2<>(input, result)));
        });
	}
}