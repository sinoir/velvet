package com.delectable.mobile.api.models;

public class LocalNotifications {

    private boolean send_ln_one;

    private boolean send_ln_two;

    private boolean send_ln_three;

    private boolean send_ln_four;

    private boolean send_ln_five;


    public boolean getSendLnOne() {
        return send_ln_one;
    }

    public void setSendLnOne(boolean send_ln_one) {
        this.send_ln_one = send_ln_one;
    }

    public boolean getSendLnTwo() {
        return send_ln_two;
    }

    public void setSendLnTwo(boolean send_ln_two) {
        this.send_ln_two = send_ln_two;
    }

    public boolean getSendLnThree() {
        return send_ln_three;
    }

    public void setSendLnThree(boolean send_ln_three) {
        this.send_ln_three = send_ln_three;
    }

    public boolean getSendLnFour() {
        return send_ln_four;
    }

    public void setSendLnFour(boolean send_ln_four) {
        this.send_ln_four = send_ln_four;
    }

    public boolean getSendLnFive() {
        return send_ln_five;
    }

    public void setSendLnFive(boolean send_ln_five) {
        this.send_ln_five = send_ln_five;
    }
}
