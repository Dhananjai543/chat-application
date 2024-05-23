package com.springprojects.realtimechatapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springprojects.realtimechatapp.dao.ChatGroupDAO;
import com.springprojects.realtimechatapp.entity.ChatGroup;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {

	private ChatGroupDAO chatGroupDao;
	
	@Override
	@Transactional
	public List<ChatGroup> getChatGroups() {
		return chatGroupDao.getChatGroups();
	}

	@Override
	@Transactional
	public void saveChatGroup(ChatGroup theChatGroup) {
		chatGroupDao.saveChatGroup(theChatGroup);
	}

	@Override
	@Transactional
	public ChatGroup getChatGroup(int theId) {
		return chatGroupDao.getChatGroup(theId);
	}

	@Override
	@Transactional
	public void deleteChatGroup(int theId) {
		chatGroupDao.deleteChatGroup(theId);
	}

}
