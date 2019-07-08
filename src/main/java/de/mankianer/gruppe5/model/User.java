package de.mankianer.gruppe5.model;

import lombok.Data;

/**
 * 
 * @author Marvin Wölk
 * 
 * Klassen zum Parsen der einegehene Tweets
 *
 */
@Data
public class User {
	
	private long id;
	private String name;
	private String screen_name;
	private String location;
	private boolean verified;
	private int friends_count;
	private int followers_count;
	private int statuses_count;
}
