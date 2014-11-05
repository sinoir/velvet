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

import de.greenrobot.event.EventBus;

public class ChooseVintageDialog extends DialogFragment {

    public static final String WINE = "WINE";

    private static final String BASE_WINE_ID = "BASE_WINE_ID";

    private static final String TAG = ChooseVintageDialog.class.getSimpleName();

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected BaseWineModel mBaseWineModel;

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
        View view = inflater.inflate(R.layout.dialog_listview, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(R.string.choose_vintage_dialog_title);
        ListView listview = (ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        if (mBaseWine == null) {
            return;
        }
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
