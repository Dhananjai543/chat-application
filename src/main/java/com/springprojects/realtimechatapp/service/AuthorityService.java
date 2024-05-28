package com.springprojects.realtimechatapp.service;

import java.util.List;

import com.springprojects.realtimechatapp.entity.Authority;

public interface AuthorityService {
	
	public List<String> getAuthority();

	public void saveOrUpdateAuthority(Authority authority);

	public void deleteAuthority(String user_email);
	
}
