package de.mankianer.gruppe5.analysis;

import com.google.gson.Gson;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.SentimentAnalyse;
import org.apache.flink.api.common.functions.MapFunction;

import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SentimentAnalyseMapFunction implements MapFunction<Tweet, Analyse> {
    @Override
    //APP ID: f8e5c5f6
    //API Key: d64f781e84f534048acad0cf63a82e94
    public Analyse map(Tweet tweet) throws Exception {
        String text = tweet.getText();
        final String urlString = "https://api.aylien.com/api/v1/sentiment";


        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(urlString);

        httppost.setHeader("X-AYLIEN-TextAPI-Application-Key", "d64f781e84f534048acad0cf63a82e94");
        httppost.setHeader("X-AYLIEN-TextAPI-Application-ID", "f8e5c5f6");

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("mode", "tweet"));
        params.add(new BasicNameValuePair("text", "Hey? How are you?"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();


        Gson gson = new Gson();
        String jsonString = "";
        if (entity != null) {
            InputStream instream;
            try {
                instream = entity.getContent();
            }catch (Exception e){
                throw e;
            }

            int c = 0;
            while((c = instream.read()) != -1){
                jsonString += (char) c;
            }

        }
        Sentiment sent = gson.fromJson(jsonString, Sentiment.class);
        return tweet.addAnalyse(new SentimentAnalyse(sent.getPolarity()));
    }
}
