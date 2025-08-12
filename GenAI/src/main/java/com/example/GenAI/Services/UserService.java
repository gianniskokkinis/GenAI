package com.example.GenAI.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.GenAI.model.User;

@Service
public interface UserService {

	public boolean isUserPresent(User user);
	
	public UserDetails loadUserByUsername(String username);
	
	public User findUserByUsername(String Username);
	
	public void saveUser(User user);
	
}
