package com.mienaikoe.wifimesh.sinoir;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ProtoConverter;

/**
 * Created by hoyinlai on 2/1/2015.
 */
public class GtfsUpdateService extends IntentService {

    private EventBus mEventBus;
    private int mInterval;
    private boolean mIsPause;
    private IGtfsUpdateRetrofit endPoint = null;

    public GtfsUpdateService() {
        super("GtfsUpdateService");

        mEventBus = EventBus.getDefault();
        mInterval = 1000 * 30 + 10;  // refresh every 30s + 10ms
        mIsPause = true; //This will destroy my data plan if it's false.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initialize();
        while (this.endPoint != null) {
            postLoop();
        }
    }


    private void initialize(){
        try {
            Resources resources = this.getResources();
            AssetManager assetManager = resources.getAssets();

            InputStream inputStream = assetManager.open("config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            String url = properties.getProperty("sinoir.web.service.url");
            Log.i(this.getClass().getName(), String.format("SinoIr REST web service URL: %s", url));

            endPoint = (new RestAdapter.Builder()).setEndpoint(url).build().create(IGtfsUpdateRetrofit.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void postLoop(){
        if (!mIsPause) {
            synchronized (this) {
                try {
                    List<GtfsRealtime.FeedMessage> messages = new ArrayList<>(2);
                    messages.add(GtfsRealtime.FeedMessage.parseFrom(this.endPoint.getUpdate(1).getBody().in()));
                    messages.add(GtfsRealtime.FeedMessage.parseFrom(this.endPoint.getUpdate(2).getBody().in()));
                    mEventBus.post(new GtfsUpdateEvent(messages));
                } catch (IOException e) {
                    Log.e(this.getClass().getSimpleName(), "IO Error Occured: "+e.getMessage());
                } catch (RetrofitError e){
                    Log.e(this.getClass().getSimpleName(), "Retrofit Error Occured: "+e.getMessage());
                }

                try {
                    this.wait(mInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
