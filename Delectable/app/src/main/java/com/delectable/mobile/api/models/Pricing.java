package com.delectable.mobile.api.models;

public class Pricing {

    Integer quantity;

    String per_bottle;

    String wine;

    String shipping;

    String tax;

    String total;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPerBbottle() {
        return per_bottle;
    }

    public void setPerBottle(String per_bottle) {
        this.per_bottle = per_bottle;
    }

    public String getWine() {
        return wine;
    }

    public void setWine(String wine) {
        this.wine = wine;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "quantity=" + quantity +
                ", per_bottle='" + per_bottle + '\'' +
                ", wine='" + wine + '\'' +
                ", shipping='" + shipping + '\'' +
                ", tax='" + tax + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
