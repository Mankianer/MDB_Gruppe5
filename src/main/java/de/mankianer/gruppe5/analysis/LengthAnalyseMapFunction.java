package de.mankianer.gruppe5.analysis;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;

import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.LengthAnalyse;

/**
 * Analyse zur Länge des Textes
 * 
 * @author Marvin Wölk
 *
 */
public class LengthAnalyseMapFunction implements MapFunction<Tweet, Analyse>
{
	private static final long serialVersionUID = 1L;

//	@Override
//	public void flatMap(Tweet value, Collector<Analyse> out) throws Exception {
//		out.collect((value.addAnalyse(new LengthAnalyse(value.getText().length()))));
//	}

	@Override
	public Analyse map(Tweet value) throws Exception {
		// TODO Auto-generated method stub
		return value.addAnalyse(new LengthAnalyse(value.getText().length()));
	}

}
