package de.mankianer.gruppe5.util;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class ReduceCounter implements ReduceFunction<Tuple2<String, Integer>>{

	@Override
	public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2)
			throws Exception {
		
		return new Tuple2<String, Integer>("Count", value1.f1 + 1);
	}


}
