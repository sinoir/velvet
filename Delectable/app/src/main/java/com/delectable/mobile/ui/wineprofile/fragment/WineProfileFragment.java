package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.cache.CaptureNoteListingModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.controllers.CaptureController;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.captures.MarkedCaptureHelpfulEvent;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.Ratingsable;
import com.delectable.mobile.api.models.VarietalsHash;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.api.models.WineProfileSubProfile;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.Rating;
import com.delectable.mobile.ui.common.widget.WineBannerView;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.dialog.BuyVintageDialog;
import com.delectable.mobile.ui.wineprofile.dialog.ChooseVintageDialog;
import com.delectable.mobile.ui.wineprofile.dialog.Over21Dialog;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;
import com.delectable.mobile.ui.wineprofile.widget.CaptureNotesAdapter;
import com.delectable.mobile.ui.wineprofile.widget.WinePriceView;
import com.delectable.mobile.ui.wineprofile.widget.WineProfileCommentUnitRow;
import com.delectable.mobile.ui.winepurchase.activity.WineCheckoutActivity;
import com.delectable.mobile.util.Animate;
import com.delectable.mobile.util.CameraUtil;
import com.delectable.mobile.util.HideableActionBarScrollListener;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.delectable.mobile.util.MathUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.delectable.mobile.util.ViewUtil;
import com.melnykov.fab.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Inits request for full {@link BaseWine} information and {@link CaptureNote CaptureNotes} for that
 * {@code baseWine} using the {@code baseWineId} provided.
 */
public class WineProfileFragment extends BaseFragment implements
        WineBannerView.ActionsHandler, WineProfileCommentUnitRow.ActionsHandler,
        InfiniteScrollAdapter.ActionsHandler {

    public static final String TAG = WineProfileFragment.class.getSimpleName();

    private static final String WINE_PROFILE = "wineProfile";

    private static final String PHOTO_HASH = "photoHash";

    private static final String BASE_WINE_MINIMAL = "baseWineMinimal";

    protected static final String BASE_WINE_ID = "baseWineId";

    private static final String VINTAGE_ID = "vintageId";

    private static final int REQUEST_BUY_VINTAGE_DIALOG = 1;

    private static final int REQUEST_AGE_DIALOG = 2;

    private static final int CHOOSE_VINTAGE_DIALOG = 3;

    private static final String BASE_WINE_NOTES_REQ = "base_wine_notes_req";

    private static final String WINE_PROFILE_NOTES_REQ = "wine_profile_notes_req";

    private static final int STICKY_TOOLBAR_BLUR_RADIUS = 25;

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected CaptureController mCaptureController;

    @Inject
    protected BaseWineModel mBaseWineModel;

    @Inject
    protected WineSourceModel mWineSourceModel;

    @InjectView(R.id.wine_image)
    protected View mWineImageView;

    @InjectView(R.id.wine_banner_view)
    protected WineBannerView mBanner;

    @InjectView(R.id.price_view)
    protected WinePriceView mWinePriceView;

    @InjectView(R.id.ratings_all_years_container)
    protected View mRatingsContainer;

    @InjectView(R.id.varietal_region_container)
    protected View mVarietalRegionContainer;

    @InjectView(R.id.varietal_container)
    protected View mVarietalContainer;

    @InjectView(R.id.varietal_color_icon)
    protected ImageView mVarietalImageView;

    @InjectView(R.id.varietal_name)
    protected TextView mVarietalTextView;

    @InjectView(R.id.region_path_name)
    protected TextView mRegionPathTextView;

    @InjectView(R.id.all_ratings_average)
    protected TextView mAllRatingsAverageTextView;

    @InjectView(R.id.all_ratings_count)
    protected TextView mAllRatingsCountTextView;

    @InjectView(R.id.pro_ratings_average)
    protected TextView mProRatingsAverageTextView;

    @InjectView(R.id.pro_ratings_count)
    protected TextView mProRatingsCountTextView;

    @InjectView(R.id.empty_view_wine_profile)
    protected View mEmptyView;

    protected View mStickyToolbar;

    protected ImageView mStickyToolbarBackground;

    protected Toolbar mToolbar;

    protected ListView mListView;

    protected int mStickyToolbarHeight;

    protected int mToolbarScrollOffset;

    protected FloatingActionButton mCameraButton;

//    private MutableForegroundColorSpan mAlphaSpan;

//    private SpannableString mTitle;

    private CaptureNotesAdapter mAdapter = new CaptureNotesAdapter(this, this);

    private WineProfileMinimal mWineProfile;

    private WineProfileSubProfile mSelectedWineVintage;

    private PhotoHash mCapturePhotoHash;

    protected String mBaseWineId;

    private String mVintageId;

    private BaseWine mBaseWine;

    private CharSequence mAllYearsText;


    /**
     * Keep track of whether we're fetching for baseWine or wineProfile capture notes.
     */
    private Type mType = Type.BASE_WINE;

    /**
     * The id that we're fetching captureNotes with. Can be a baseWineId or a wineProfileId.
     */
    protected String mFetchingId;

    /**
     * If this is not null, then it means this fragment was spawned from Search Wines.
     */
    protected BaseWineMinimal mBaseWineMinimal;


    private Listing<CaptureNote, String> mCaptureNoteListing;

    /**
     * these maps are used to retain references to {@link CaptureNote} objects expecting updates to
     * their helpful status. This way, when the {@link MarkedCaptureHelpfulEvent} returns, we don't
     * have to iterate through our {@link CaptureNote} list to find the {@link CaptureNote} object
     * to modify.
     */
    private HashMap<String, CaptureNote> mCaptureNotesExpectingUpdate
            = new HashMap<String, CaptureNote>();

    private HashMap<String, Boolean> mCaptureNoteExpectedHelpfulStatus
            = new HashMap<String, Boolean>();

    private boolean mViewWineTracked = false;

    private boolean mFetching;

    //region Initializers
    /**
     * @param wineProfile      used to populate the producer and wine name.
     * @param capturePhotoHash used for the picture display. Usually, when a specific capture's
     *                         photo wants to be used instead of the {@code wineProfile}'s photo.
     *                         Pass in {@code null} to use {@code wineProfile}'s photo.
     */
    //TODO would be cleaner if CaptureDetail was passed in here, but it doesn't implement parcelable yet
    public static WineProfileFragment newInstance(WineProfileMinimal wineProfile,
            @Nullable PhotoHash capturePhotoHash) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(WINE_PROFILE, wineProfile);
        args.putParcelable(PHOTO_HASH, capturePhotoHash);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param baseWine         used to populate the producer and wine name.
     * @param capturePhotoHash used for the picture display. Usually, when a specific capture's
     *                         photo wants to be used instead of the {@code baseWine}'s. Pass in
     *                         {@code null} to use {@code baseWine}'s photo.
     */
    //used for Search Wines and User Profiles
    public static WineProfileFragment newInstance(BaseWineMinimal baseWine,
            @Nullable PhotoHash capturePhotoHash) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(BASE_WINE_MINIMAL, baseWine);
        args.putParcelable(PHOTO_HASH, capturePhotoHash);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@link WineProfileFragment} accessed by deep links will use this method to start up the
     * fragment. It can contain either just {@code baseWineId}, or both {@code baseWineId} and
     * {@code vintageId} (wine profile id). If the latter, then we must show {@link CaptureNote
     * CaptureNotes} for that {@link WineProfileMinimal}.
     *
     * @param baseWineId required
     * @param vintageId  optional
     */
    public static WineProfileFragment newInstance(String baseWineId, @Nullable String vintageId) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putString(BASE_WINE_ID, baseWineId);
        args.putString(VINTAGE_ID, vintageId);
        fragment.setArguments(args);
        return fragment;
    }
    //endregion Initializers

    //region LifeCycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args != null) {
            //spawned from Feed Fragment
            mWineProfile = args.getParcelable(WINE_PROFILE);
            mCapturePhotoHash = args.getParcelable(PHOTO_HASH);

            //spawned from Search or User Captures
            mBaseWineMinimal = args.getParcelable(BASE_WINE_MINIMAL);

            //spawned from deep links
            mBaseWineId = args.getString(BASE_WINE_ID);
            mVintageId = args.getString(VINTAGE_ID);
        }

        //ensure mBaseWineId is not null, so we can use it to retrieve BaseWine and CaptureNotes
        if (mWineProfile != null) {
            //spawned from Feed Fragment
            mBaseWineId = mWineProfile.getBaseWineId();
        } else if (mBaseWineMinimal != null) {
            //spawned from Search or User Captures
            mBaseWineId = mBaseWineMinimal.getId();
        }
        //if spawned from deeplinks, mBaseWineId wil be already populated

        //ensured that baseWineId is populated, set as our fetchingId
        mFetchingId = mBaseWineId;

        mAllYearsText = getString(R.string.wine_profile_all_years);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_profile, container, false);

        // prepare header view
        final View header = inflater.inflate(R.layout.wine_profile_header, null, false);
        ButterKnife.inject(this, header);

        // set wine banner height to match screen width
        Point screenSize = ViewUtil.getDisplayDimensions();
        mStickyToolbarHeight = screenSize.x;

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(screenSize.x,
                screenSize.x);
        mBanner.setLayoutParams(parms);

        // sticky toolbar
        mStickyToolbar = (RelativeLayout) view.findViewById(R.id.sticky_toolbar);
        ViewGroup.LayoutParams stickyToolbarParms = mStickyToolbar.getLayoutParams();
        stickyToolbarParms.height = mStickyToolbarHeight;
        mStickyToolbar.setLayoutParams(stickyToolbarParms);

        mStickyToolbarBackground = (ImageView) view.findViewById(R.id.sticky_toolbar_bg);
        ViewGroup.LayoutParams stickyToolbarBackgroundParms = mStickyToolbarBackground
                .getLayoutParams();
        stickyToolbarBackgroundParms.height = screenSize.x;
        mStickyToolbarBackground.setLayoutParams(stickyToolbarBackgroundParms);

        // set toolbar scroll offset to half the banner height
        mToolbarScrollOffset = mStickyToolbarHeight / 2;
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getBaseActivity().setSupportActionBar(mToolbar);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mAlphaSpan = new MutableForegroundColorSpan(0,
//                getResources().getColor(R.color.d_big_stone));
//        if (mWineProfile != null) {
//            mTitle = new SpannableString(
//                    mWineProfile.getProducerName() + " " + mWineProfile.getName());
//            mTitle.setSpan(mAlphaSpan, 0, mTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            getBaseActivity().getSupportActionBar().setSubtitle(mTitle);
//        }

        mBanner.setActionsHandler(this);
        updateBannerView();
        mBanner.updateVintage(mAllYearsText);

        updateVarietyRegionRatingView(mBaseWine);

        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.addHeaderView(header, null, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--; //headerview offsets position of listitems by 1
                launchCaptureDetails(mAdapter.getItem(position));
            }
        });
        mListView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged();

                        ViewTreeObserver obs = mListView.getViewTreeObserver();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            obs.removeOnGlobalLayoutListener(this);
                        } else {
                            obs.removeGlobalOnLayoutListener(this);
                        }
                    }
                });

        // empty state
        mEmptyView.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);

        final HideableActionBarScrollListener hideableActionBarScrollListener
                = new HideableActionBarScrollListener(this);

        // Setup Floating Camera Button
        mCameraButton = (FloatingActionButton) view.findViewById(R.id.camera_button);
        final FloatingActionButton.FabOnScrollListener fabOnScrollListener
                = new FloatingActionButton.FabOnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                onScrollChanged();
            }
        };
        mCameraButton.attachToListView(mListView, fabOnScrollListener);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraButtonClicked();
            }
        });

        mWinePriceView.setActionsCallback(new WinePriceView.WinePriceViewActionsCallback() {
            @Override
            public void onPriceCheckClicked(VintageWineInfo wineInfo) {
                fetchWineSource();
                mWinePriceView.showLoading();
            }

            @Override
            public void onPriceClicked(VintageWineInfo wineInfo) {
                showBuyVintageDialog();
            }

            @Override
            public void onSoldOutClicked(VintageWineInfo wineInfo) {
                showBuyVintageDialog();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBaseWineId != null && mBaseWine == null) {
            loadLocalBaseWineData(); //load from model to show something first
            mBaseWineController.fetchBaseWine(mBaseWineId);
        }

        if (mBaseWineId != null && mAdapter.getItems().isEmpty()) {
            loadCaptureNotesData(Type.BASE_WINE, mBaseWineId);
        }

        if (mBaseWineId != null) {
            mAnalytics.trackViewWineProfile();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BUY_VINTAGE_DIALOG) {
            String wineId = data.getStringExtra(BuyVintageDialog.EXTRAS_RESULT_WINE_ID);
            if (resultCode == BuyVintageDialog.RESULT_SWITCH_VINTAGE) {
                changeVintage(wineId);
            } else if (resultCode == BuyVintageDialog.RESULT_PURCHASE_WINE) {
                startWinePurchaseFlow(wineId);
            }
        }

        if (requestCode == REQUEST_AGE_DIALOG && resultCode == Over21Dialog.RESULT_OVER21) {
            startWinePurchaseFlow(mSelectedWineVintage.getId());
        }

        if (requestCode == CHOOSE_VINTAGE_DIALOG && resultCode == Activity.RESULT_OK) {
            Object wine = data.getParcelableExtra(ChooseVintageDialog.EXTRAS_RESULT_WINE);
            //when All Years is selected from dialog
            if (wine instanceof BaseWine) {
                BaseWine baseWine = (BaseWine) wine;
                changeVintage(baseWine.getId());
            }
            //when a vintage year is selected from the dialog
            if (wine instanceof WineProfileSubProfile) {
                WineProfileSubProfile wineProfile = (WineProfileSubProfile) wine;
                changeVintage(wineProfile.getId());
            }
        }
    }
    //endregion LifeCycle

    protected void onCameraButtonClicked() {
        launchWineCapture();
    }

    private void startWinePurchaseFlow(String wineId) {
        changeVintage(wineId);
        if (!UserInfo.isOver21()) {
            showOver21Dialog();
        } else if (UserInfo.isOver21()) {
            launchWineCheckout(wineId);
        }
    }

    private void changeVintage(String wineId) {

        String mOldFetchingId = mFetchingId;
        mFetchingId = wineId;


        if (mBaseWine.getId().equalsIgnoreCase(wineId)) {
            //first cover case where basewine is selected
            mType = Type.BASE_WINE;
            updateRatingsView(mBaseWine);
            //change back to default wineProfile to show pricing for it
            mSelectedWineVintage = mBaseWine.getDefaultWineProfile();
        } else {
            //if wineId is not basewine, then it must be a wineprofile
            mSelectedWineVintage = mBaseWine.getWineProfileByWineId(wineId);
            mType = Type.WINE_PROFILE;
            updateRatingsView(mSelectedWineVintage);
            loadPricingData();
        }


        //only clear list if fetching id has changed
        if (!mOldFetchingId.equals(mFetchingId)) {
            mAdapter.getItems().clear();

            //first query cache to see if we have something on hand already
            loadCaptureNotesData(mType, mFetchingId);
        }
        // Update the BannerView with new vintage
        updateBannerView();

        if (mType == Type.BASE_WINE) {
            //manually set vintage display to all years
            mBanner.updateVintage(mAllYearsText);
        }
    }

    //region Load Local Data

    /**
     * @param wineId baseWineId (for all wines) or wineProfileId (for specific vintage)
     */
    protected void loadCaptureNotesData(final Type type, final String wineId) {
        if (wineId == null) {
            return;
        }
        mFetching = true;
        new SafeAsyncTask<Listing<CaptureNote, String>>(this) {
            @Override
            protected Listing<CaptureNote, String> safeDoInBackground(Void[] params) {
                Log.d(TAG, "loadLocalData:doInBg");
                return CaptureNoteListingModel.getUserCaptures(wineId);
            }

            @Override
            protected void safeOnPostExecute(Listing<CaptureNote, String> listing) {
                Log.d(TAG, "loadLocalData:returnedFromCache");
                if (listing != null) {
                    Log.d(TAG, "loadLocalData:listingExists size: " + listing.getUpdates().size());
                    mCaptureNoteListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    fetchCaptureNotes(type, wineId, null, false);
                } else {
                    Log.d(TAG, "loadLocalData:success");
                    //simulate a pull to refresh if there are items
                    fetchCaptureNotes(type, wineId, mCaptureNoteListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void loadLocalBaseWineData() {
        loadLocalBaseWineData(false);
    }

    protected void loadLocalBaseWineData(boolean baseWineHasChanged) {
        //retrieve full base wine information
        mBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);
        if (mBaseWine == null) {
            return;
        }

        //if spawned from a Feed Fragment, mCapturePhotoHash won't be null, we don't want to replace that capture photo with the basewine's photo
        if (mCapturePhotoHash == null) {
            mCapturePhotoHash = mBaseWine.getPhoto();
        }

        if (mSelectedWineVintage == null || baseWineHasChanged) {
            mSelectedWineVintage = mBaseWine.getDefaultWineProfile();
        }
        loadPricingData();
        updateVarietyRegionRatingView(mBaseWine);
    }

    private void loadPricingData() {
        if (mSelectedWineVintage == null) {
            return;
        }
        WineProfileSubProfile wineWithPrice = mWineSourceModel
                .getMinWineWithPrice(mSelectedWineVintage.getId());
        if (wineWithPrice != null) {
            mSelectedWineVintage = wineWithPrice;
        }
        mWinePriceView.updateWithPriceInfo(new VintageWineInfo(mSelectedWineVintage));
    }
    //endregion

    //region EventBus Events
    public void onEventMainThread(UpdatedBaseWineEvent event) {
        if (mBaseWineId != null && !mBaseWineId.equals(event.getBaseWineId())) {
            return;
        }

        if (event.isSuccessful()) {
            loadLocalBaseWineData();
        } else {
            showToastError(event.getErrorMessage());
        }
    }

    public void onEventMainThread(FetchedWineSourceEvent event) {
        if (mSelectedWineVintage == null || !mSelectedWineVintage.getId()
                .equals(event.getWineId())) {
            return;
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        loadPricingData();
    }

    public void onEventMainThread(UpdatedListingEvent<CaptureNote, String> event) {
        if (!event.getRequestId().equals(WINE_PROFILE_NOTES_REQ)) {
            if (!event.getRequestId().equals(BASE_WINE_NOTES_REQ)) {
                return;
            }
        }

        mFetching = false;

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        if (event.getListing() != null) {
            mCaptureNoteListing = event.getListing();
            mAdapter.setItems(mCaptureNoteListing.getUpdates());
            mAdapter.notifyDataSetChanged();
        }
        //if listing is null, means there are no updates
        //we don't let listing get assigned null

        // empty state
        mEmptyView.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);

    }

    public void onEventMainThread(MarkedCaptureHelpfulEvent event) {
        String captureId = event.getCaptureId();
        CaptureNote captureNote = mCaptureNotesExpectingUpdate.remove(captureId);
        Boolean expectedHelpful = mCaptureNoteExpectedHelpfulStatus.remove(captureId);
        if (captureNote == null) {
            return; //capturenote didn't exist in the hashmap, means this event wasn't called from this fragment
        }

        //the captureNote object's helpfuling account ids list was modified when the user pressed helpful,
        // if the event errored we need to revert the list back to it's original form
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());

            //revert helpfulness, bc event failed
            String userId = UserInfo.getUserId(getActivity());
            if (expectedHelpful) {
                captureNote.unmarkHelpful(userId);
            } else {
                captureNote.markHelpful(userId);
            }
        }
        //will reset following toggle button back to original setting if error
        mAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Fetch Remote Data

    private void fetchWineSource() {
        mBaseWineController.fetchWineSource(mSelectedWineVintage.getId(), null);
    }

    /**
     * @param idType Whether to load captures notes for a base wine or a wine profile.
     */
    private void fetchCaptureNotes(Type idType, String id, Listing<CaptureNote, String> listing,
            Boolean isPullToRefresh) {
        if (id == null) {
            return;
        }
        mFetching = true;
        if (idType == Type.BASE_WINE) {
            mCaptureController.fetchCaptureNotes(BASE_WINE_NOTES_REQ, id, null, listing, null,
                    isPullToRefresh);
        }
        if (idType == Type.WINE_PROFILE) {
            mCaptureController
                    .fetchCaptureNotes(WINE_PROFILE_NOTES_REQ, null, id, listing, null,
                            isPullToRefresh);
        }
    }
    //endregion

    private void onScrollChanged() {
        View v = mListView.getChildAt(0);
        int top = (v == null ? 0 : v.getTop());
        int bannerHeight = mBanner.getHeight();
        boolean isHeaderVisible = mListView.getFirstVisiblePosition() == 0;

        if (isHeaderVisible) {

            int toolbarHeight = mToolbar.getHeight();

            // sticky toolbar
            int minTranslation = -mStickyToolbarHeight + toolbarHeight;
            int stickyToolbarTranslation = MathUtil
                    .clamp(top, minTranslation, 0);
            mStickyToolbar.setTranslationY(stickyToolbarTranslation);
            float alphaRatio = MathUtil
                    .clamp(stickyToolbarTranslation / (float) (-mStickyToolbarHeight
                            + toolbarHeight), 0f, 1f);
            float stickyToolbarAlpha = MathUtil.clamp(4f * alphaRatio - 2f, 0f, 1f);
            mStickyToolbar.setAlpha(stickyToolbarAlpha);

            // parallax effect on wine image
            mWineImageView.setTranslationY(-stickyToolbarTranslation / 2f);
            mStickyToolbarBackground.setTranslationY(-stickyToolbarTranslation / 2f);

            // elevate sticky toolbar once it docks
            Animate.elevate(mToolbar, top < minTranslation ? Animate.ELEVATION * 2 : 0);
            Animate.elevate(mStickyToolbar, top < minTranslation ? Animate.ELEVATION * 2 : 0);

            // drag toolbar off the screen when reaching the bottom of the header
//            int toolbarDragOffset = bannerHeight - mToolbarScrollOffset;
//            int toolbarTranslation = MathUtil.clamp(top + toolbarDragOffset, -toolbarHeight, 0);
//            mToolbar.setTranslationY(toolbarTranslation);

//            Log.d(TAG, "####### top=" + top + " / stickyTranslation=" + stickyToolbarTranslation
//                    + " / stickyHeight=" + mStickyToolbarHeight + " / toolbarHeight=" + toolbarHeight + " / alpha=" + stickyToolbarAlpha);

            // animated spannables
//            // title
//            ObjectAnimator titleAnimator = new ObjectAnimator()
//                    .ofInt(mAlphaSpan, MutableForegroundColorSpan.ALPHA_PROPERTY,
//                            isToolbarSolid ? 255 : 0, isToolbarSolid ? 0 : 255);
//            titleAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
//            titleAnimator.setInterpolator(new DecelerateInterpolator());
//            titleAnimator.setEvaluator(new ArgbEvaluator());
//            titleAnimator
//                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            setActionBarSubtitle(mTitle);
//                        }
//                    });
//            titleAnimator.start();
//            // background
//            int solidColor = getResources().getColor(R.color.d_off_white);
//            int translucentColor = getResources()
//                    .getColor(android.R.color.transparent);
//            final ValueAnimator bgAnimator = ValueAnimator.ofObject(
//                    new ArgbEvaluator(),
//                    isToolbarSolid ? solidColor : translucentColor,
//                    isToolbarSolid ? translucentColor : solidColor);
//            bgAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
//            bgAnimator.setInterpolator(new DecelerateInterpolator());
//            bgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                    getActionBarToolbar().setBackgroundColor(
//                            (Integer) bgAnimator.getAnimatedValue());
//                }
//            });
//            bgAnimator.start();
        }
    }

    private void updateVarietyRegionRatingView(BaseWine baseWine) {
        if (baseWine == null) {
            return;
        }
        //varietal info
        if (baseWine.getVarietalComposition().size() == 0) {
            //beers don't have varietal composition
            mVarietalContainer.setVisibility(View.GONE);
        } else {
            GradientDrawable varietalDrawable = (GradientDrawable) mVarietalImageView.getDrawable();
            String hexColor = baseWine.getVarietalComposition().get(0).getColor();
            varietalDrawable.setColor(Color.parseColor(hexColor));

            //combine varietal names if there's more than one
            ArrayList<String> varietalNames = new ArrayList<String>();
            for (VarietalsHash varietal : baseWine.getVarietalComposition()) {
                varietalNames.add(varietal.getName());
            }
            TextUtils.join(", ", varietalNames);
            String varietalDisplayText = StringUtils.join(varietalNames, ", ");
            mVarietalTextView.setText(varietalDisplayText);
        }

        //regional info
        String regionPath = baseWine.getRegionPathDisplayText(getActivity());
        mRegionPathTextView.setText(regionPath);

        mVarietalRegionContainer.setVisibility(View.VISIBLE);

        updateRatingsView(baseWine);

        if (!mViewWineTracked) {
            KahunaUtil.trackViewWine(baseWine.getId(), baseWine.getName());
            mViewWineTracked = true;
        }
    }

    private void updateRatingsView(Ratingsable ratingsable) {
        //rating avg
        double allAvg = ratingsable.getRatingsSummary().getAllAvg();
        double proAvg = ratingsable.getRatingsSummary().getProAvg();
        mAllRatingsAverageTextView.setText(Rating.forDisplay(getActivity(), allAvg));
        mProRatingsAverageTextView.setText(Rating.forDisplay(getActivity(), proAvg));

//        mAllRatingsAverageTextView.setText(TextUtil.makeRatingDisplayText(getActivity(), allAvg));
//        mProRatingsAverageTextView.setText(TextUtil.makeRatingDisplayText(getActivity(), proAvg));

        //ratings count
        int allCount = ratingsable.getRatingsSummary().getAllCount();
        int proCount = ratingsable.getRatingsSummary().getProCount();
        String allRatingsCount = getResources().getQuantityString(
                R.plurals.wine_profile_ratings_count, allCount, allCount);
        String proRatingsCount = getResources()
                .getQuantityString(R.plurals.wine_profile_pro_ratings_count, proCount, proCount);
        mAllRatingsCountTextView.setText(allRatingsCount);
        mProRatingsCountTextView.setText(proRatingsCount);

        mRatingsContainer.setVisibility(View.VISIBLE);
    }

    protected void updateBannerView() {
        updateBannerView(null);
    }

    /**
     * The WineBannerView can be set with different types of data depending from where this fragment
     * was spawned.
     */
    protected void updateBannerView(final Bitmap previewImage) {
        if (mWineProfile != null) {
            //spawned from Feed Fragment
            mBanner.updateData(mWineProfile, mCapturePhotoHash, false);
        } else if (mBaseWineMinimal != null) {
            //spawned from Search Wines, User Captures or Instant Flow
            if (previewImage != null) {
                mBanner.updateData(mBaseWineMinimal, previewImage);
            } else {
                mBanner.updateData(mBaseWineMinimal, mCapturePhotoHash);
            }
        } else if (mBaseWine != null) {
            //called after BaseWine is successfully fetched
            mBanner.updateData(mBaseWine, mCapturePhotoHash);
        }

        if (mSelectedWineVintage != null) {
            // When we select a new Vintage
            mBanner.updateVintage(mSelectedWineVintage.getVintage());
        } else {
            mBanner.updateVintage(mAllYearsText);
        }

        // sticky toolbar background image
        if (previewImage != null) {
            mStickyToolbarBackground.setImageBitmap(previewImage);
            CameraUtil.blurImageAsync(previewImage,
                    STICKY_TOOLBAR_BLUR_RADIUS, this, new SafeAsyncTask.Callback<Bitmap>() {
                        @Override
                        public void onResult(Bitmap bitmap) {
                            if (bitmap != null) {
                                mStickyToolbarBackground.setImageBitmap(bitmap);
                            }
                        }
                    });
        } else if (mCapturePhotoHash != null) {
            ImageLoaderUtil.loadBlurredImageIntoView(getActivity(), mCapturePhotoHash.get450Plus(),
                    mStickyToolbarBackground, STICKY_TOOLBAR_BLUR_RADIUS);
        }
    }

    private void markCaptureAsHelpful(CaptureNote captureNote, boolean markHelpful) {
        mCaptureNotesExpectingUpdate.put(captureNote.getId(), captureNote);
        mCaptureNoteExpectedHelpfulStatus.put(captureNote.getId(), markHelpful);

        mCaptureController.markCaptureHelpful(captureNote, markHelpful);
    }

    @Override
    public void toggleHelpful(CaptureNote captureNote, boolean markHelpful) {
        markCaptureAsHelpful(captureNote, markHelpful);
    }

    private void showChooseVintageDialog() {
        if (mBaseWine == null) {
            return;
        }
        ChooseVintageDialog dialog = ChooseVintageDialog.newInstance(mBaseWine);
        dialog.setTargetFragment(this, CHOOSE_VINTAGE_DIALOG); //callback goes to onActivityResult
        dialog.show(getFragmentManager(), ChooseVintageDialog.TAG);
    }

    private void showBuyVintageDialog() {
        BuyVintageDialog dialog = BuyVintageDialog.newInstance(mBaseWineId);
        dialog.setTargetFragment(WineProfileFragment.this,
                REQUEST_BUY_VINTAGE_DIALOG); //callback goes to onActivityResult
        dialog.show(getFragmentManager(), "dialog");
    }

    private void launchWineCheckout(String wineId) {
        Intent intent = WineCheckoutActivity.newIntent(getActivity(), wineId);
        startActivity(intent);
    }

    private void showOver21Dialog() {
        Over21Dialog dialog = Over21Dialog.newInstance();
        dialog.setTargetFragment(WineProfileFragment.this, REQUEST_AGE_DIALOG);
        dialog.show(getFragmentManager(), "dialog");
    }

    public void launchCaptureDetails(CaptureNote captureNote) {
        Intent intent = new Intent();
        intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                captureNote.getId());
        intent.setClass(getActivity(), CaptureDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onVintageClick() {
        showChooseVintageDialog();
    }

    @Override
    public void onEditBaseWineClicked() {
        // Override in WineProfileInstantFragment
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:notFetching");

        if (mCaptureNoteListing == null) {
            //reached end of list/there are no items, we do nothing.
            //in theory, this should never be null because the getMore check below should stop it from loading
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:captureListingExists");

        if (mCaptureNoteListing.getMore()) {
            mFetching = true;
            //mNoFollowersText.setVisibility(View.GONE);
            fetchCaptureNotes(mType, mFetchingId, mCaptureNoteListing, false);
            Log.d(TAG, "shouldLoadNextPage:moreTrue");
        }
    }

    protected static enum Type {
        BASE_WINE, WINE_PROFILE;
    }
}
