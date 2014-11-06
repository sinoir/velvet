package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.events.wines.UpdatedBaseWineEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.ui.wineprofile.widget.WineProfilesAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ChooseVintageDialog extends DialogFragment {

    public static final String WINE = "WINE";

    private static final String BASE_WINE_ID = "BASE_WINE_ID";

    private static final String TAG = ChooseVintageDialog.class.getSimpleName();

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected BaseWineModel mBaseWineModel;

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    private WineProfilesAdapter mAdapter = new WineProfilesAdapter();

    private BaseWine mBaseWine;

    private String mBaseWineId;

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

        mListView.setAdapter(mAdapter);

        // TODO: Replace with Click Listeners within the Rows
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //call back to implementing class
                Intent intent = new Intent();
                intent.putExtra(WINE,
                        mAdapter.getItem(position)); //can be a BaseWine or WineProfile
                getTargetFragment()
                        .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        loadBaseWineData();

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

    private void loadBaseWineData() {
        //retrieve full base wine information
        mBaseWine = mBaseWineModel.getBaseWine(mBaseWineId);

        updateUI();
    }

    private void updateUI() {
        if (mBaseWine == null) {
            return;
        }
        mProducerName.setText(mBaseWine.getProducerName());
        mWineName.setText(mBaseWine.getName());

        mAdapter.setBaseWine(mBaseWine);
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(UpdatedBaseWineEvent event) {
        if (!mBaseWineId.equals(event.getBaseWineId())) {
            return;
        }

        if (event.isSuccessful()) {
            loadBaseWineData();
        }
    }
}
