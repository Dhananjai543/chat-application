package com.springprojects.realtimechatapp.service;

import java.util.List;

import com.springprojects.realtimechatapp.entity.ChatUser;


public interface ChatUserService {
	
	public List<ChatUser> getChatUsers();

	public void saveChatUser(ChatUser theChatUser);
	
	public ChatUser getChatUser(int theId);

	public void deleteChatUser(int theId);
	
	public ChatUser findByUsername(String username);
	
	public ChatUser findByUserEmail(String email);
	
}
