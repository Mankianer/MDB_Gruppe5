package de.mankianer.gruppe5.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.google.gson.Gson;

import de.mankianer.gruppe5.model.Tweet;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import scala.collection.parallel.ParIterableLike.FlatMap;

public class StringToTweetFlatMapFunction implements FlatMapFunction<String, Tweet> {

	private static final long serialVersionUID = 1L;

	@Override
	public void flatMap(String value, Collector<Tweet> out) throws Exception {
		Gson gson = new Gson();
		try {
			Tweet t = gson.fromJson(value, Tweet.class);
			t.setRomantic_date(LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
			out.collect(t);
		}catch (Exception e) {
		}
	}

}