package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;
import com.delectable.mobile.ui.wineprofile.widget.WineProfilesVintageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class ChooseVintageDialog extends BaseDialogFragment {

    public static final String TAG = ChooseVintageDialog.class.getSimpleName();

    public static final String EXTRAS_RESULT_WINE = "EXTRAS_RESULT_WINE";

    private static final String BASE_WINE = "BASE_WINE";

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    private WineProfilesVintageAdapter mAdapter = new WineProfilesVintageAdapter();

    private BaseWine mBaseWine;

    public static ChooseVintageDialog newInstance(BaseWine baseWine) {
        ChooseVintageDialog f = new ChooseVintageDialog();
        Bundle args = new Bundle();
        args.putParcelable(BASE_WINE, baseWine);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException(TAG + "needs to be instantiated with a BaseWine");
        }
        mBaseWine = getArguments().getParcelable(BASE_WINE);
        if (mBaseWine == null) {
            dismiss();
        }
        mAdapter.setBaseWine(mBaseWine);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_vintage, container, false);
        ButterKnife.inject(this, view);
        if (mBaseWine.getProducerName() != null) {
            mProducerName.setText(mBaseWine.getProducerName().toLowerCase());
        }
        mWineName.setText(mBaseWine.getName());
        mListView.setAdapter(mAdapter);
        return view;
    }

    @OnItemClick(R.id.list_view)
    public void onItemClick(int position) {
        //call back to implementing class
        Parcelable wine = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_RESULT_WINE, wine);
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    intent);
        }
        dismiss();
    }
}
