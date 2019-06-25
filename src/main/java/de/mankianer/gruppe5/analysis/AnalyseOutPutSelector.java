package de.mankianer.gruppe5.analysis;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;

import de.mankianer.gruppe5.model.analyse.Analyse;

public class AnalyseOutPutSelector implements OutputSelector<Analyse>{

	@Override
	public Iterable<String> select(Analyse value) {
		return null;
	}

}
