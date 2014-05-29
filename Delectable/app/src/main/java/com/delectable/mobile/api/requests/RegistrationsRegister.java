package com.delectable.mobile.api.requests;

public class RegistrationsRegister extends BaseRegistrations {

    String email;

    String fname;

    String lname;

    String password;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "session_type",
                "email",
                "password",
                "fname",
                "lname",
        };
    }

    @Override
    public String getResourceUrl() {
        return sBaseUri + "/register";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
