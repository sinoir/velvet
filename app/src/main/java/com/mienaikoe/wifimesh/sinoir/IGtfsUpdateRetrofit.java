package com.mienaikoe.wifimesh.sinoir;

import com.google.transit.realtime.GtfsRealtime;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by hoyinlai on 2/1/2015.
 */
public interface IGtfsUpdateRetrofit {
    // TODO(hoyinlai): change to use GtfsRealtime.FeedMessage
    @GET("/mta/subway/update/{id}")
    Response getUpdate(@Path("id") int id);
}
