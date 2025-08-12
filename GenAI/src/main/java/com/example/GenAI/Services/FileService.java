package com.example.GenAI.Services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
	
	/*upload user's data to server*/
	public String uploadFiles(MultipartFile file);

	/*upload user's zip data to server. IMPORTANT FOR EMAILS*/
	public List<String> uploadZip(MultipartFile zipFile);
	
}
