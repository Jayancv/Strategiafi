package com.dc.ws.client;

import jakarta.annotation.PostConstruct;
import org.example.models.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Component
public class WebSocketClient {
    @Value("${remote.init.server.address}")
    private String serverUrl;

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        System.out.println(":::::::::::::::::: Initiating Websocket client on :" + serverUrl + " ::::::::::::::::::::::: ");
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connectAsync(serverUrl, new CustomStompSessionHandler()).thenAccept(session -> {
            System.out.println("############ connection established ##############");
        }).exceptionally(err -> {
            System.out.println(err.getMessage());
            return null;
        });
//        stompSession.send("/websocket", new RegisterRequest("c_1234"));
//        System.out.println(stompSession.getSessionId());
    }

}
