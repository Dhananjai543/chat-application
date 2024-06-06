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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="chat_group")
public class ChatGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="group_id")
	private int group_id;
	
	@Column(name="group_name")
	@Size(min = 5, max = 20, message="Group name must be between 5 to 20 characters")
	private String group_name;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH })
	@JoinTable(
			name="group_user",
			joinColumns=@JoinColumn(name="group_id"),
			inverseJoinColumns=@JoinColumn(name="user_id")
			)
	private List<ChatUser> chatUsers;
	
	public ChatGroup() {
		
	}

	public ChatGroup(int group_id, String group_name) {
		this.group_id = group_id;
		this.group_name = group_name;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public List<ChatUser> getChatUsers() {
		return chatUsers;
	}

	public void setChatUsers(List<ChatUser> chatUsers) {
		this.chatUsers = chatUsers;
	}
	
	public void addChatUser(ChatUser chatUser) {
		if(chatUsers==null) {
			chatUsers = new ArrayList<>();
		}
		chatUsers.add(chatUser);
	}

	@Override
	public String toString() {
		return "ChatGroup [group_id=" + group_id + ", group_name=" + group_name + ", chatUsers=" + chatUsers + "]";
	}
	

}
