package de.mankianer.gruppe5.model.analyse;

import lombok.Data;

public class WordCountAnalyse extends Analyse {

	int wordCount;
	
	public WordCountAnalyse(int result) {
		super(result);
		wordCount = result;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
