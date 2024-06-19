package com.springprojects.realtimechatapp.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="chat_user")
public class ChatUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int user_id;
	
	@Column(name="user_name")
	private String user_name;
	
	@Column(name="user_email")
	private String user_email;
	
	@Column(name="user_password")
	private String user_password;  //password validations handled in controller class
	
	
//	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH })
//	@JoinTable(
//			name="group_user",
//			joinColumns=@JoinColumn(name="user_id"),
//			inverseJoinColumns=@JoinColumn(name="group_id")
//			)
//	private List<ChatGroup> chatGroups;
	

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user_email")
    private List<Authority> authorities;


	public ChatUser() {
		
	}

	public ChatUser(String user_name, String user_email, String user_password) {
		this.user_name = user_name;
		this.user_email = user_email;
		this.user_password = user_password;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	
	public List<Authority> getAuthorities() {
		if(authorities == null) {
			authorities = new ArrayList<>();
		}
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "ChatUser [user_id=" + user_id + ", user_name=" + user_name + ", user_email=" + user_email
				+ ", user_password=" + user_password + ", authorities=" + authorities + "]";
	}

	
	
	
}
