package de.mankianer.gruppe5.util;

import java.util.UUID;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class TweetMapToId implements MapFunction<String, Tuple2<String, String>>{

	@Override
	public Tuple2<String, String> map(String value) throws Exception {
		return new Tuple2<String, String>(value, "" + System.currentTimeMillis());
	}

}
