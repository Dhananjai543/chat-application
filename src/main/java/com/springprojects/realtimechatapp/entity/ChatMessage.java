package com.springprojects.realtimechatapp.entity;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType messageType;
    private String chatGroupName;
}
