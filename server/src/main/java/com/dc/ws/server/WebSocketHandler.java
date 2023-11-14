package com.dc.ws.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import org.example.models.RegisterRequest;
import org.example.models.RegisterResponse;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Controller
public class WebSocketHandler extends TextWebSocketHandler {

    private static final String invalidIPAddress = "INVALID_IP_ADDRESS";
    private static Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private static Map<String, String> leafNodeIPAddresses = new ConcurrentHashMap<>();

    private static final Gson gson = new Gson();

@Override
public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
    // Handle incoming messages here

    if(message.getPayload() instanceof RegisterRequest){
        RegisterRequest receivedMessage = (RegisterRequest) message.getPayload();
        leafNodeIPAddresses.put(receivedMessage.getClientId(), getIpAddressFromWebsocketConnection(session));
    } else {
        System.out.println(":::::::" + message.getPayload() +":::::::::::" );

    }

    try {
        session.sendMessage(new TextMessage(gson.toJson(new RegisterResponse())));

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}

@Override
public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    activeSessions.put(session.getId(), session);
    System.out.println("connection cls: " + session.getId());
}

@Override
public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    activeSessions.remove(session.getId());
    System.out.println("connection cls: " + session.getId());
}

public static void send(String session_id, String msg) throws MessagingException {
    System.out.println("sessionId "+session_id);
    WebSocketSession session = activeSessions.get(session_id);
    if(session != null){
        try {
                session.sendMessage (new TextMessage(msg));
            } catch (IOException ex) {
                throw new MessagingException(ex.getMessage());
            }
    } else {
        System.out.println("session is null");
    }
}

private String getIpAddressFromWebsocketConnection(WebSocketSession session){

    String clientIPAddress;
    InetSocketAddress remoteAddress = session.getRemoteAddress();
    if(remoteAddress != null){
        clientIPAddress = remoteAddress.getAddress().getHostAddress();
    } else {
        System.out.println("client IP address not found");
        clientIPAddress = invalidIPAddress;
    }
    System.out.println("Client IP address: " + clientIPAddress);
    return clientIPAddress;
}
}