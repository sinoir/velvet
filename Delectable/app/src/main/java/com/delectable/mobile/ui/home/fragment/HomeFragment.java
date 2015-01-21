package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.events.accounts.UpdatedCaptureFeedsEvent;
import com.delectable.mobile.api.events.ui.HideOrShowFabEvent;
import com.delectable.mobile.api.models.CaptureFeed;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingTabAdapter;
import com.delectable.mobile.ui.common.widget.SlidingTabLayout;
import com.delectable.mobile.ui.search.activity.SearchActivity;
import com.delectable.mobile.ui.search.fragment.SearchFragment;
import com.melnykov.fab.FloatingActionButton;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment {

    /**
     * Used with the ViewPager in order to cache a certain amount of pages
     */
    public static final int PREFETCH_FEED_COUNT = 6;

    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * keeps track of which position each feed key is in in the view pager
     */
    private HashMap<String, Integer> mFeedKeyMap = new HashMap<String, Integer>();

    private View mView;

    private ViewPager mViewPager;

    private SlidingTabLayout mTabLayout;

    protected FloatingActionButton mCameraButton;

    private SlidingTabAdapter mTabsAdapter;

    private List<CaptureFeed> mCaptureFeeds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabLayout = (SlidingTabLayout) mView.findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));

        mCameraButton = (FloatingActionButton) mView.findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });

        List<CaptureFeed> captureFeeds = UserInfo.getCaptureFeeds();
        populateFeedTabs(captureFeeds, captureFeeds);

        return mView;
    }

    private void populateFeedTabs(List<CaptureFeed> captureFeeds,
            List<CaptureFeed> oldCaptureFeeds) {
        String currentUserId = UserInfo.getUserId(getActivity());
        List<SlidingTabAdapter.SlidingTabItem> tabItems
                = new ArrayList<SlidingTabAdapter.SlidingTabItem>();
        mFeedKeyMap.clear();

        if (captureFeeds != null) {
            int i = 0;
            for (CaptureFeed feed : captureFeeds) {
                // list banner
                int backgroundColor = getResources().getColor(R.color.d_suva_gray);
                int textColor = getResources().getColor(R.color.d_white);
                List<CaptureFeed.BannerAttribute> attr = feed.getBannerAttributes();
                if (attr != null) {
                    for (CaptureFeed.BannerAttribute a : attr) {
                        if (a.getType().equals(CaptureFeed.BannerAttribute.BG_COLOR)) {
                            try {
                                backgroundColor = Color.parseColor(a.getValue());
                            } catch (IllegalArgumentException e) {
                            }
                        } else if (a.getType().equals(CaptureFeed.BannerAttribute.TEXT_COLOR)) {
                            try {
                                textColor = Color.parseColor(a.getValue());
                            } catch (IllegalArgumentException e) {
                            }
                        }
                    }
                }

                // add feed to tabs
                boolean isNewFeed = oldCaptureFeeds != null && !oldCaptureFeeds.contains(feed);
                tabItems.add(i, new SlidingTabAdapter.SlidingTabItem(
                        CaptureListFragment
                                .newInstance(feed.getKey(), feed.getFeedType(),
                                        feed.getTitle(),
                                        feed.getBanner(), backgroundColor, textColor),
                        feed.getTitle().toLowerCase(),
                        isNewFeed
                ));
                mFeedKeyMap.put(feed.getKey(), i);
                i++;
            }
        }
        mCaptureFeeds = captureFeeds;

        mTabsAdapter = new SlidingTabAdapter(getFragmentManager(), tabItems);
        mViewPager.setAdapter(mTabsAdapter);
        mViewPager.setOffscreenPageLimit(PREFETCH_FEED_COUNT);
        // TODO page margin does not work with the tab indicator
//        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.spacing_16));
        // material design does not like page transformers!
//        mViewPager.setPageTransformer(false, new FeedPageTransformer());
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        //mEventBus.registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mCameraButton.isShown()) {
            mCameraButton.show(true);
        }
    }

    public void onEventMainThread(ShowSpecificFeedEvent event) {
        String feedKey = event.getFeedKey();
        Integer position = mFeedKeyMap.get(feedKey);
        if (position == null) {
            return;
        }
        mViewPager.setCurrentItem(position);

        //event is consumed, we can remove it now
        mEventBus.removeStickyEvent(event);

    }


    public void onEventMainThread(HideOrShowFabEvent event) {
        if (event.show) {
            mCameraButton.show(true);
        } else {
            mCameraButton.hide(true);
        }
    }

    public void onEventMainThread(UpdatedCaptureFeedsEvent event) {
        if (event.isSuccessful()) {
            if (event.getCaptureFeeds() != null && !event.getCaptureFeeds().equals(mCaptureFeeds)) {
                populateFeedTabs(event.getCaptureFeeds(), event.getOldCaptureFeeds());
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getBaseActivity().deregisterHeaderView(mTabLayout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().registerHeaderView(mTabLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_action_search:
                startActivity(SearchActivity.newIntent(getActivity(), SearchFragment.WINES));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ShowSpecificFeedEvent {

        private final String mFeedKey;

        public ShowSpecificFeedEvent(String feedKey) {
            mFeedKey = feedKey;
        }

        public String getFeedKey() {
            return mFeedKey;
        }
    }

}
