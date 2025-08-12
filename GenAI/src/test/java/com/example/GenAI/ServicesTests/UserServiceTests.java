package com.example.GenAI.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.example.GenAI.Services.FileService;
import com.example.GenAI.Services.ManagerService;
import com.example.GenAI.Services.UserService;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.User;

@SpringBootTest
class UserServiceTests {

	@Autowired
	UserService userService;
	
	/*uncomment this only if you want store users to system*/
//	@Test
//	void getUploadedPath() {
//		
//		User user = new User();
//		user.setUsername("admin");
//		user.setPassword("admin");
//		userService.saveUser(user);
//		
//	}
	
	

}
