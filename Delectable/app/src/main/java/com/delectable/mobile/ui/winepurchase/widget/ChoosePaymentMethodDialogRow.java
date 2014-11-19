package com.delectable.mobile.ui.winepurchase.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.PaymentMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChoosePaymentMethodDialogRow extends RelativeLayout {

    private static final String TAG = ChoosePaymentMethodDialogRow.class.getSimpleName();

    @InjectView(R.id.radio_button)
    protected RadioButton mRadioButton;

    @InjectView(R.id.cc_type)
    protected ImageView mCCType;

    @InjectView(R.id.payment_method_last_digits)
    protected TextView mLastDigits;

    private PaymentMethod mPaymentMethod;

    private ActionsHandler mActionsHandler;

    public ChoosePaymentMethodDialogRow(Context context) {
        this(context, null);
    }

    public ChoosePaymentMethodDialogRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChoosePaymentMethodDialogRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_choose_payment_method, this);
        ButterKnife.inject(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionsHandler != null && mPaymentMethod != null) {
                    mActionsHandler.onRowClicked(mPaymentMethod.getId());
                }
            }
        });
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setChecked(boolean checked) {
        mRadioButton.setChecked(checked);
    }

    public void updateData(PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
        mLastDigits.setText(mPaymentMethod.getLastFour());

        mCCType.setVisibility(View.INVISIBLE);
        if (mPaymentMethod.isAmex()) {
            mCCType.setImageResource(R.drawable.ic_amex);
            mCCType.setVisibility(View.VISIBLE);
        } else if (mPaymentMethod.isMastercard()) {
            mCCType.setImageResource(R.drawable.ic_mastercard);
            mCCType.setVisibility(View.VISIBLE);
        } else if (mPaymentMethod.isDiscover()) {
            mCCType.setImageResource(R.drawable.ic_discover);
            mCCType.setVisibility(View.VISIBLE);
        } else if (mPaymentMethod.isVisa()) {
            mCCType.setImageResource(R.drawable.ic_visa);
            mCCType.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.delete_button)
    protected void onDeleteClicked() {
        if (mActionsHandler != null && mPaymentMethod != null) {
            mActionsHandler.onDeleteClicked(mPaymentMethod.getId());
        }
    }

    public interface ActionsHandler {

        public void onDeleteClicked(String shippingAddressId);

        public void onRowClicked(String shippingAddressId);
    }
}