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
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.common.widget.UserCapturesAdapter;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileFragment extends BaseFragment {

    public static final String TAG = "UserProfileFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private static final String sArgsDispalyUserNameInActionBar = "sArgsDispalyUserNameInActionBar";

    private View mView;

    private SwipeRefreshLayout mRefreshContainer;

    private ListView mListView;

    private ProfileHeaderView mProfileHeaderView;

    private UserCapturesAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private Account mUserAccount;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    private boolean mShouldShowNameInActionBar = false;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String userId) {
        return newInstance(userId, false);
    }

    public static UserProfileFragment newInstance(String userId,
            boolean displayUserNameInActionbar) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(sArgsUserId, userId);
        args.putBoolean(sArgsDispalyUserNameInActionBar, displayUserNameInActionbar);
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
            mShouldShowNameInActionBar = args.getBoolean(sArgsDispalyUserNameInActionBar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_user_profile_tab, container, false);
        mRefreshContainer = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        // TODO: Implement ProfileHeaderActionListener
        mProfileHeaderView = new ProfileHeaderView(getActivity());
        mListView.addHeaderView(mProfileHeaderView);

        mAdapter = new UserCapturesAdapter(getActivity(), mCaptureDetails, mUserId);
        mListView.setAdapter(mAdapter);
        setupPullToRefresh();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ignore header
                if (position > 0) {
                    CaptureDetails captureDetails = (CaptureDetails) mAdapter.getItem(position - 1);
                    Intent intent = new Intent();
                    intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                            captureDetails.getId());
                    intent.setClass(getActivity(), CaptureDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

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

        mProfileHeaderView.setWineCount(numCaptures);

        ImageLoaderUtil.loadImageIntoView(getActivity(), imageUrl,
                mProfileHeaderView.getUserImageView());
        mProfileHeaderView.setUserName(userName);
        mProfileHeaderView.setFollowerCount(mUserAccount.getFollowerCount());
        mProfileHeaderView.setFollowingCount(mUserAccount.getFollowingCount());

        if (mShouldShowNameInActionBar) {
            getActivity().getActionBar().setTitle(mUserAccount.getFname());
        }
    }
}
