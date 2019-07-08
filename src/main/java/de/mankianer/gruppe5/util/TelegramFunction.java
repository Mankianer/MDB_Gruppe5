package de.mankianer.gruppe5.util;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import de.mankianer.gruppe5.model.Tweet;

public class TelegramFunction implements SinkFunction<Tweet>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void invoke(Tweet value) throws Exception {
		MyTelegramBot.send(value);
	}
}