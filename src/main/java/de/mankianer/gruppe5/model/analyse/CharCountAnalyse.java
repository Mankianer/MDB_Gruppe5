package de.mankianer.gruppe5.model.analyse;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import lombok.Getter;

public class CharCountAnalyse extends Analyse{

	private Map<String, Integer> charCountMap;
	
	public CharCountAnalyse(Map<String, Integer> charCountMap) {
		super(1337);
		this.charCountMap = charCountMap;
	}
	
	public CharCountAnalyse(Tuple3<String, Integer, Long> result) {
		super(0);
		charCountMap = new HashMap<String, Integer>();
		charCountMap.put(result.f0, result.f1);
		setTweetID(result.f2);
	}
	
	public CharCountAnalyse reduce(CharCountAnalyse in){
		charCountMap.putAll(in.charCountMap);
		
		return this;
	}

}
