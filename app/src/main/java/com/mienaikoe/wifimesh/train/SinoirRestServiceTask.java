package com.mienaikoe.wifimesh.train;

import android.os.AsyncTask;
import android.util.Log;

import com.google.transit.realtime.GtfsRealtime;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jesse on 1/31/2015.
 */
public class SinoirRestServiceTask extends AsyncTask<String, Void, List<GtfsRealtime.FeedMessage>> {


        private Exception exception;

        protected List<GtfsRealtime.FeedMessage> doInBackground(String... urls) {
            List<GtfsRealtime.FeedMessage> feedMessages = new ArrayList<GtfsRealtime.FeedMessage>(urls.length);
            try{
                HttpClient httpclient = new DefaultHttpClient();

                for( String url : urls ) {
                    HttpGet request = new HttpGet(url);
                    InputStream in = httpclient.execute(request).getEntity().getContent();
                    feedMessages.add( GtfsRealtime.FeedMessage.parseFrom(in) );
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return feedMessages;
        }

    /*
        protected void onPostExecute( feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
        */
}
