package com.dc.ws.client;

import com.google.gson.Gson;
import org.example.models.RegisterRequest;
import org.example.models.RegisterResponse;
import org.example.models.ResponseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CustomStompSessionHandler implements StompSessionHandler {
    @Value("${client.id}")
    private String clientId;

    private final Gson gson  = new Gson();

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Client connected to ws server : " + session.getSessionId());
        session.subscribe("/websocket", this);
        session.send("/websocket", new RegisterRequest(clientId));
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("ERRRRRRRRRRRRRR");
        throw new RuntimeException(exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Transport ERRRRRRRRRRRRRR");
        throw new RuntimeException(exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("received Message");
        RegisterResponse msg = (RegisterResponse) payload;
        if(msg.getResponseType().equals(ResponseType.REGISTER_ACK)){
            System.out.println("Registration Successful");
        } else {
            System.out.println("Invalid message received via websocket :" + gson.toJson(msg));
        }

    }
}
