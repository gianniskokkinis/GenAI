package com.example.GenAI.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.GenAI.Services.FileService;
import com.example.GenAI.Services.ManagerService;
import com.example.GenAI.Services.UserService;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.User;

@SpringBootTest
class FileServiceTests {

	@Autowired
	FileService fileService;
	
	
	/*load zip file*/
	@Test
	void testLoadZip() {
		

		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/emails/emails.zip");
		
		try {
			
	        //create mock 
	        MultipartFile file = new MockMultipartFile(
	            "file",                     
	            resource.getFilename(),     
	            "message/rfc822",           
	            resource.getInputStream()   
	        );
	        
	        //check
	        List<String> filePaths = fileService.uploadZip(file);
	        assertTrue(!filePaths.isEmpty());
			
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		
	}
	
	@Test
	void testLoadFile(){
	
		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/emails/email_01.eml");
		
		try {
			 //create mock 
	        MultipartFile file = new MockMultipartFile(
	            "file",                     
	            resource.getFilename(),     
	            "message/rfc822",           
	            resource.getInputStream()   
	        );
	        
	        String path = fileService.uploadFiles(file);
	        assertTrue(path!=null);
			
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
	}
	
	

}
