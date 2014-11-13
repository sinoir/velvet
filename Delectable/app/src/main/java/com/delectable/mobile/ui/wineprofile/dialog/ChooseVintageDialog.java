package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.WineProfileSubProfile;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;
import com.delectable.mobile.ui.wineprofile.widget.WinePriceView;
import com.delectable.mobile.ui.wineprofile.widget.WineProfilesAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ChooseVintageDialog extends DialogFragment
        implements WinePriceView.WinePriceViewActionsCallback {

    public static final String EXTRAS_RESULT_WINE_ID = "EXTRAS_RESULT_WINE_ID";

    public static final int RESULT_SWITCH_VINTAGE = 1000;

    public static final int RESULT_PURCHASE_WINE = 2000;

    private static final String BASE_WINE_ID = "BASE_WINE_ID";

    private static final String TAG = ChooseVintageDialog.class.getSimpleName();

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected BaseWineModel mBaseWineModel;

    @Inject
    protected WineSourceModel mWineSourceModel;

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    private WineProfilesAdapter mAdapter;

    private BaseWine mBaseWine;

    private String mBaseWineId;

    private ArrayList<VintageWineInfo> mVintageWineInfos;

    public static ChooseVintageDialog newInstance(String baseWineId) {
        ChooseVintageDialog f = new ChooseVintageDialog();
        Bundle args = new Bundle();
        args.putString(BASE_WINE_ID, baseWineId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        if (getArguments() != null) {
            mBaseWineId = getArguments().getString(BASE_WINE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_vintage, container, false);
        ButterKnife.inject(this, view);

        mAdapter = new WineProfilesAdapter(this);

        mListView.setAdapter(mAdapter);

        // TODO: Replace with Click Listeners within the Rows
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //call back to implementing class
                String wineId = mAdapter.getItem(position).getWineId();
                Intent intent = new Intent();
                intent.putExtra(EXTRAS_RESULT_WINE_ID, wineId);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        RESULT_SWITCH_VINTAGE,
                        intent);
                dismiss();
            }
        });

        mVintageWineInfos = new ArrayList<VintageWineInfo>();
        mAdapter.setData(mVintageWineInfos);

        loadData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
            // no-op
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    //region LoadLocal Data
    private void loadData() {
        //retrieve full base wine information
        mBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);

        if (mVintageWineInfos.size() == 0) {
            loadPricingData();
        } else {
            updatePricingData();
        }

        updateUI();
    }

    /**
     * Initial loading of pricing data
     */
    private void loadPricingData() {
        for (WineProfileSubProfile wineProfile : mBaseWine.getWineProfiles()) {
            WineProfileSubProfile wineWithPrice = mWineSourceModel
                    .getMinWineWithPrice(wineProfile.getId());
            if (wineWithPrice != null) {
                mVintageWineInfos.add(new VintageWineInfo(wineWithPrice));
            } else {
                mVintageWineInfos.add(new VintageWineInfo(wineProfile));
            }
        }
    }
    //endregion

    //region Update UI methods

    /**
     * Update Vintage ViewModel data with new pricing info
     */
    private void updatePricingData() {
        // Reloads the data via reference from the cache
        for (VintageWineInfo wineInfo : mVintageWineInfos) {
            WineProfileSubProfile wineWithPrice = mWineSourceModel.getMinWineWithPrice(
                    wineInfo.getWineProfileMinimal().getId());
            if (wineWithPrice != null) {
                wineInfo.updateWineWithPrice(wineWithPrice);
                if (wineInfo.isLoading()) {
                    wineInfo.setLoading(false);
                }
            }
        }
    }

    private void updateUI() {
        if (mBaseWine == null) {
            return;
        }
        mProducerName.setText(mBaseWine.getProducerName());
        mWineName.setText(mBaseWine.getName());

        mAdapter.notifyDataSetChanged();
    }
    //endregion

    //region EventBus methods
    public void onEventMainThread(UpdatedBaseWineEvent event) {
        if (!mBaseWineId.equals(event.getBaseWineId())) {
            return;
        }

        if (event.isSuccessful()) {
            loadData();
        }
    }

    public void onEventMainThread(FetchedWineSourceEvent event) {
        if (!event.isSuccessful() && getActivity() != null) {
            Toast.makeText(getActivity(), event.getErrorMessage(), Toast.LENGTH_LONG).show();
            // Stop Loading failed WineInfo
            for (VintageWineInfo info : mVintageWineInfos) {
                if (event.getWineId().equalsIgnoreCase(info.getWineProfileMinimal().getId())) {
                    info.setLoading(false);
                    break;
                }
            }
        }
        // Reload data to update UI States
        loadData();
    }
    //endregion

    //region Fetch Remote data
    private void fetchPriceForWine(String wineId) {
        mBaseWineController.fetchWineSource(wineId, null);
    }
    //endregion

    //region WinePriceViewActionsCallback methods
    @Override
    public void onPriceCheckClicked(VintageWineInfo wineInfo) {
        wineInfo.setLoading(true);
        fetchPriceForWine(wineInfo.getWineProfileMinimal().getId());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPriceClicked(VintageWineInfo wineInfo) {
        //call back to implementing class
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_RESULT_WINE_ID,
                wineInfo.getWineProfileMinimal().getId()); //can be a BaseWine or WineProfile
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_PURCHASE_WINE, intent);
        dismiss();
    }

    @Override
    public void onSoldOutClicked(VintageWineInfo wineInfo) {
        // no-op
    }
    //endregion
}
