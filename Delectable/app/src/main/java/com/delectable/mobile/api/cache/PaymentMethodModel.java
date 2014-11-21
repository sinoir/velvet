package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PaymentMethodModel {

    private HashMap<String, PaymentMethod> mData = new HashMap<String, PaymentMethod>();

    @Inject
    public PaymentMethodModel() {
    }

    public void savePaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return;
        }
        mData.put(paymentMethod.getId(), paymentMethod);
    }

    public void savePaymentMethodAsPrimary(PaymentMethod primaryPaymentMethod) {
        if (primaryPaymentMethod == null) {
            return;
        }
        // Reset old primary shipping address to false
        for (PaymentMethod paymentMethod : mData.values()) {
            paymentMethod.setPrimary(false);
        }
        primaryPaymentMethod.setPrimary(true);
        mData.put(primaryPaymentMethod.getId(), primaryPaymentMethod);
    }

    public void savePaymentMethods(List<PaymentMethod> paymentMethods) {
        if (paymentMethods == null) {
            return;
        }
        for (PaymentMethod paymentMethod : paymentMethods) {
            savePaymentMethod(paymentMethod);
        }
    }

    public PaymentMethod getPaymentMethod(String id) {
        return mData.get(id);
    }

    public PaymentMethod getPrimaryPaymentMethod() {
        for (PaymentMethod paymentMethod : mData.values()) {
            if (paymentMethod.getPrimary()) {
                return paymentMethod;
            }
        }
        return null;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<PaymentMethod>();
        for (PaymentMethod paymentMethod : mData.values()) {
            paymentMethods.add(paymentMethod);
        }
        return paymentMethods;
    }

    public void removePaymentMethod(String id) {
        if (mData.containsKey(id)) {
            mData.remove(id);
        }
    }

    public void clear() {
        mData.clear();
    }

    /**
     * DO NOT USE DIRECTLY, only used for testing
     */
    public HashMap<String, PaymentMethod> getData() {
        return mData;
    }
}
