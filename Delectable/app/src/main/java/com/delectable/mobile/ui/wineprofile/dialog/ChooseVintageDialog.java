package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.ui.wineprofile.widget.WineProfilesAdapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseVintageDialog extends DialogFragment {

    public static final String WINE_PROFILE = "WINE_PROFILE";

    private static final String WINE_PROFILES = "WINE_PROFILES";

    private static final String TAG = ChooseVintageDialog.class.getSimpleName();

    private ArrayList<WineProfile> mWineProfiles = new ArrayList<WineProfile>();

    private WineProfilesAdapter mAdapter = new WineProfilesAdapter(mWineProfiles);


    public static ChooseVintageDialog newInstance(ArrayList<WineProfile> wineProfiles) {
        ChooseVintageDialog f = new ChooseVintageDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(WINE_PROFILES, wineProfiles);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        if (getArguments() != null) {
            ArrayList<WineProfile> wineProfiles = getArguments().getParcelableArrayList(WINE_PROFILES);
            mWineProfiles.clear();
            mWineProfiles.addAll(wineProfiles);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_vintage, container, false);
        ListView listview = (ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //call back to implementing class
                Intent intent = new Intent();
                intent.putExtra(WINE_PROFILE, mAdapter.getItem(position));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        return view;
    }

}
