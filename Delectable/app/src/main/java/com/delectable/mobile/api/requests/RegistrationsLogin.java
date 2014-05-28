package com.delectable.mobile.api.requests;

public class RegistrationsLogin extends BaseRegistrations {

    String email;

    String password;

    String session_type;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "session_type",
                "email",
                "password",
        };
    }

    @Override
    public String getResourceUrl() {
        return sBaseUri + "/login";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionType() {
        return session_type;
    }

    public void setSessionType(String session_type) {
        this.session_type = session_type;
    }
}
