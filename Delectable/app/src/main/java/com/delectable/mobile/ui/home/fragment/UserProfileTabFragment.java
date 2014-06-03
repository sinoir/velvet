package com.delectable.mobile.ui.home.fragment;

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
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.UserCapturesAdapter;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileTabFragment extends BaseFragment {

    public static final String TAG = "UserProfileTabFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private View mView;

    private SwipeRefreshLayout mRefreshContainer;


    private ListView mListView;

    private View mProfileHeaderView;

    private TextView mUserNameTextView;

    private TextView mFollowerCountTextView;

    private TextView mFollowingCountTextView;

    private TextView mCaptureWineCountTextView;

    private CircleImageView mUserImageView;

    //TODO: ImageButtons?
    private Button mSwitchToListViewButton;

    private Button mSwitchToFeedViewButton;

    private UserCapturesAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private Account mUserAccount;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    public UserProfileTabFragment() {
        // Required empty public constructor
    }

    public static UserProfileTabFragment newInstance(String userId) {
        UserProfileTabFragment fragment = new UserProfileTabFragment();
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_user_profile_tab, container, false);
        mRefreshContainer = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        mProfileHeaderView = inflater.inflate(R.layout.profile_header, null);
        mListView.addHeaderView(mProfileHeaderView);

        mAdapter = new UserCapturesAdapter(getActivity(), mCaptureDetails, mUserId);
        mListView.setAdapter(mAdapter);
        setupPullToRefresh();
        setupHeader();

        return mView;
    }

    private void setupPullToRefresh() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view,
                    int firstVisibleItem,
                    int visibleItemCount,
                    int totalItemCount) {
                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0)
                        ?
                        0 : mListView.getChildAt(0).getTop();
                mRefreshContainer.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        mRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "On Refresh");
                // TODO: Show some indicator of refreshing
                loadData();
            }
        });
    }

    private void setupHeader() {
        // TODO: Will be placing main header in a viewpager
        View headerMainView = mProfileHeaderView.findViewById(R.id.profile_header_main);
        mUserImageView = (CircleImageView) headerMainView.findViewById(R.id.image);
        mUserNameTextView = (TextView) headerMainView.findViewById(R.id.user_name);
        mFollowerCountTextView = (TextView) headerMainView.findViewById(R.id.followers_count);
        mFollowingCountTextView = (TextView) headerMainView.findViewById(R.id.following_count);

        mCaptureWineCountTextView = (TextView) mProfileHeaderView
                .findViewById(R.id.capture_wine_count);

        mSwitchToListViewButton = (Button) mProfileHeaderView
                .findViewById(R.id.switch_to_listing_button);
        mSwitchToFeedViewButton = (Button) mProfileHeaderView
                .findViewById(R.id.switch_to_feed_listing_button);
        mSwitchToListViewButton.setSelected(true);
        setupSwitchButtons();
    }

    private void setupSwitchButtons() {
        mSwitchToListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchToListViewButton.setSelected(true);
                mSwitchToFeedViewButton.setSelected(false);
                // TODO: Switch Adapters
            }
        });
        mSwitchToFeedViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchToListViewButton.setSelected(false);
                mSwitchToFeedViewButton.setSelected(true);
                // TODO: Switch Adapters
            }
        });
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
                        mAdapter.notifyDataSetChanged();
                        mRefreshContainer.setRefreshing(false);
                        updateUIWithData();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        mRefreshContainer.setRefreshing(false);
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
        String wineCount = getResources().getString(R.string.wine_count, numCaptures);

        wineCount = wineCount != null ? wineCount : "";

        ImageLoaderUtil.loadImageIntoView(getActivity(), imageUrl, mUserImageView);
        mUserNameTextView.setText(userName);
        mFollowerCountTextView.setText(String.valueOf(mUserAccount.getFollowerCount()));
        mFollowingCountTextView.setText(String.valueOf(mUserAccount.getFollowingCount()));
        mCaptureWineCountTextView.setText(wineCount);
    }
}
