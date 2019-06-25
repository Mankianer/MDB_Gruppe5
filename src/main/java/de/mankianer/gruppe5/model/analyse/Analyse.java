package de.mankianer.gruppe5.model.analyse;

import java.io.Serializable;

import lombok.Data;

@Data
public class Analyse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Analyse(String result) {
		this.result = result;
	}
	
	private  String result;
}
