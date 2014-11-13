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

    //region CC Type Helpers

    public boolean isAmex() {
        return isType("American Express");
    }

    public boolean isDiscover() {
        return isType("discover");
    }

    public boolean isMastercard() {
        return isType("mastercard");
    }

    public boolean isVisa() {
        return isType("visa");
    }

    private boolean isType(String ccType) {
        return ccType.equalsIgnoreCase(type);
    }
    //endregion

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentMethod that = (PaymentMethod) o;

        if (primary != that.primary) {
            return false;
        }
        if (cvc != null ? !cvc.equals(that.cvc) : that.cvc != null) {
            return false;
        }
        if (exp_month != null ? !exp_month.equals(that.exp_month) : that.exp_month != null) {
            return false;
        }
        if (exp_year != null ? !exp_year.equals(that.exp_year) : that.exp_year != null) {
            return false;
        }
        if (expiration != null ? !expiration.equals(that.expiration) : that.expiration != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (last_four != null ? !last_four.equals(that.last_four) : that.last_four != null) {
            return false;
        }
        if (number != null ? !number.equals(that.number) : that.number != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (primary ? 1 : 0);
        result = 31 * result + (last_four != null ? last_four.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (exp_month != null ? exp_month.hashCode() : 0);
        result = 31 * result + (exp_year != null ? exp_year.hashCode() : 0);
        result = 31 * result + (cvc != null ? cvc.hashCode() : 0);
        return result;
    }
}
