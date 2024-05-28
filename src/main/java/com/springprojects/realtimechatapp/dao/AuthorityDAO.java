package com.springprojects.realtimechatapp.dao;

import java.util.List;

import com.springprojects.realtimechatapp.entity.Authority;
import com.springprojects.realtimechatapp.entity.ChatUser;


public interface AuthorityDAO {
	
	public List<String> getAuthority();

	public void saveOrUpdateAuthority(Authority authority);

	public void deleteAuthority(String user_email);
	
}
