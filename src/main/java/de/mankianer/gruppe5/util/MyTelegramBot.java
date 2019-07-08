package de.mankianer.gruppe5.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;import java.net.SocketAddress;
import java.util.LinkedList;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

import de.mankianer.gruppe5.model.Tweet;

public class MyTelegramBot {
	
	private TelegramBot bot;
	
	private LinkedList<Long> chatIds;
	
	private static MyTelegramBot instance;

	private Gson gson;

	private MyTelegramBot() {
		chatIds = new LinkedList<Long>();
		
		gson = new Gson();
		
		bot = new TelegramBot("201091374:AAEy8-w2aXNyoMaN23tSFqddBPPt5t0YT1A");
		
		bot.setUpdatesListener(updates -> {
			
			updates.forEach(action -> {
				
			long id = action.message().chat().id();
			String text = action.message().text();
			System.out.println("TELEGRAMM: " + action.message().from().username() + " " + id + " " + text);
				
			if(text.toLowerCase().contains("stop")) {
				chatIds.remove(id);
				bot.execute(new SendMessage(id, "bye."));
			}else if(!chatIds.contains(id)){
				System.out.println("Added");
				chatIds.add(id);
				bot.execute(new SendMessage(id, "Hallo!\nzum Gruppe 5 TelgramBot!\nmit 'stop' kann man sich abmelden"));
			}else if(text.toLowerCase().contains("exit")) {
				send("Gute Nacht...");
				System.exit(0);
			}
			});
			
		    return UpdatesListener.CONFIRMED_UPDATES_ALL;
		});
		
	}
	
	public static MyTelegramBot getInstance() {
		return instance = instance == null ? new MyTelegramBot() : instance;
	}
	
	private void send(String msg) {
		System.out.println("Send msg(x" + chatIds.size() + "): " + msg);
		chatIds.forEach(id -> {
			System.out.println("Send zu " + id);
			bot.execute(new SendMessage(id, msg ));
		});
	}
	
	private void sendTo(Tweet msg) {
		send(gson.toJson(msg));
	}
	
	public static void send(Tweet msg) {
		
		getInstance().sendTo(msg);
	}
}
