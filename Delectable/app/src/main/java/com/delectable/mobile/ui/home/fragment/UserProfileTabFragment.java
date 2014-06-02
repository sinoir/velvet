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
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserProfileTabFragment extends BaseFragment {

    private View mView;

    private ListView mListView;

    private View mProfileHeaderView;

    private TextView mUserNameTextView;

    private TextView mFollowerCountTextView;

    private TextView mFollowingCountTextView;

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

    public static UserProfileTabFragment newInstance() {
        UserProfileTabFragment fragment = new UserProfileTabFragment();
        Bundle args = new Bundle();
        // TODO: Pass ID
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaptureDetails = new ArrayList<CaptureDetails>();
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        // TODO: Get UserID from Bundle
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_user_profile_tab, container, false);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        mProfileHeaderView = inflater.inflate(R.layout.profile_header, null);
        mListView.addHeaderView(mProfileHeaderView);

        mAdapter = new UserCapturesAdapter(getActivity(), mCaptureDetails);
        mListView.setAdapter(mAdapter);
        setupHeader();

        return mView;
    }

    private void setupHeader() {
        // TODO: Will be placing main header in a viewpager
        View headerMainView = mProfileHeaderView.findViewById(R.id.profile_header_main);
        mUserImageView = (CircleImageView) headerMainView.findViewById(R.id.image);
        mUserNameTextView = (TextView) headerMainView.findViewById(R.id.user_name);
        mFollowerCountTextView = (TextView) headerMainView.findViewById(R.id.followers_count);
        mFollowingCountTextView = (TextView) headerMainView.findViewById(R.id.following_count);

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
        loadAccountProfile();
    }

    private void loadAccountProfile() {
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PROFILE);
        request.setId(mUserId);
        mAccountsNetworkController.performActionOnResource(request,
                new BaseNetworkController.RequestActionCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mUserAccount = (Account) result;
                        mCaptureDetails.clear();
                        if (mUserAccount.getCaptureSummaries() != null
                                && mUserAccount.getCaptureSummaries().size() > 0) {
                            for (CaptureSummary summary : mUserAccount.getCaptureSummaries()) {
                                mCaptureDetails.addAll(summary.getCaptures());
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        updateUIWithData();
                    }

                    @Override
                    public void onFailed(RequestError error) {

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
        Picasso.with(getActivity()).load(imageUrl).into(mUserImageView.getPicassoTarget());
        mUserNameTextView.setText(userName);
        mFollowerCountTextView.setText(String.valueOf(mUserAccount.getFollowerCount()));
        mFollowingCountTextView.setText(String.valueOf(mUserAccount.getFollowingCount()));
    }
}
