package com.springprojects.realtimechatapp.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springprojects.realtimechatapp.entity.Authority;

@Repository
public class AuthorityDAOImpl implements AuthorityDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<String> getAuthority() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<String> theQuery = currentSession.createQuery("from authorities", String.class);
		List<String> authorities = theQuery.getResultList();
		return authorities;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveOrUpdateAuthority(Authority authority) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(authority);	
	}

	@Override
	public void deleteAuthority(String this_user_email) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = currentSession.createQuery("delete from authorities where user_email=:this_user_email");
		theQuery.setParameter("this_user_email", this_user_email);
		theQuery.executeUpdate();
	}
	
}
