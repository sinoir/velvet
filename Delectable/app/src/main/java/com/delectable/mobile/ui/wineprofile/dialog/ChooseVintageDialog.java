package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
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

public class ChooseVintageDialog extends DialogFragment {

    public static final String WINE = "WINE";

    private static final String sArgsBaseWine = "BASE_WINE";

    private static final String TAG = ChooseVintageDialog.class.getSimpleName();

    private WineProfilesAdapter mAdapter = new WineProfilesAdapter();


    public static ChooseVintageDialog newInstance(BaseWine baseWine) {
        ChooseVintageDialog f = new ChooseVintageDialog();
        Bundle args = new Bundle();
        args.putParcelable(sArgsBaseWine, baseWine);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        if (getArguments() != null) {
            BaseWine baseWine = getArguments().getParcelable(sArgsBaseWine);
            mAdapter.setBaseWine(baseWine);
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
                intent.putExtra(WINE,
                        mAdapter.getItem(position)); //can be a BaseWine or WineProfile
                getTargetFragment()
                        .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        return view;
    }

}
