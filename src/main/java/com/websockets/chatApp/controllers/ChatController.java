package com.websockets.chatApp.controllers;


import com.websockets.chatApp.models.ChatModel;
import com.websockets.chatApp.models.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatModel handleMessage(@Payload ChatModel chatModel){
        log.info("chatMessage : {} " , chatModel.getChatContent()) ;
         return chatModel;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatModel handleJoinRequest(@Payload ChatModel model){
        if(model.getType().equals(MessageType.JOIN)){
            log.info("{} is joined " , model.getSender()) ;

            return new ChatModel(model.getSender() , model.getChatContent() ,  MessageType.JOIN ) ;
        }
        return null ;
    }
}
