package de.mankianer.gruppe5.analyse.chartypecount;

import org.apache.flink.api.common.functions.MapFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.CharCountAnalyse;

public class CharCountAnalyseToTweet implements MapFunction<CharCountAnalyse, Tweet>{
	
	@Override
	public Tweet map(CharCountAnalyse ca) throws Exception {

		Tweet t = new Tweet();
		t.setId(ca.getTweetID());
		t.addAnalyse(ca);
		return t;
	}
}
