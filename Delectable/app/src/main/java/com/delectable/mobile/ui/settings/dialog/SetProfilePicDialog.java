package com.delectable.mobile.ui.settings.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.settings.widget.SetProfilePicRowAdapter;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SetProfilePicDialog extends DialogFragment {

    private static final String TAG = SetProfilePicDialog.class.getSimpleName();

    private static final String LIST_ITEMS = "BASE_WINE";

    private SetProfilePicRowAdapter mAdapter;

    private Callback mCallback;

    public static SetProfilePicDialog newInstance(ArrayList<String> listItems) {
        SetProfilePicDialog f = new SetProfilePicDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(LIST_ITEMS, listItems);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

        if (getArguments() != null) {
            ArrayList<String> listItems = getArguments().getStringArrayList(LIST_ITEMS);
            mAdapter = new SetProfilePicRowAdapter(listItems);
            return;
        }

        throw new RuntimeException(TAG + " needs to be initialized with list items");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_listview, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(R.string.settings_set_profile_pic_dialog_title);
        ListView listview = (ListView) view.findViewById(R.id.list_view);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //call back to implementing class
                if (mCallback != null) {
                    mCallback.onDialogItemClick(position);
                }
                dismiss();
            }
        });

        return view;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public static interface Callback {

        public void onDialogItemClick(int position);
    }

}
