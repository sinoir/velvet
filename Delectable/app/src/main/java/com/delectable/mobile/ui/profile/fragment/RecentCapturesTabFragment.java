package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.requests.BaseCaptureFeedListingRequest;
import com.delectable.mobile.api.requests.CaptureFeedRequest;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FollowFeedAdapter;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

// TODO / Note: Abstract something from FollowFeedTabFragment, these are almost identical.
public class RecentCapturesTabFragment extends BaseCaptureDetailsFragment {

    public static final String TAG = "RecentCapturesTabFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private View mView;

    private ListView mListView;

    private FollowFeedAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private ListingResponse<CaptureDetails> mDetailsListing;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    public RecentCapturesTabFragment() {
        // Required empty public constructor
    }

    public static RecentCapturesTabFragment newInstance(String userId) {
        RecentCapturesTabFragment fragment = new RecentCapturesTabFragment();
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
        mView = inflater.inflate(R.layout.list_view_layout, container, false);

        mListView = (ListView) mView.findViewById(android.R.id.list);

        // Not handling pagination here
        mAdapter = new FollowFeedAdapter(getActivity(), mCaptureDetails, null, this, mUserId);
        mAdapter.setCurrentViewType(FollowFeedAdapter.VIEW_TYPE_SIMPLE);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaptureDetails captureDetails = (CaptureDetails) mAdapter.getItem(position);
                Intent intent = new Intent();
                // Launch WineProfile if the capture matched a wine, otherwise launch the capture details
                if (captureDetails.getWineProfile() != null) {
                    intent.putExtra(WineProfileActivity.PARAMS_WINE_PROFILE,
                            captureDetails.getWineProfile());
                    intent.putExtra(WineProfileActivity.PARAMS_CAPTURE_PHOTO_HASH,
                            captureDetails.getPhoto());
                    intent.setClass(getActivity(), WineProfileActivity.class);
                } else {
                    intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                            captureDetails.getId());
                    intent.setClass(getActivity(), CaptureDetailsActivity.class);

                }
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        BaseCaptureFeedListingRequest request = new CaptureFeedRequest(
                BaseCaptureFeedListingRequest.CONTEXT_DETAILS);
        request.setId(mUserId);
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);
                        mDetailsListing = (ListingResponse<CaptureDetails>) result;
                        mCaptureDetails.clear();
                        if (mDetailsListing != null) {
                            mCaptureDetails.addAll(mDetailsListing.getSortedCombinedData());
                        } else {
                            // TODO: Emptystate for no data?
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        showToastError(error.getMessage());
                    }
                }
        );
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
