package com.springprojects.realtimechatapp.service;

import java.util.List;

import com.springprojects.realtimechatapp.entity.ChatGroup;
import com.springprojects.realtimechatapp.entity.ChatUser;

public interface ChatGroupService {
	
	public List<ChatGroup> getChatGroups();

	public void saveChatGroup(ChatGroup theChatGroup);
	
	public ChatGroup getChatGroup(int theId);

	public void deleteChatGroup(int theId);

	public ChatGroup findByChatGroupName(String chatgroup);
	
}
