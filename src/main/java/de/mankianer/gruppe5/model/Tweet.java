package de.mankianer.gruppe5.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mankianer.gruppe5.model.analyse.Analyse;
import lombok.Data;
import lombok.Getter;

@Data
public class Tweet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String screen_name;
	private String location;
	private boolean verified;
	private int friends_cound;
	private int followers_count;
	private int statuses_count;
	
	private String created_at, romantic_date;

	private String text;
	private String lang;
	private boolean is_retweet;
	
	private Map<String, Analyse> analysen;
	
	private Map<String, Analyse> getAnalysen(){
		return analysen = (analysen == null ? new HashMap<String, Analyse>() : analysen);
	}

	public Tweet reduce(Tweet tweet){
		if(tweet.getText() == null) {
			tweet.getAnalysen().forEach((String s, Analyse a) -> addAnalyse(a));
			return this;
		}else{
			getAnalysen().forEach((String s, Analyse a) -> tweet.addAnalyse(a));
			return tweet;
		}
	}
	
	public Analyse addAnalyse(Analyse analyse) {
		analyse.setTweetID(id);
		getAnalysen().put(analyse.getClass().getSimpleName(), analyse);
		return analyse;
	}
}
