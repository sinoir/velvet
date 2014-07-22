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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentCapturesTabFragment extends BaseFragment {

    public static final String TAG = "RecentCapturesTabFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private View mView;

    private ListView mListView;

    private UserCapturesAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private Account mUserAccount;

    // TODO: Pass up data via some mechanism
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

        mAdapter = new UserCapturesAdapter(getActivity(), mCaptureDetails, mUserId);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaptureDetails captureDetails = (CaptureDetails) mAdapter.getItem(position - 1);
                Intent intent = new Intent();
                intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                        captureDetails.getId());
                intent.setClass(getActivity(), CaptureDetailsActivity.class);
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
}
