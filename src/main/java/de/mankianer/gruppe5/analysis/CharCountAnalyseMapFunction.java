package de.mankianer.gruppe5.analysis;

import java.util.HashMap;

import org.apache.flink.api.common.functions.MapFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.CharCountAnalyse;
import de.mankianer.gruppe5.model.analyse.WordCountAnalyse;

public class CharCountAnalyseMapFunction implements MapFunction<Tweet, Analyse>{

	@Override
	public Analyse map(Tweet value) throws Exception {
		HashMap<Character, Integer> counter = new HashMap<Character, Integer>();
		for (char c : value.getText().toCharArray()) {
			counter.put(c, (counter.containsKey(c)) ? counter.get(c) + 1 : 1);
		}
		
		
		return value.addAnalyse(new CharCountAnalyse(counter));
	}

}
