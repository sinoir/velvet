package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ConfirmationDialogFragment extends BaseDialogFragment {

    private static final String TAG = ConfirmationDialogFragment.class.getSimpleName();

    @InjectView(R.id.confirm_text)
    protected TextView mConfirmTextView;

    public static ConfirmationDialogFragment newInstance() {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, null, false);
        ButterKnife.inject(this, view);

        formatConfirmText();
        return view;
    }

    /**
     * Adds the Email with proper color / click state
     */
    private void formatConfirmText() {
        String confirmationEmail = getString(R.string.order_confirmation_email);
        String confirmationTextWithEmail = getString(R.string.order_confirmation_text,
                confirmationEmail);
        int startPos = confirmationTextWithEmail.indexOf(confirmationEmail);
        int endPos = startPos + confirmationEmail.length();
        Log.d(TAG, "Start Position: " + startPos);
        Log.d(TAG, "End Position: " + endPos);
        Spannable spannableString = new SpannableString(confirmationTextWithEmail);
        spannableString.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.d_chestnut)), startPos,
                endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mConfirmTextView.setText(spannableString);
    }

    @OnClick(R.id.ok_button)
    protected void onOkClicked() {
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}
