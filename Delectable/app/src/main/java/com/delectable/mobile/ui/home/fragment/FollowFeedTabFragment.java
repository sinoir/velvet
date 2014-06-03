package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureDetailsListing;
import com.delectable.mobile.api.requests.AccountsFollowerFeedRequest;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FollowFeedAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FollowFeedTabFragment extends BaseFragment {

    private View mView;

    private ListView mListView;

    private FollowFeedAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private CaptureDetailsListing mDetailsListing;

    private ArrayList<CaptureDetails> mCaptureDetails;

    public FollowFeedTabFragment() {
        // Required empty public constructor
    }

    public static FollowFeedTabFragment newInstance() {
        FollowFeedTabFragment fragment = new FollowFeedTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaptureDetails = new ArrayList<CaptureDetails>();
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_follow_feed_tab, container, false);

        // TODO: Pull to Refresh
        // TODO: Pagination
        mListView = (ListView) mView.findViewById(R.id.list_view);

        mAdapter = new FollowFeedAdapter(getActivity(), mCaptureDetails);
        mListView.setAdapter(mAdapter);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        AccountsFollowerFeedRequest request = new AccountsFollowerFeedRequest(
                AccountsFollowerFeedRequest.CONTEXT_DETAILS);
        // TODO: Update with Before / After for pagination
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mDetailsListing = (CaptureDetailsListing) result;
                        updateDisplayData();
                    }

                    @Override
                    public void onFailed(RequestError error) {

                    }
                }
        );
    }

    private void updateDisplayData() {
        mCaptureDetails.clear();
        // TODO : Figure out way to combine before/after listing and deletes..
        mCaptureDetails.addAll(mDetailsListing.getUpdates());
        mAdapter.notifyDataSetChanged();
    }
}
