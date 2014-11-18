package com.delectable.mobile.tests;

import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.endpointmodels.accounts.ShippingAddressesResponse;
import com.delectable.mobile.api.models.ShippingAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShippingAddressModelTest extends BaseInstrumentationTestCase {

    private ShippingAddressModel mShippingAddressModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mShippingAddressModel = new ShippingAddressModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mShippingAddressModel = null;
    }

    private List<ShippingAddress> loadShippingAddresses() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_shipping_addresses_response);
        ShippingAddressesResponse response = mGson
                .fromJson(json.toString(), ShippingAddressesResponse.class);
        return response.getPayload().getShippingAddresses();
    }

    public void testSaveShippingAddress() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();
        ShippingAddress expectedAddress = expectedShippingAddresses.get(0);

        mShippingAddressModel.saveShippingAddress(expectedAddress);
        assertEquals(expectedAddress, mShippingAddressModel.getData().get(expectedAddress.getId()));
    }

    public void testSaveShippingAddressAsPrimary() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();
        mShippingAddressModel.saveShippingAddresses(expectedShippingAddresses);
        ShippingAddress oldPrimaryAddress = expectedShippingAddresses.get(0);
        ShippingAddress newPrimaryAddress = expectedShippingAddresses.get(1);

        // Check if existing address is Primary
        assertTrue(oldPrimaryAddress.getPrimary());
        assertFalse(newPrimaryAddress.getPrimary());

        mShippingAddressModel.saveShippingAddressAsPrimary(newPrimaryAddress);

        // The values should have been switched:
        assertTrue(
                mShippingAddressModel.getShippingAddress(newPrimaryAddress.getId()).getPrimary());
        assertFalse(
                mShippingAddressModel.getShippingAddress(oldPrimaryAddress.getId()).getPrimary());
    }

    public void testSaveShippingAddresses() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();

        mShippingAddressModel.saveShippingAddresses(expectedShippingAddresses);

        assertEquals(2, mShippingAddressModel.getData().values().size());

        for (ShippingAddress expectedAddress : expectedShippingAddresses) {
            assertEquals(expectedAddress,
                    mShippingAddressModel.getData().get(expectedAddress.getId()));
        }
    }

    public void testRemoveShippingAddress() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();
        ShippingAddress addressToRemove = expectedShippingAddresses.get(0);

        mShippingAddressModel.saveShippingAddresses(expectedShippingAddresses);

        mShippingAddressModel.removeShippingAddress(addressToRemove.getId());

        assertFalse(mShippingAddressModel.getData().containsKey(addressToRemove.getId()));
    }

    public void testRemoveNonExistantShippingAddress() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();
        mShippingAddressModel.saveShippingAddresses(expectedShippingAddresses);

        // Shouldn't crash / throw NPE
        mShippingAddressModel.removeShippingAddress("Non Existant");

        assertEquals(2, mShippingAddressModel.getData().values().size());
    }

    public void testGetShippingAddressById() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();
        String id = expectedShippingAddresses.get(0).getId();

        mShippingAddressModel.saveShippingAddress(expectedShippingAddresses.get(0));

        assertEquals(expectedShippingAddresses.get(0),
                mShippingAddressModel.getShippingAddress(id));
    }

    public void testGetPrimaryShippingAddress() throws JSONException {
        List<ShippingAddress> expectedShippingAddresses = loadShippingAddresses();

        mShippingAddressModel.saveShippingAddresses(expectedShippingAddresses);

        assertEquals(expectedShippingAddresses.get(0),
                mShippingAddressModel.getPrimaryShippingAddress());
    }
}
