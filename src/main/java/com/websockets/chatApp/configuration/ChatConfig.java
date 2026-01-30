package com.websockets.chatApp.configuration;


import com.websockets.chatApp.service.StompInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

     private final StompInterceptor stompInterceptor ;

    public ChatConfig(StompInterceptor stompInterceptor) {
        this.stompInterceptor = stompInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/myChat")
                .addInterceptors() // before sending connect frame like verifying security
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry mb){
        mb.setApplicationDestinationPrefixes("/app") ;
        mb.enableSimpleBroker("/topic") ;
    }

     @Override
     public void configureClientInboundChannel(ChannelRegistration registration) {
         // {message} here is STOMP connect frame which was sent during connection creating
         // from sender -> {{Message channel -> clientInbound}} -> /app
         registration.interceptors(stompInterceptor);
     }
}
