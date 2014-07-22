package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.CaptureNoteListing;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.VarietalsHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.requests.BaseWinesContext;
import com.delectable.mobile.api.requests.CaptureNotesRequest;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.WineBannerView;
import com.delectable.mobile.ui.wineprofile.widget.CaptureNotesAdapter;

import org.apache.commons.lang3.StringUtils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class WineProfileFragment extends BaseFragment {

    public static final String TAG = WineProfileFragment.class.getSimpleName();

    private static final int NO_AVG_RATING = -1;

    private static final String sArgsWineProfile = "wineProfile";

    private static final String sArgsPhotoHash = "photoHash";

    private ListView mListView;

    private View mVarietalContainer;

    private ImageView mVarietalImageView;

    private TextView mVarietalTextView;

    private TextView mRegionPathTextView;

    private TextView mAllRatingsAverageTextView;

    private TextView mAllRatingsCountTextView;

    private TextView mProRatingsAverageTextView;

    private TextView mProRatingsCountTextView;


    private BaseNetworkController mNetworkController;

    private ArrayList<CaptureNote> mCaptureNotes = new ArrayList<CaptureNote>();

    private CaptureNotesAdapter mAdapter = new CaptureNotesAdapter(mCaptureNotes);

    private WineProfile mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private BaseWine mBaseWine;

    private CaptureNoteListing mCaptureNoteListing;


    /**
     * Returns fragment that uses the capture photo as it's wine image.
     *
     * @param capturePhotoHash {@link com.delectable.mobile.api.models.CaptureDetails
     *                         CaptureDetails}' PhotoHash. Pass in null to use WineProfile's image.
     */
    public static WineProfileFragment newInstance(WineProfile wineProfile,
            PhotoHash capturePhotoHash) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(sArgsWineProfile, wineProfile);
        args.putParcelable(sArgsPhotoHash, capturePhotoHash);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkController = new BaseNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mWineProfile = args.getParcelable(sArgsWineProfile);
            mCapturePhotoHash = args.getParcelable(sArgsPhotoHash);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_profile, container, false);
        ListView listview = (ListView) view.findViewById(R.id.list_view);

        //prepare header view
        View header = inflater.inflate(R.layout.wine_profile_header, null, false);

        WineBannerView banner = (WineBannerView) header.findViewById(R.id.wine_banner_view);
        banner.updateData(mWineProfile, mCapturePhotoHash, false);

        mVarietalContainer = header.findViewById(R.id.varietal_container);
        mVarietalImageView = (ImageView) header.findViewById(R.id.varietal_color_icon);
        mVarietalTextView = (TextView) header.findViewById(R.id.varietal_name);
        mRegionPathTextView = (TextView) header.findViewById(R.id.region_path_name);

        mAllRatingsAverageTextView = (TextView) header.findViewById(R.id.all_ratings_average);
        mAllRatingsCountTextView = (TextView) header.findViewById(R.id.all_ratings_count);
        mProRatingsAverageTextView = (TextView) header.findViewById(R.id.pro_ratings_average);
        mProRatingsCountTextView = (TextView) header.findViewById(R.id.pro_ratings_count);

        listview.addHeaderView(header, null, false);
        listview.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //only load data if there is no base wine yet
        if (mBaseWine == null) {
            loadBaseWineData();
        }
        if (mCaptureNoteListing == null) {
            loadCaptureNotesData();
        }
    }

    private void loadBaseWineData() {
        //retrieve full base wine information
        BaseWinesContext request = new BaseWinesContext();
        request.setId(mWineProfile.getBaseWineId());
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mBaseWine = (BaseWine) result;
                        updateBaseWineData();
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

    private void loadCaptureNotesData() {
        //retrieve captureNotes
        CaptureNotesRequest captureReq = new CaptureNotesRequest();
        captureReq.setBaseWineId(mWineProfile.getBaseWineId());
        mNetworkController.performRequest(captureReq,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mCaptureNoteListing = (CaptureNoteListing) result;
                        updateCaptureNotesData();
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

    private void updateBaseWineData() {

        //varietal info
        if (mBaseWine.getVarietalComposition().size() == 0) {
            //beers don't have varietal composition
            mVarietalContainer.setVisibility(View.GONE);
        } else {
            GradientDrawable varietalDrawable = (GradientDrawable) mVarietalImageView.getDrawable();
            String hexColor = mBaseWine.getVarietalComposition().get(0).getColor();
            varietalDrawable.setColor(Color.parseColor(hexColor));

            //combine varietal names if there's more than one
            ArrayList<String> varietalNames = new ArrayList<String>();
            for(VarietalsHash varietal: mBaseWine.getVarietalComposition()) {
                varietalNames.add(varietal.getName());
            }
            TextUtils.join(", ", varietalNames);
            String varietalDisplayText = StringUtils.join(varietalNames, ", ");
            mVarietalTextView.setText(varietalDisplayText);
        }

        //regional info
        String regionPath = mBaseWine.getRegionPathDisplayText(getActivity());
        mRegionPathTextView.setText(regionPath);

        //rating avg
        double allAvg = mBaseWine.getRatingsSummary().getAllAvgOfTen();
        double proAvg = mBaseWine.getRatingsSummary().getProAvgOfTen();
        DecimalFormat format = new DecimalFormat("0.0");
        if (allAvg == NO_AVG_RATING) { //handling a no rating case, show a dash
            mAllRatingsAverageTextView.setText("-");
            mAllRatingsAverageTextView.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            String allAvgStr = format.format(allAvg);
            mAllRatingsAverageTextView.setText(makeRatingDisplayText(allAvgStr));
        }
        if (proAvg == NO_AVG_RATING) {
            mProRatingsAverageTextView.setText("-");
            mProRatingsAverageTextView.setTextColor(getResources().getColor(R.color.d_medium_gray));
        } else {
            String proAvgStr = format.format(proAvg);
            mProRatingsAverageTextView.setText(makeRatingDisplayText(proAvgStr));
        }

        //ratings count
        int allCount = mBaseWine.getRatingsSummary().getAllCount();
        int proCount = mBaseWine.getRatingsSummary().getProCount();
        String allRatingsCount = getResources().getQuantityString(
                R.plurals.wine_profile_ratings_count, allCount, allCount);
        String proRatingsCount = getResources()
                .getQuantityString(R.plurals.wine_profile_pro_ratings_count, proCount, proCount);
        mAllRatingsCountTextView.setText(allRatingsCount);
        mProRatingsCountTextView.setText(proRatingsCount);
    }

    /**
     * Makes the rating display text where the rating is a bit bigger than the 10.
     */
    private CharSequence makeRatingDisplayText(String rating) {
        SpannableString ss = new SpannableString(rating);
        ss.setSpan(new RelativeSizeSpan(1.3f), 0, rating.length(), 0); // set size
        CharSequence displayText = TextUtils.concat(ss, "/10");
        return displayText;
    }

    private void updateCaptureNotesData() {
        mCaptureNotes.clear();
        mCaptureNotes.addAll(mCaptureNoteListing.getUpdates());
        mAdapter.notifyDataSetChanged();
    }


}
