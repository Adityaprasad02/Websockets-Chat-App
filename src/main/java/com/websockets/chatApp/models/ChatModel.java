package com.websockets.chatApp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatModel {
    private String sender ;
    private String chatContent ;
    private MessageType type ;
}
