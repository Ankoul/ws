package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;

@Component
public class WSClientSender extends WSClient {

    public static final String WS_PATH = "/app/test";
    private Logger logger = LoggerFactory.getLogger(WSClientSender.class);
    private StompSession mSession;


    @Override
    protected StompSessionHandler newAdapter(String name) {
        return new StompSessionAdapter(name);
    }

    private class StompSessionAdapter extends StompSessionHandlerAdapter {

        private String name;

        public StompSessionAdapter(String name) {
            this.name = name;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.send(WS_PATH, ("{\"senderName\":\"" + name + "\"}").getBytes());
            mSession = session;
            logger.info("SEND " + name);

            send10messages();
        }

        public void send10messages(){
            for (int i = 0; i < 10; i++) {
                mSession.send(WS_PATH, ("{\"message\":\"message " + i + "\"}").getBytes());
            }
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            logger.info(name + "RECEIVED -> " + payload);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            exception.printStackTrace();
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Map.class;
        }
    }
}
