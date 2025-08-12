package com.example.GenAI.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GenAI.DAO.UserDAO;
import com.example.GenAI.model.User;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserDAO userDAO;
	
	
	@Override
	public boolean isUserPresent(User user) {
		Optional<User> storedUser = userDAO.findByUsername(user.getUsername());
		return storedUser.isPresent();
	}

	// Method defined in Spring Security UserDetailsService interface
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// orElseThrow method of Optional container that throws an exception if Optional result  is null
		return userDAO.findByUsername(username).orElseThrow(
	                ()-> new UsernameNotFoundException(
	                        String.format("USER_NOT_FOUND %s", username)
	                ));
	}

	@Override
	public User findUserByUsername(String Username) {
		User user = userDAO.findByUsername(Username).get();
		return user;
	}
	
	
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword()); //encrypt password here
        user.setPassword(encodedPassword); //set password here
        userDAO.save(user);	
    }

}
