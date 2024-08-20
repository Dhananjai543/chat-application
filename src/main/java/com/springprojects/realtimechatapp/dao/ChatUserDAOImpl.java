package com.springprojects.realtimechatapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springprojects.realtimechatapp.entity.ChatUser;

import jakarta.persistence.NoResultException;

@Repository
public class ChatUserDAOImpl implements ChatUserDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ChatUser> getchatUsers() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ChatUser> theQuery = currentSession.createQuery("from ChatUser", ChatUser.class);
		List<ChatUser> chatUsers = theQuery.getResultList();
		return chatUsers;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveChatUser(ChatUser theChatUser) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theChatUser);	
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

	@Override
	public ChatUser findByUsername(String username) {
	    Session currentSession = sessionFactory.getCurrentSession();
	    Query<ChatUser> theQuery = currentSession.createQuery("from ChatUser where user_name=:u", ChatUser.class);
	    theQuery.setParameter("u", username);
	    try {
	        return theQuery.getSingleResult();
	    } catch (NoResultException nre) {
	        return null;
	    }
	}

	@Override
	public ChatUser findByUserEmail(String email) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<ChatUser> theQuery = currentSession.createQuery("from ChatUser where user_email=:e", ChatUser.class);
		theQuery.setParameter("e", email);
		try {
	        return theQuery.getSingleResult();
	    } catch (NoResultException nre) {
	        return null;
	    }
	}
}
