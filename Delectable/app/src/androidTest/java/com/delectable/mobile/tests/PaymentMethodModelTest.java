package com.delectable.mobile.tests;

import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.endpointmodels.accounts.PaymentMethodResponse;
import com.delectable.mobile.api.models.PaymentMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PaymentMethodModelTest extends BaseInstrumentationTestCase {

    private PaymentMethodModel mPaymentMethodModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPaymentMethodModel = new PaymentMethodModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mPaymentMethodModel = null;
    }

    private List<PaymentMethod> loadPaymentMethods() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_payment_methods_response);
        PaymentMethodResponse response = mGson
                .fromJson(json.toString(), PaymentMethodResponse.class);
        return response.getPayload().getPaymentMethods();
    }

    public void testSavePaymentMethod() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();
        PaymentMethod expectedAddress = expectedPaymentMethods.get(0);

        mPaymentMethodModel.savePaymentMethod(expectedAddress);
        assertEquals(expectedAddress, mPaymentMethodModel.getData().get(expectedAddress.getId()));
    }

    public void testSavePaymentMethodAsPrimary() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();
        mPaymentMethodModel.savePaymentMethods(expectedPaymentMethods);
        PaymentMethod oldPrimaryAddress = expectedPaymentMethods.get(1);
        PaymentMethod newPrimaryAddress = expectedPaymentMethods.get(0);

        // Check if existing address is Primary
        assertTrue(oldPrimaryAddress.getPrimary());
        assertFalse(newPrimaryAddress.getPrimary());

        mPaymentMethodModel.savePaymentMethodAsPrimary(newPrimaryAddress);

        // The values should have been switched:
        assertTrue(
                mPaymentMethodModel.getPaymentMethod(newPrimaryAddress.getId()).getPrimary());
        assertFalse(
                mPaymentMethodModel.getPaymentMethod(oldPrimaryAddress.getId()).getPrimary());
    }

    public void testSavePaymentMethods() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();

        mPaymentMethodModel.savePaymentMethods(expectedPaymentMethods);

        assertEquals(2, mPaymentMethodModel.getData().values().size());

        for (PaymentMethod expectedAddress : expectedPaymentMethods) {
            assertEquals(expectedAddress,
                    mPaymentMethodModel.getData().get(expectedAddress.getId()));
        }
    }

    public void testRemovePaymentMethod() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();
        PaymentMethod addressToRemove = expectedPaymentMethods.get(0);

        mPaymentMethodModel.savePaymentMethods(expectedPaymentMethods);

        mPaymentMethodModel.removePaymentMethod(addressToRemove.getId());

        assertFalse(mPaymentMethodModel.getData().containsKey(addressToRemove.getId()));
    }

    public void testRemoveNonExistantPaymentMethod() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();
        mPaymentMethodModel.savePaymentMethods(expectedPaymentMethods);

        // Shouldn't crash / throw NPE
        mPaymentMethodModel.removePaymentMethod("Non Existant");

        assertEquals(2, mPaymentMethodModel.getData().values().size());
    }

    public void testGetPaymentMethodById() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();
        String id = expectedPaymentMethods.get(0).getId();

        mPaymentMethodModel.savePaymentMethod(expectedPaymentMethods.get(0));

        assertEquals(expectedPaymentMethods.get(0),
                mPaymentMethodModel.getPaymentMethod(id));
    }

    public void testGetPrimaryPaymentMethod() throws JSONException {
        List<PaymentMethod> expectedPaymentMethods = loadPaymentMethods();

        mPaymentMethodModel.savePaymentMethods(expectedPaymentMethods);

        assertEquals(expectedPaymentMethods.get(1), mPaymentMethodModel.getPrimaryPaymentMethod());
    }
}
