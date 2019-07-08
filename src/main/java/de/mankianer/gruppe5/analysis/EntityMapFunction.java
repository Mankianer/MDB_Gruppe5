package de.mankianer.gruppe5.analysis;

import com.aylien.textapi.responses.Sentiment;
import com.google.gson.Gson;
import de.mankianer.gruppe5.analyse.entity.Entity;
import de.mankianer.gruppe5.model.Tweet;
import de.mankianer.gruppe5.model.analyse.Analyse;
import de.mankianer.gruppe5.model.analyse.EntityAnalyse;
import de.mankianer.gruppe5.model.analyse.SentimentAnalyse;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.util.Collector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EntityMapFunction implements FlatMapFunction<Tweet, Analyse> {
    @Override
    public void flatMap(Tweet value, Collector<Analyse> out) throws Exception {

        String text = value.getText();
        final String urlString = "https://api.aylien.com/api/v1/entities";


        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(urlString);

        httppost.setHeader("X-AYLIEN-TextAPI-Application-Key", "d64f781e84f534048acad0cf63a82e94");
        httppost.setHeader("X-AYLIEN-TextAPI-Application-ID", "f8e5c5f6");

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("text", text));
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
        try {
            Entity e = gson.fromJson(jsonString, Entity.class);
            String[][] entities = {e.entities.location, e.entities.organization, e.entities.person};
            System.out.println(e);
            out.collect(value.addAnalyse(new EntityAnalyse(entities)));
        }catch (Exception e) {
        }
    }
}
