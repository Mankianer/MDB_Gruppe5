package de.mankianer.gruppe5.util;

import org.apache.flink.streaming.util.serialization.SerializationSchema;

import com.google.gson.Gson;

import de.mankianer.gruppe5.model.Tweet;

public class TweetSerializationSchema implements SerializationSchema<Tweet>{

	@Override
	public byte[] serialize(Tweet element) {
		
		return new Gson().toJson(element).getBytes();
	}

}
