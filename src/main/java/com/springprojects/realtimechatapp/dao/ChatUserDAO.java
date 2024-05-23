package com.springprojects.realtimechatapp.dao;

import java.util.List;

import com.springprojects.realtimechatapp.entity.ChatUser;

public interface ChatUserDAO {
	
	public List<ChatUser> getchatUsers();

	public void saveChatUser(ChatUser theChatUser);
	
	public ChatUser getChatUser(int theId);

	public void deleteChatUser(int theId);

}
