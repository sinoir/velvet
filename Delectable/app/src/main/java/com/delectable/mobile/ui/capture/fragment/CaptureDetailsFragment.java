package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.requests.CapturesContextRequest;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CaptureDetailsFragment extends BaseCaptureDetailsFragment {

    public static final String TAG = "CaptureDetailsFragment";

    private static final String sArgsCaptureId = "sArgsCaptureId";

    private View mView;

    private CaptureDetailsView mCaptureDetailsView;

    private String mCaptureId;

    private BaseNetworkController mNetworkController;

    private CaptureDetails mCaptureDetails;

    public CaptureDetailsFragment() {
        // Required empty public constructor
    }

    public static CaptureDetailsFragment newInstance(String captureId) {
        CaptureDetailsFragment fragment = new CaptureDetailsFragment();
        Bundle args = new Bundle();
        args.putString(sArgsCaptureId, captureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkController = new BaseNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mCaptureId = args.getString(sArgsCaptureId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_capture_details, container, false);
        mCaptureDetailsView = (CaptureDetailsView) mView.findViewById(R.id.capture_details_view);
        mCaptureDetailsView.setActionsHandler(this);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void dataSetChanged() {
        mCaptureDetailsView.updateCaptureData(mCaptureDetails);
    }

    private void loadData() {
        CapturesContextRequest request = new CapturesContextRequest();
        request.setId(mCaptureId);
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);
                        mCaptureDetails = (CaptureDetails) result;
                        mCaptureDetailsView.updateCaptureData(mCaptureDetails);
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
