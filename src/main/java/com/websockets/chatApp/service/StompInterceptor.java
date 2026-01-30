package com.websockets.chatApp.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class StompInterceptor implements ChannelInterceptor {

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            log.info("STOMP frame command : {} " , accessor.getCommand());
            String chatUser = accessor.getFirstNativeHeader("client-name");
            accessor.getSessionAttributes().put("sender" , chatUser) ;
            log.info("key chatUser : {}" , chatUser );
            String firstNativeHeader = accessor.getFirstNativeHeader("client-chat-id");
            log.info("key client-id : {}" , firstNativeHeader);
            String s = accessor.getLogin() + "\n" +
                    accessor.getPasscode() + "\n" +
                    accessor.getMessageId() + "\n" +
                    accessor.getMessageType() ;
            log.info( "Test INFO : {} " , s);
        }
        return message ;
    }
}
