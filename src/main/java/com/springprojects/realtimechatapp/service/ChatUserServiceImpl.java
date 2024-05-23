package com.springprojects.realtimechatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springprojects.realtimechatapp.dao.ChatUserDAO;
import com.springprojects.realtimechatapp.entity.ChatUser;

@Service
public class ChatUserServiceImpl implements ChatUserService{

	@Autowired
	private ChatUserDAO chatUserDao;
	
	@Override
	@Transactional
	public List<ChatUser> getChatUsers() {
		return chatUserDao.getchatUsers();
	}

	@Override
	@Transactional
	public void saveChatUser(ChatUser theChatUser) {
		chatUserDao.saveChatUser(theChatUser);
	}
	
	@Override
	@Transactional
	public ChatUser getChatUser(int theId) {
		return chatUserDao.getChatUser(theId);
	}

	@Override
	@Transactional
	public void deleteChatUser(int theId) {
		chatUserDao.deleteChatUser(theId);
	}

}
