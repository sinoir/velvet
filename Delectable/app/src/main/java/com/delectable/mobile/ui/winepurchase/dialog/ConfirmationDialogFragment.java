package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.dialog.BaseDialogFragment;
import com.delectable.mobile.ui.common.widget.TouchableSpan;
import com.delectable.mobile.util.Animate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
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

    @InjectView(R.id.confirmation_icon)
    protected View mConfirmIcon;

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

    @Override
    public void onResume() {
        super.onResume();
        Animate.grow(mConfirmIcon, 400);
    }

    /**
     * Adds the Email with proper color / click state
     */
    private void formatConfirmText() {
        final String confirmationEmail = getString(R.string.order_confirmation_email);
        String confirmationTextWithEmail = getString(R.string.order_confirmation_text,
                confirmationEmail);
        int startPos = confirmationTextWithEmail.indexOf(confirmationEmail);
        int endPos = startPos + confirmationEmail.length();
        mConfirmTextView.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable spannableString = new SpannableString(confirmationTextWithEmail);

        spannableString.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.d_chestnut)),
                startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(
                new TouchableSpan(getResources().getColor(R.color.d_chestnut_pressed)) {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Email Clicked? ");
                        Intent send = new Intent(Intent.ACTION_SENDTO);
                        String uriText = "mailto:" + Uri.encode(confirmationEmail);
                        Uri uri = Uri.parse(uriText);

                        send.setData(uri);
                        startActivity(Intent.createChooser(send,
                                getString(R.string.settings_contact_email_intent_dialog_title)));
                    }
                },
                startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mConfirmTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @OnClick(R.id.ok_button)
    protected void onOkClicked() {
        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}
