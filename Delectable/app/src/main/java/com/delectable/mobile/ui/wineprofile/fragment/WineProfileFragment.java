package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.VarietalsHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.requests.CaptureNotesRequest;
import com.delectable.mobile.api.requests.HelpfulActionRequest;
import com.delectable.mobile.controllers.BaseWineController;
import com.delectable.mobile.data.BaseWineModel;
import com.delectable.mobile.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.common.widget.WineBannerView;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.dialog.ChooseVintageDialog;
import com.delectable.mobile.ui.wineprofile.widget.CaptureNotesAdapter;
import com.delectable.mobile.ui.wineprofile.widget.WineProfileCommentUnitRow;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.inject.Inject;


public class WineProfileFragment extends BaseFragment implements
        WineProfileCommentUnitRow.ActionsHandler {

    public static final String TAG = WineProfileFragment.class.getSimpleName();

    private static final int NO_AVG_RATING = -1;

    private static final String sArgsWineProfile = "wineProfile";

    private static final String sArgsPhotoHash = "photoHash";

    private static final String sArgsBaseWineId = "baseWineId";

    private static final String sArgsVintageId = "vintageId";

    private static final int CHOOSE_VINTAGE_DIALOG = 1;

    @Inject
    BaseWineController mBaseWineController;

    @Inject
    BaseWineModel mBaseWineModel;

    private View mVarietalContainer;

    private ImageView mVarietalImageView;

    private TextView mVarietalTextView;

    private TextView mRegionPathTextView;

    private TextView mAllRatingsAverageTextView;

    private TextView mAllRatingsCountTextView;

    private TextView mProRatingsAverageTextView;

    private TextView mProRatingsCountTextView;

    private TextView mAllYearsTextView;


    private BaseNetworkController mNetworkController;

    private ArrayList<CaptureNote> mCaptureNotes = new ArrayList<CaptureNote>();

    private CaptureNotesAdapter mAdapter = new CaptureNotesAdapter(mCaptureNotes, this);

    private WineProfile mWineProfile;

    private PhotoHash mCapturePhotoHash;

    private String mBaseWineId;

    private String mVintageId;

    private BaseWine mBaseWine;

    private ListingResponse<CaptureNote> mCaptureNoteListing;

    private WineBannerView mBanner;


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

    public static WineProfileFragment newInstance(String baseWineId,
            String vintageId) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putString(sArgsBaseWineId, baseWineId);
        args.putString(sArgsVintageId, vintageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mNetworkController = new BaseNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mWineProfile = args.getParcelable(sArgsWineProfile);
            mCapturePhotoHash = args.getParcelable(sArgsPhotoHash);

            mBaseWineId = args.getString(sArgsBaseWineId);
            mVintageId = args.getString(sArgsVintageId);
        }
        if (mBaseWineId == null && mWineProfile != null) {
            mBaseWineId = mWineProfile.getBaseWineId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_profile, container, false);
        ListView listview = (ListView) view.findViewById(R.id.list_view);

        //prepare header view
        View header = inflater.inflate(R.layout.wine_profile_header, null, false);

        mBanner = (WineBannerView) header.findViewById(R.id.wine_banner_view);
        updateBannerData();

        mVarietalContainer = header.findViewById(R.id.varietal_container);
        mVarietalImageView = (ImageView) header.findViewById(R.id.varietal_color_icon);
        mVarietalTextView = (TextView) header.findViewById(R.id.varietal_name);
        mRegionPathTextView = (TextView) header.findViewById(R.id.region_path_name);

        mAllRatingsAverageTextView = (TextView) header.findViewById(R.id.all_ratings_average);
        mAllRatingsCountTextView = (TextView) header.findViewById(R.id.all_ratings_count);
        mProRatingsAverageTextView = (TextView) header.findViewById(R.id.pro_ratings_average);
        mProRatingsCountTextView = (TextView) header.findViewById(R.id.pro_ratings_count);
        mAllYearsTextView = (TextView) header.findViewById(R.id.all_years_textview);

        mAllYearsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVintageDialog dialog = ChooseVintageDialog.newInstance(mBaseWine);
                dialog.setTargetFragment(WineProfileFragment.this,
                        CHOOSE_VINTAGE_DIALOG); //callback goes to onActivityResult
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        listview.addHeaderView(header, null, false);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--; //headerview offsets position of listitems by 1
                launchCaptureDetails(mAdapter.getItem(position));
            }
        });
        return view;
    }

    private void updateBannerData() {
        if (mWineProfile != null && mCapturePhotoHash != null) {
            mBanner.updateData(mWineProfile, mCapturePhotoHash, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //only load data if there is no base wine yet
        if (mBaseWine == null) {
            loadBaseWineData();
        }
        mBaseWineController.fetchBaseWine(mBaseWineId);
        if (mCaptureNoteListing == null) {
            loadCaptureNotesData(IdType.BASE_WINE, mWineProfile.getBaseWineId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CHOOSE_VINTAGE_DIALOG:
                Object wine = data.getParcelableExtra(ChooseVintageDialog.WINE);
                if (wine instanceof BaseWine) {
                    BaseWine baseWine = (BaseWine) wine;
                    loadCaptureNotesData(IdType.BASE_WINE, baseWine.getId());
                }
                if (wine instanceof WineProfile) {
                    WineProfile wineProfile = (WineProfile) wine;
                    loadCaptureNotesData(IdType.WINE_PROFILE, wineProfile.getId());
                }
        }
    }

    private void loadBaseWineData() {
        //retrieve full base wine information
        mBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);
        if (mBaseWine == null) {
            return;
        }
        if (mBaseWine.getWineProfiles() != null
                && mBaseWine.getWineProfiles().size() > 0) {
            // TODO: Loop Through Wine Profiles to get Vintage.  Will require this for price / purchasing.
            // TODO: Possibly create a helper method in BaseWine: mBaseWine.getWineByVintage("")
            mWineProfile = mBaseWine.getWineProfiles().get(0);

        }
        if (mCapturePhotoHash == null) {
            mCapturePhotoHash = mBaseWine.getPhoto();
        }
        updateBannerData();
        updateBaseWineData();
    }

    public void onEventMainThread(UpdatedBaseWineEvent event) {
        if (!mBaseWineId.equals(event.getBaseWineId())) {
            return;
        }

        if (event.isSuccessful()) {
            loadBaseWineData();
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    /**
     * @param idType Whether to load captures notes for a base wine or a wine profile.
     */
    private void loadCaptureNotesData(IdType idType, String id) {
        //retrieve captureNotes
        CaptureNotesRequest captureReq = new CaptureNotesRequest();
        if (idType == IdType.BASE_WINE) {
            captureReq.setBaseWineId(id);
        }
        if (idType == IdType.WINE_PROFILE) {
            captureReq.setWineProfileId(id);
        }
        mNetworkController.performRequest(captureReq,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        mCaptureNoteListing = (ListingResponse<CaptureNote>) result;
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

    private void markCaptureAsHelpful(CaptureNote captureNote, boolean markHelpful) {
        HelpfulActionRequest request = new HelpfulActionRequest(captureNote, markHelpful);
        mNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        //TODO implement HelpfulActionRequests's buildResopnseFromJson in order to have the result return non-null
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
            for (VarietalsHash varietal : mBaseWine.getVarietalComposition()) {
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
        // TODO : Fix , this shouldn't be null
        if (mCaptureNoteListing == null) {
            return;
        }
        mCaptureNotes.addAll(mCaptureNoteListing.getUpdates());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void toggleHelpful(CaptureNote captureNote, boolean markHelpful) {
        markCaptureAsHelpful(captureNote, markHelpful);
    }

    public void launchCaptureDetails(CaptureNote captureNote) {
        Intent intent = new Intent();
        intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                captureNote.getId());
        intent.setClass(getActivity(), CaptureDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    private static enum IdType {
        BASE_WINE, WINE_PROFILE;
    }
}
