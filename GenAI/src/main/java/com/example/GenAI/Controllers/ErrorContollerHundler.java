package com.example.GenAI.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class ErrorContollerHundler implements ErrorController{
	
	
	@RequestMapping("/error")
	public String handleError() {
		
		//this is in case something went wrong
		return "error/errorpage";
	}

}