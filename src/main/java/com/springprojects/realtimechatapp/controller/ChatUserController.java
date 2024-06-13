package com.springprojects.realtimechatapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.springprojects.realtimechatapp.entity.Authority;
import com.springprojects.realtimechatapp.entity.ChatUser;
import com.springprojects.realtimechatapp.service.AuthorityService;
import com.springprojects.realtimechatapp.service.ChatUserService;

import jakarta.validation.Valid;

@Controller
public class ChatUserController {

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ChatUserService chatUserService;

	@Autowired
	private AuthorityService authorityService;

	@GetMapping("/showLoginForm")
	public String showLoginForm() {
		return "login-form";
	}

	@GetMapping("/showSignUpForm")
	public String showSignUpForm(Model theModel) {
		theModel.addAttribute("chatUser", new ChatUser());
		return "sign-up-form";
	}

	@PostMapping("/processSignUpForm")
	public String processSignUpForm(@Valid @ModelAttribute("chatUser") ChatUser chatUser, BindingResult bindingResult,
			Model model) {

		System.out.println(chatUser.toString());

		// validation errors from ChatUser Entity
		if (bindingResult.hasErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				System.out.println(error.getDefaultMessage());
				model.addAttribute("error", error.getDefaultMessage());
			}
			return "sign-up-form";
		}

		try {

			ChatUser existingUser = chatUserService.findByUsername(chatUser.getUser_name());
			if(existingUser != null){
				model.addAttribute("error", "OOPS!! This username is already taken. Please use some other username");
				return "sign-up-form";
			}

			System.out.println(chatUser.toString());

			// password encryption
//            chatUser.setUser_password(passwordEncoder.encode(chatUser.getUser_password()));

			chatUser.setUser_password("{noop}" + chatUser.getUser_password());
			chatUserService.saveChatUser(chatUser);
            Authority authority = new Authority(chatUser.getUser_email(), "ROLE_USER");
            authorityService.saveOrUpdateAuthority(authority);

			return "chat-page";
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			model.addAttribute("error", "There already exists an account with this email");
			return "sign-up-form";
		}
	}

	@GetMapping("/showChatPage")
	public String showChatPage() {
		return "chat-page";
	}
	
    @GetMapping("/api/username")
    @ResponseBody
    public String currentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        ChatUser loggedInUser = chatUserService.findByUserEmail(userEmail);
        return loggedInUser.getUser_name();
    }

	@PostMapping("/privateChat")
	public String privateChat(@RequestParam("username") String username, Model model){
		try{
			ChatUser foundUser = chatUserService.findByUsername(username);
			if(foundUser == null){
				model.addAttribute("errorMessage2","Sorry! User does not exist");
				return "chat-page";
			}
			model.addAttribute("receiver",foundUser.getUser_name());
			return "message-page";
		}catch (Exception e){
			model.addAttribute("errorMessage2","Oops! Error retrieving the user. Please try later");
			return "chat-page";
		}
	}

}
