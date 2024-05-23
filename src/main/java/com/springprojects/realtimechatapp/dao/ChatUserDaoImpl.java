package com.springprojects.realtimechatapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springprojects.realtimechatapp.entity.ChatUser;

@Repository
public class ChatUserDaoImpl implements ChatUserDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ChatUser> getchatUsers() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ChatUser> theQuery = currentSession.createQuery("from ChatUser", ChatUser.class);
		List<ChatUser> chatUsers = theQuery.getResultList();
		return chatUsers;
	}

	@Override
	public void saveChatUser(ChatUser theChatUser) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.merge(theChatUser);	
	}

	@Override
	public ChatUser getChatUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		ChatUser theChatUser = currentSession.get(ChatUser.class, theId);
		return theChatUser;
	}

	@Override
	public void deleteChatUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("delete from ChatUser where chat_id=:chatUserId");
		theQuery.setParameter("chatUserId", theId);
		theQuery.executeUpdate();
	}

}
