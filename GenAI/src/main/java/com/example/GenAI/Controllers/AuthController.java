package com.example.GenAI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.GenAI.Services.UserService;

@Controller
public class AuthController {
	
	@Autowired
    UserService userService;
    

    @RequestMapping("/login")
    public String login(){
        return "auth/signin";
    }
    
    
    

}
