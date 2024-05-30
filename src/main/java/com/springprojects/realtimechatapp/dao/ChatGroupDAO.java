package com.springprojects.realtimechatapp.dao;

import java.util.List;

import com.springprojects.realtimechatapp.entity.ChatGroup;

public interface ChatGroupDAO {
	
	public List<ChatGroup> getChatGroups();

	public void saveChatGroup(ChatGroup theChatGroup);
	
	public ChatGroup getChatGroup(int theId);

	public void deleteChatGroup(int theId);

	public ChatGroup findByChatGroupName(String chatgroup);

}
