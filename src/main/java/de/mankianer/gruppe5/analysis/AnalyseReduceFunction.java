package de.mankianer.gruppe5.analysis;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;

public class AnalyseReduceFunction implements MapFunction<Analyse, Tweet> {

	@Override
	public Tweet map(Analyse value) throws Exception {
		return null;
	}


}
