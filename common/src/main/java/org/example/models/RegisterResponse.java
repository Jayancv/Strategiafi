package org.example.models;


public class RegisterResponse implements Response{

    @Override
    public ResponseType getResponseType() {
        return ResponseType.REGISTER_ACK;
    }
}
