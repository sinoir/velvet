package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//TODO should build a title into this as well if needed later on
public class ConfirmationNoTitleDialog extends DialogFragment {

    private static final String TAG = ConfirmationNoTitleDialog.class.getSimpleName();

    private static final String MESSAGE = "MESSAGE";

    private static final String POSITIVE_LABEL = "POSITIVE_LABEL";

    private static final String NEGATIVE_LABEL = "NEGATIVE_LABEL";


    @InjectView(R.id.message)
    protected FontTextView mMessageTextView;

    @InjectView(R.id.positive_text)
    protected FontTextView mPositiveTextView;

    @InjectView(R.id.negative_text)
    protected FontTextView mNegativeTextView;

    private String mMessage;

    private String mPositiveButtonLabel;

    private String mNegativeButtonLabel;

    /**
     * Uses "OK" and "cancel" as positive and negative button　labels.
     * @param message
     * @return
     */
    public static ConfirmationNoTitleDialog newInstance(String message) {
        return newInstance(message, null, null);
    }

    public static ConfirmationNoTitleDialog newInstance(String message, String positiveButton,
            String negativeButton) {
        ConfirmationNoTitleDialog f = new ConfirmationNoTitleDialog();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putString(POSITIVE_LABEL, positiveButton);
        args.putString(NEGATIVE_LABEL, negativeButton);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DelectableTheme_Dialog);

        if (getArguments() == null) {
            throw new RuntimeException(TAG + " needs to be initialized with a message.");
        }

        mMessage = getArguments().getString(MESSAGE);
        mPositiveButtonLabel = getArguments().getString(POSITIVE_LABEL);
        mNegativeButtonLabel = getArguments().getString(NEGATIVE_LABEL);

        if (mMessage == null) {
            throw new RuntimeException(TAG + " needs to be initialized with a message.");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirmation_no_title, container, false);
        ButterKnife.inject(this, view);

        mMessageTextView.setText(mMessage);
        if (mPositiveButtonLabel != null && !mPositiveButtonLabel.trim().equals("")) {
            mPositiveTextView.setText(mPositiveButtonLabel);
        }
        if (mNegativeButtonLabel != null && !mNegativeButtonLabel.trim().equals("")) {
            mNegativeTextView.setText(mNegativeButtonLabel);
        }
        return view;
    }


    @OnClick(R.id.negative_text)
    protected void onNegativeTextClick() {
        if (getTargetFragment() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
        dismiss();
    }

    @OnClick(R.id.positive_text)
    protected void onPositiveTextClick() {
        if (getTargetFragment() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
        dismiss();
    }


}

