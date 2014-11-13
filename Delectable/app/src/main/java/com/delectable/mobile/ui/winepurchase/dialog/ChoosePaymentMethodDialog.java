package com.delectable.mobile.ui.winepurchase.dialog;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.RemovePaymentMethodEvent;
import com.delectable.mobile.api.models.PaymentMethod;
import com.delectable.mobile.ui.common.widget.CancelSaveButtons;
import com.delectable.mobile.ui.winepurchase.widget.ChoosePaymentMethodAdapter;
import com.delectable.mobile.ui.winepurchase.widget.ChoosePaymentMethodDialogRow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ChoosePaymentMethodDialog extends DialogFragment
        implements CancelSaveButtons.ActionsHandler, ChoosePaymentMethodDialogRow.ActionsHandler,
        ChoosePaymentMethodAdapter.ActionsHandler {

    public static final String EXTRAS_PAYMENT_METHOD_ID = "EXTRAS_PAYMENT_METHOD_ID";

    public static final int RESULT_PAYMENT_METHOD_SELECTED = 1000;

    private static final int REQUEST_ADD_PAYMENT_METHOD_DIALOG = 1000;

    private static final String ARGS_SELECTED_PAYMENT_METHOD_ID = "ARGS_SELECTED_PAYMENT_METHOD_ID";

    private static final String TAG = ChoosePaymentMethodDialog.class.getSimpleName();

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    @InjectView(R.id.dialog_title)
    protected TextView mDialogTitle;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.progress_bar)
    protected View mProgressBar;

    @InjectView(R.id.action_buttons)
    protected CancelSaveButtons mActionButtons;

    private ChoosePaymentMethodAdapter mAdapter;

    private String mSelectedPaymentMethodId;

    private ArrayList<PaymentMethod> mPaymentMethods;

    private ArrayList<String> mRemovePaymentMethodList = new ArrayList<String>();

    public static ChoosePaymentMethodDialog newInstance(String paymentMethodId) {
        ChoosePaymentMethodDialog f = new ChoosePaymentMethodDialog();
        Bundle args = new Bundle();
        args.putString(ARGS_SELECTED_PAYMENT_METHOD_ID, paymentMethodId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        mSelectedPaymentMethodId = getArguments().getString(ARGS_SELECTED_PAYMENT_METHOD_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_checkoutitem_listing, container, false);
        ButterKnife.inject(this, view);

        mDialogTitle.setText(R.string.paymentmethod_choose_title);

        mActionButtons.setActionsHandler(this);

        mAdapter = new ChoosePaymentMethodAdapter();
        mAdapter.setRowActionsHandler(this);
        mAdapter.setActionsHandler(this);
        mListView.setAdapter(mAdapter);

        loadExistingPaymentMethods();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
            // no-op
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_PAYMENT_METHOD_DIALOG
                && resultCode == AddPaymentMethodDialog.RESULT_PAYMENT_METHOD_SAVED) {
            loadExistingPaymentMethods();
        }
    }

    private void dismissWithSelectedId(String id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRAS_PAYMENT_METHOD_ID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                RESULT_PAYMENT_METHOD_SELECTED,
                intent);
        dismiss();
    }

    //region UpdateUI
    private void updateUIWithNewData() {
        mAdapter.setSelectedItemById(mSelectedPaymentMethodId);
        mAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Saving/Loading
    private void loadExistingPaymentMethods() {
        mPaymentMethods = (ArrayList<PaymentMethod>) mPaymentMethodModel.getAllPaymentMethods();

        ArrayList<PaymentMethod> toRemove = new ArrayList<PaymentMethod>();
        for (PaymentMethod paymentMethod : mPaymentMethods) {
            if (mRemovePaymentMethodList.contains(paymentMethod.getId())) {
                toRemove.add(paymentMethod);
            }
        }

        mPaymentMethods.removeAll(toRemove);

        if (mSelectedPaymentMethodId == null && mPaymentMethods.size() > 0) {
            mSelectedPaymentMethodId = mPaymentMethods.get(0).getId();
        }

        mAdapter.setData(mPaymentMethods);
        updateUIWithNewData();
    }

    public void syncRemovedItems() {
        showLoader();
        mAccountController.removePaymentMethods(mRemovePaymentMethodList);
    }
    //endregion

    //region onClicks
    @Override
    public void onSaveClicked() {
        if (mRemovePaymentMethodList.size() > 0) {
            syncRemovedItems();
        } else {
            dismissWithSelectedId(mSelectedPaymentMethodId);
        }
    }

    @Override
    public void onCancelClicked() {
        dismiss();
    }

    @Override
    public void onDeleteClicked(String id) {
        // Remove Item From List
        PaymentMethod itemToRemove = null;
        for (PaymentMethod address : mPaymentMethods) {
            if (address.getId().equalsIgnoreCase(id)) {
                itemToRemove = address;
                break;
            }
        }

        mRemovePaymentMethodList.add(id);
        mPaymentMethods.remove(itemToRemove);

        if (mSelectedPaymentMethodId.equalsIgnoreCase(id)) {
            if (mPaymentMethods.size() > 0) {
                mSelectedPaymentMethodId = mPaymentMethods.get(0).getId();
            } else {
                mSelectedPaymentMethodId = null;
            }
        }
        mAdapter.setSelectedItemById(mSelectedPaymentMethodId);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddAnotherClicked() {
        showAddPaymentMethodDialog();
    }

    @Override
    public void onRowClicked(String id) {
        mSelectedPaymentMethodId = id;
        updateUIWithNewData();
    }
    //endregion

    //region EventBus
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RemovePaymentMethodEvent event) {
        hideLoader();
        if (event.isSuccessful()) {
            dismissWithSelectedId(mSelectedPaymentMethodId);
        } else {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), event.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion

    private void showAddPaymentMethodDialog() {
        AddPaymentMethodDialog dialog = AddPaymentMethodDialog.newInstance();
        dialog.setTargetFragment(this, REQUEST_ADD_PAYMENT_METHOD_DIALOG);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showLoader() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        mProgressBar.setVisibility(View.GONE);
    }
}
