package com.springprojects.realtimechatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springprojects.realtimechatapp.dao.AuthorityDAO;
import com.springprojects.realtimechatapp.dao.ChatUserDAO;
import com.springprojects.realtimechatapp.entity.Authority;
import com.springprojects.realtimechatapp.entity.ChatUser;

@Service
public class ChatUserServiceImpl implements ChatUserService{

	@Autowired
	private ChatUserDAO chatUserDao;
	
	@Autowired
	private AuthorityDAO authorityDao;
	
	@Override
	@Transactional
	public List<ChatUser> getChatUsers() {
		return chatUserDao.getchatUsers();
	}

	@Override
	@Transactional
	public void saveChatUser(ChatUser theChatUser) {
		chatUserDao.saveChatUser(theChatUser);
		// Create and save the Authority
        Authority authority = new Authority(theChatUser.getUser_email(), "ROLE_USER");
        authorityDao.saveOrUpdateAuthority(authority);

        // Add the Authority to the ChatUser
        theChatUser.getAuthorities().add(authority);
  
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

	@Override
	@Transactional
	public ChatUser findByUsername(String username) {
		return chatUserDao.findByUsername(username);
	}

	@Override
	@Transactional
	public ChatUser findByUserEmail(String email) {
		return chatUserDao.findByUserEmail(email);
	}
	

}
