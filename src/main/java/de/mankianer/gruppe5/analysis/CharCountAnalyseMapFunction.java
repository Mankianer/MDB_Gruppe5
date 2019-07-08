package de.mankianer.gruppe5.analysis;

import java.util.HashMap;

import org.apache.flink.api.common.functions.MapFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.CharCountAnalyse;
import de.mankianer.gruppe5.model.analyse.WordCountAnalyse;

/**
 * Funktion zum Zählen von Buchstarben
 * 
 * Aufgrund von Parsing problemen für sonderzeichen nur A-z
 * 
 * @author Marvin Wölk
 *
 */
public class CharCountAnalyseMapFunction implements MapFunction<Tweet, Analyse>{

	@Override
	public Analyse map(Tweet value) throws Exception {
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for (char c : value.getText().toCharArray()) {
			if(c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
				counter.put("" + c, (counter.containsKey("" + c)) ? counter.get("" + c) + 1 : 1);
			}else {
				counter.put("sonst.", (counter.containsKey("sonst.")) ? counter.get("sonst.") + 1 : 1);
			}
		}
		
		
		return value.addAnalyse(new CharCountAnalyse(counter));
	}

}
