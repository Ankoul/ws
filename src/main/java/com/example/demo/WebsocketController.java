package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class WebsocketController {

    private Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Map test(Map message) {
        logger.info(message.toString());
        return message;
    }
}
