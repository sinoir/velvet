package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureSummary;
import com.delectable.mobile.api.requests.AccountsContextRequest;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileFragment extends BaseFragment implements
        ProfileHeaderView.ProfileHeaderActionListener {

    public static final String TAG = "UserProfileFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private View mView;

    private ProfileHeaderView mProfileHeaderView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private SlidingPagerAdapter mTabsAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private Account mUserAccount;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(sArgsUserId, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaptureDetails = new ArrayList<CaptureDetails>();
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(sArgsUserId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mProfileHeaderView = (ProfileHeaderView) mView.findViewById(R.id.profile_header_view);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<SlidingPagerAdapter.SlidingPagerItem> tabItems
                = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();

        // TODO: Split Recent / TopRated Tabs
        // "RECENT" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                RecentCapturesTabFragment.newInstance(mUserId),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.profile_tab_recent)));

        // "TOP RATED" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                RecentCapturesTabFragment.newInstance(mUserId),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.profile_tab_top_rated)));

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PROFILE);
        request.setId(mUserId);
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);

                        mUserAccount = (Account) result;
                        mCaptureDetails.clear();
                        if (mUserAccount.getCaptureSummaries() != null
                                && mUserAccount.getCaptureSummaries().size() > 0) {
                            for (CaptureSummary summary : mUserAccount.getCaptureSummaries()) {
                                mCaptureDetails.addAll(summary.getCaptures());
                            }
                        }
                        updateUIWithData();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        // TODO: What to do with errors?
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void updateUIWithData() {
        if (getActivity() == null) {
            return;
        }

        String userName = mUserAccount.getFname() + " " + mUserAccount.getLname();
        String imageUrl = mUserAccount.getPhoto().getUrl();
        int numCaptures = mUserAccount.getCaptureCount() != null ?
                mUserAccount.getCaptureCount() : 0;

        mProfileHeaderView.setWineCount(numCaptures);

        ImageLoaderUtil.loadImageIntoView(getActivity(), imageUrl,
                mProfileHeaderView.getUserImageView());
        mProfileHeaderView.setUserName(userName);
        mProfileHeaderView.setFollowerCount(mUserAccount.getFollowerCount());
        mProfileHeaderView.setFollowingCount(mUserAccount.getFollowingCount());

        if (mUserAccount.isUserRelationshipTypeSelf()) {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_SELF);
        } else if (mUserAccount.isUserRelationshipTypeFollowing()) {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_FOLLOWING);
        } else {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_NOT_FOLLOWING);
        }
    }

    @Override
    public void toggleFollowUserClicked() {
        // TODO: Make Follow Request
    }

    @Override
    public void wineCountClicked() {
        // TODO: Do we do anything?
    }

    @Override
    public void followerCountClicked() {
        // TODO: Do we do anything?
    }

    @Override
    public void followingCountClicked() {
        // TODO: Do we do anything?
    }
}
