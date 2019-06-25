package de.mankianer.gruppe5.analysis;

import org.apache.flink.api.common.functions.MapFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.LengthAnalyse;

public class LengthAnalyseMapFunction implements MapFunction<Tweet, Tweet>
{
	@Override
	public Tweet map(Tweet value) throws Exception {
		return value.addAnalyse(new LengthAnalyse(value.getText().length()));
	}

}
