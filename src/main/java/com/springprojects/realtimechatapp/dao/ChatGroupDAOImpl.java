package com.springprojects.realtimechatapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springprojects.realtimechatapp.entity.ChatGroup;

import jakarta.persistence.NoResultException;

@Repository
public class ChatGroupDAOImpl implements ChatGroupDAO{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ChatGroup> getChatGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveChatGroup(ChatGroup theChatGroup) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theChatGroup);
	}

	@Override
	public ChatGroup getChatGroup(int theId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteChatGroup(int theId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChatGroup findByChatGroupName(String chatgroup) {
		Session currentSession = sessionFactory.getCurrentSession();
	    Query<ChatGroup> theQuery = currentSession.createQuery("from ChatGroup where group_name=:g", ChatGroup.class);
	    theQuery.setParameter("g", chatgroup);
	    try {
	        return theQuery.getSingleResult();
	    } catch (NoResultException nre) {
	        return null;
	    }
	}
}

