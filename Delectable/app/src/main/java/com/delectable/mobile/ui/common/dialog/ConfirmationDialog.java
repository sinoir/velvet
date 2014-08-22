package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class ConfirmationDialog extends DialogFragment {

    private static final String PARAM_TITLE = "param_title";

    private static final String PARAM_MESSAGE = "param_message";

    private static final String PARAM_OK_TEXT = "param_ok_text";

    private String mTitle;

    private String mMessage;

    private String mOkText;


    public static ConfirmationDialog newInstance(String title, String message,
            String okText, Fragment targetFragment, int requestCode) {
        ConfirmationDialog f = new ConfirmationDialog();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putString(PARAM_MESSAGE, message);
        args.putString(PARAM_OK_TEXT, okText);
        f.setArguments(args);

        f.setTargetFragment(targetFragment, requestCode);

        return f;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mTitle = args.getString(PARAM_TITLE);
        mMessage = args.getString(PARAM_MESSAGE);
        mOkText = args.getString(PARAM_OK_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle)
                .setPositiveButton(mOkText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    Activity.RESULT_OK, null);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    Activity.RESULT_CANCELED, null);
                        }
                    }
                });
        return builder.create();
    }
}
