package com.delectable.mobile.api.models;

public class PaymentMethod {

    private String id;

    private boolean primary;

    private String last_four;

    private String type;

    private String expiration;

    // Note: These should never be persisted locally
    private String number;

    private String exp_month;

    private String exp_year;

    private String cvc;

    public PaymentMethod() {
    }

    /**
     * Build Payment method for Creating Payment Method
     *
     * @param number    - Creditcard Number
     * @param exp_month - Expiration Month
     * @param exp_year  - Expiration year
     * @param cvc       - CVC Secret
     */
    public PaymentMethod(String number, String exp_month, String exp_year, String cvc) {
        this.number = number;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.cvc = cvc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getLastFour() {
        return last_four;
    }

    public void setLastFour(String last_four) {
        this.last_four = last_four;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpMonth() {
        return exp_month;
    }

    public void setExpMonth(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExpYear() {
        return exp_year;
    }

    public void setExpYear(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id='" + id + '\'' +
                ", primary=" + primary +
                ", last_four='" + last_four + '\'' +
                ", type='" + type + '\'' +
                ", expiration='" + expiration + '\'' +
                ", number='" + number + '\'' +
                ", exp_month='" + exp_month + '\'' +
                ", exp_year='" + exp_year + '\'' +
                ", cvc='" + cvc + '\'' +
                '}';
    }
}
