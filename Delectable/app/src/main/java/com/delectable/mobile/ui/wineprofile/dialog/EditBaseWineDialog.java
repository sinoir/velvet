package com.delectable.mobile.ui.wineprofile.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;
import com.delectable.mobile.ui.wineprofile.widget.BaseWineAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class EditBaseWineDialog extends BaseDialogFragment {

    public static final String TAG = EditBaseWineDialog.class.getSimpleName();

    public static final String EXTRAS_RESULT_WINE = "EXTRAS_RESULT_WINE";

    private static final String MATCHES = "MATCHES";

    @InjectView(R.id.list_view)
    protected ListView mListView;

    private BaseWineAdapter mAdapter = new BaseWineAdapter();

    private ArrayList<BaseWine> mMatches;

    public static EditBaseWineDialog newInstance(ArrayList<BaseWine> matches) {
        EditBaseWineDialog f = new EditBaseWineDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MATCHES, matches);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new RuntimeException(
                    TAG + "needs to be instantiated with an ArrayList<BaseWine>");
        }
        mMatches = getArguments().getParcelableArrayList(MATCHES);
        mAdapter.setMatches(mMatches);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_base_wine, container, false);
        ButterKnife.inject(this, view);
        View footerView = inflater.inflate(R.layout.edit_base_wine_footer, container, false);
        mListView.addFooterView(footerView, null, true);
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
