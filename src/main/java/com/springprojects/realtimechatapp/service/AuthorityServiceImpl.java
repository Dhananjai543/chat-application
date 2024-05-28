package com.springprojects.realtimechatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springprojects.realtimechatapp.dao.AuthorityDAO;
import com.springprojects.realtimechatapp.entity.Authority;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	
	@Autowired
	private AuthorityDAO authorityDao;

	@Override
	@Transactional
	public List<String> getAuthority() {
		return authorityDao.getAuthority();
	}

	@Override
	public void saveOrUpdateAuthority(Authority authority) {
		authorityDao.saveOrUpdateAuthority(authority);
	}

	@Override
	public void deleteAuthority(String user_email) {
		authorityDao.deleteAuthority(user_email);
	}

}
