package com.delectable.mobile.ui.versionupgrade.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VersionUpgradeDialog extends BaseDialogFragment {

    public static final String TAG = VersionUpgradeDialog.class.getSimpleName();

    private ActionsHandler mHandler;

    public static VersionUpgradeDialog newInstance() {
        VersionUpgradeDialog f = new VersionUpgradeDialog();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_version_upgrade, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    public void setActionsHandler(ActionsHandler handler) {
        mHandler = handler;
    }


    @OnClick(R.id.cancel_textview)
    protected void onCancelClick() {
        if (getTargetFragment() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
        if (mHandler != null) {
            mHandler.onCancelClick();
        }
        dismiss();
    }

    @OnClick(R.id.upgrade_textview)
    protected void onUpgradeClick() {
        if (getTargetFragment() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
        if (mHandler != null) {
            mHandler.onUpgradeClick();
        }
        dismiss();
        //TODO connect to reset password endpoint, not live yet when this was made
    }

    //Handler is necessary if calling this dialog from an activity
    public interface ActionsHandler {

        public void onCancelClick();

        public void onUpgradeClick();

    }
}
