package com.delectable.mobile.model.api.wineprofiles;

import com.delectable.mobile.model.api.BaseRequest;

public class WineProfilesPurchaseRequest extends BaseRequest {

    private Payload payload;

    public WineProfilesPurchaseRequest(String id, String purchase_offer_id,
            String payment_method_id,
            String shipping_address_id, int quantity, String additional_comments) {
        super();
        this.payload = new Payload(id, purchase_offer_id, payment_method_id, shipping_address_id,
                quantity, additional_comments);
    }

    public static class Payload {

        private String id;

        private String purchase_offer_id;

        private String payment_method_id;

        private String shipping_address_id;

        private int quantity;

        private String additional_comments;

        /**
         * @param id                  required
         * @param purchase_offer_id   required
         * @param payment_method_id   required
         * @param shipping_address_id required
         * @param quantity            required
         * @param additional_comments optional
         */
        public Payload(String id, String purchase_offer_id, String payment_method_id,
                String shipping_address_id, int quantity, String additional_comments) {
            this.id = id;
            this.purchase_offer_id = purchase_offer_id;
            this.payment_method_id = payment_method_id;
            this.shipping_address_id = shipping_address_id;
            this.quantity = quantity;
            this.additional_comments = additional_comments;
        }

    }

}
