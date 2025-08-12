package com.example.GenAI.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{

	private static String uploadDir = "uploads";//server folder
	
	@Override
	public String uploadFiles(MultipartFile file) {
		
				
		/*case empty file*/
		if(file.isEmpty()) {
			return "isEmpty";
		}
				
		
		try {
			
			//create folder if not exist
			Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(uploadPath);
			
			
			//upload the file 
			Path filePath = uploadPath.resolve(file.getOriginalFilename());
			file.transferTo(filePath);
			
			//check if attachments exists in case email reading
			String checkExt = file.getOriginalFilename();
			if(checkExt.endsWith(".eml")) {
				Path parentDir = Paths.get(filePath.toString()).getParent();
				Path attachmentDir = parentDir.resolve("attachments");
				if(!Files.exists(attachmentDir)) {
					System.out.println("attachmentDir: "+ attachmentDir);
					return "-2";
				}
			}
			
			
			//get the string path
			String fullpath = filePath.toString();
			
			return fullpath;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Something went wrong reading the file";
		}
		
		
	}

	@Override
	public List<String> uploadZip(MultipartFile zipFile) {
		
		/*store the paths of unziped files */
		List<String> unzipedPaths = new ArrayList<String>();
		
		//get the path of uploads folder
		Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
		
		
		try {
			
			/*create folder if not exists*/
			Files.createDirectories(uploadPath);
			
			/*open zip file */
			ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
			
			/*every file inside zip*/
			ZipEntry zipEntry = zis.getNextEntry();
			
			/*for every ziped file*/
			while(zipEntry != null) {
				
				//create the path for unzip file 
				Path filePath = uploadPath.resolve(zipEntry.getName());
				
				/*case entry is folder*/
				if(zipEntry.isDirectory()) {
					Files.createDirectories(filePath);
					zipEntry = zis.getNextEntry();
					continue;
				}
				
				/*case is file*/
				if(!zipEntry.isDirectory()) {
					Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);//unzip
				}
				
				/*add paths*/
				unzipedPaths.add(filePath.toString());
				System.out.println("Unzip: "+ filePath.toString());//test
				zipEntry = zis.getNextEntry(); //go to next file
			}
			
			return unzipedPaths;
			
			
		} catch (IOException e) {
			e.printStackTrace();
			unzipedPaths.add("Something went wrong reading the zip file");
			return unzipedPaths;
			
		}
		
		
	}
	
	

	
	
}
