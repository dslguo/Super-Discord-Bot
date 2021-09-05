package com.example.demo.listeners.impl;

import com.example.demo.listeners.QuoteListener;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class QuoteListenerImpl implements QuoteListener {

    @Autowired
    private static HttpURLConnection connection;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        // Method: Java.net.HttpURLConnection
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        if(messageCreateEvent.getMessageContent().equals("!quote")) {
            try {
                URL url = new URL("https://zenquotes.io/api/random/");
                connection = (HttpURLConnection) url.openConnection();

                // Request Setup
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int status = connection.getResponseCode();

                if (status > 299) {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
                // Parse responseContent which is in JSON format
//                messageCreateEvent.getChannel().sendMessage(responseContent.toString());

                JSONArray quotes = new JSONArray(responseContent.toString());
                for (int i = 0; i < quotes.length(); i++) {
                    JSONObject quoteDetails = quotes.getJSONObject(i);
                    String quote = quoteDetails.getString("q");
                    String author = quoteDetails.getString("a");
                    messageCreateEvent.getChannel().sendMessage(quote + " - " + author);
                }

//                System.out.println(responseContent.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

        }
    }
}
