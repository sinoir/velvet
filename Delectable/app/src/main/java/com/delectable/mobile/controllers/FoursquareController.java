package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.foursquare.SearchFoursquareVenuesJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class FoursquareController {

    private static final String TAG = FoursquareController.class.getSimpleName();

    @Inject
    JobManager mJobManager;

    public void searchFoursquareVenuesByLatLon(String latLon) {
        mJobManager.addJobInBackground(new SearchFoursquareVenuesJob(latLon));
    }
}
