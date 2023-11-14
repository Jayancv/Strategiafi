package org.example.models;

public class RegisterRequest implements Request{

    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public RegisterRequest(String clientId){
        this.clientId = clientId;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.REGISTER;
    }
}
