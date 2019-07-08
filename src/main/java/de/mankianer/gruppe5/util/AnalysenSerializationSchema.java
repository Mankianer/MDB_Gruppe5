package de.mankianer.gruppe5.util;

import java.util.Map;

import org.apache.flink.streaming.util.serialization.SerializationSchema;

import com.google.gson.Gson;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;

public class AnalysenSerializationSchema implements SerializationSchema<Map<String,Analyse>>{

	@Override
	public byte[] serialize(Map<String,Analyse> element) {
		
		return new Gson().toJson(element).getBytes();
	}

}
