package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;

@Component
public class WSClientReceiver extends WSClient {

    private Logger logger = LoggerFactory.getLogger(WSClientReceiver.class);

    protected StompSessionAdapter newAdapter(String name) {
        return new StompSessionAdapter(name);
    }

    private class StompSessionAdapter extends StompSessionHandlerAdapter {

        private String name;

        public StompSessionAdapter(String name) {
            this.name = name;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/topic/test", this);

            logger.info("SUBSCRIBED " + name);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            logger.info("RECEIVED " + name + " -> " + payload);
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
