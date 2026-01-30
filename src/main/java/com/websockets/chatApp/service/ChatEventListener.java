package com.websockets.chatApp.service;


import com.websockets.chatApp.models.ChatModel;
import com.websockets.chatApp.models.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Arrays;

@Component
@Slf4j
public class ChatEventListener {

    private final  SimpMessagingTemplate template ;

    public ChatEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void handleSessionConnectRequest(SessionConnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sender = accessor.getFirstNativeHeader("client-name");
        log.info("{} is sending STOMP CONNECT frame" , sender);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage())  ;
        String sender = accessor.getSessionAttributes().get("sender").toString();
        if(sender !=null ){
            log.info("{} left the chat " , sender) ;
            ChatModel chatModel = new ChatModel(sender, null, MessageType.LEAVE);
            template.convertAndSend("/topic/public" , chatModel);
        };
    }


}
