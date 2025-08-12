package com.example.GenAI.Controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.example.GenAI.DAO.ClientDAO;
import com.example.GenAI.Services.FileService;
import com.example.GenAI.Services.ManagerService;
import com.example.GenAI.model.Client;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.Invoice;
import com.example.GenAI.model.Request;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;


import org.springframework.ui.Model;

@Controller
public class MainController {
	
	/*for display*/
	List<Data> data = new ArrayList<Data>();
	List<Client> displayClients = new ArrayList<Client>();
	List<Invoice> displayInvoices = new ArrayList<Invoice>();
	List<Data> extractedData = new ArrayList<Data>();
	
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	FileService fileService;
	

	@RequestMapping("/dashboard")
	public String dashboard(@RequestParam(required = false, defaultValue = "Select your file or zip: ")String message, 
			@RequestParam(required = false, defaultValue="primary") String status,Model model) {
		
		//System.out.println("Data length: "+ this.data.size());//test
		this.data = managerService.deleteData("", this.data); //delete error data
		
		model.addAttribute("collectedData", this.data);
		model.addAttribute("message",message);
		model.addAttribute("status", status);	
		
		return "Overview/Overview.html";
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		
		String checkFileName = file.getOriginalFilename();
		String message;
		
		/*check file extension*/
		if(checkFileName.endsWith(".zip")) {
			//System.out.println("======> zip File <======");
			
			/*get file paths of exported files*/
			List<String> filePaths = fileService.uploadZip(file);
			
			/*colect data from email*/
			List<Data> getData = managerService.readData(filePaths);
			
			/*handle error*/
			if((getData.size()==1)) {
				if(getData.get(0).getError().length()!=0) {
					message = getData.get(0).getError();
					message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
					return "redirect:/dashboard?message="+message+"&status=fail"; 
				}
			}
			
			/*fix repeted data*/
			List<Data> fixedData = managerService.fixData(getData, this.data);
			
			/*copy returned data*/
			for(Data cpData: fixedData) {
				this.data.add(cpData);
			}
			
			/*return message*/
			message = fixedData.size()+" new data loaded!";
			
			
		}else {
			if (checkFileName.endsWith(".eml") || checkFileName.endsWith(".html")
					|| checkFileName.endsWith(".pdf")
					) {
				
				/*case load 1 file*/

				/*get the path*/
				String getPath = fileService.uploadFiles(file);
				
				/*error handle*/
				if(getPath.equals("-2")) {
					message = " 'attachemnts' folder is missing! Please insert first the attachments to analyze emails";
					message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
					return "redirect:/dashboard?message="+message+"&status=fail";
				}
				
				/*Collect data from the file*/
				List<String> paths = new ArrayList<String>();
				paths.add(getPath);
				List<Data> getData = managerService.readData(paths);
				
				/*error handle*/
				if(getData.size()==1) {
					if(getData.get(0).getError().length()!=0) {
						message = getData.get(0).getError() + " from '"+checkFileName+"'";
						message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
						return "redirect:/dashboard?message="+message+"&status=fail";
					}
				}
				
				/*fix repeated data*/
				List<Data> fixedData =  managerService.fixData(getData, this.data);
				
				/*copy data*/
				for(Data cpData: fixedData) {
					this.data.add(cpData);
				}
				
				/*return message to user*/
				message = fixedData.size()+" new data loaded!";
				
			}else {
				
				/*return error message to user*/
				message = checkFileName+" file is not supported";
			}
		}
		
		
		//System.out.println("File: "+ file.getOriginalFilename());
//		String getPath = fileService.uploadFiles(file);
//		System.out.println("getPath: "+ getPath);//test
		
		//return the message encoded
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
	}
	
	
	@RequestMapping("/client/save")
	public String clientSave(@ModelAttribute("data") Data data, RedirectAttributes redirectAttrs) {
		
//		System.out.println("Client name: "+ data.getClientName());
//		System.out.println("Email: "+ data.getEmail());
//		System.out.println("Phone: "+ data.getPhone());
//		System.out.println("Company: "+ data.getCompany());
//		System.out.println("Service Interest: "+ data.getServiceInterest());
//		System.out.println("Priority: "+ data.getPriority());
//		System.out.println("Message: "+ data.getMessage());
		
		managerService.saveClient(data);
		
		
		String message="Client '"+data.getClientName()+"' has been stored sucessfully!";
		
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
	}
	
	@RequestMapping("/invoice/save")
	public String invoiceSave(@ModelAttribute("data") Data data, RedirectAttributes redirectAttrs) {
		
		managerService.saveInvoice(data);
		String message="Invoice '"+data.getInvoiceNumber()+"' has been stored sucessfully!";
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
	}
	
	
	
	
	
	@RequestMapping("/decline")
	public String declineData(@RequestParam int index) {
		
		Data removeData = this.data.get(index);
		
		this.data = managerService.deleteData(removeData.getSource(), this.data);
		
		String messsage = removeData.getSource()+" has been declined!";
		messsage = URLEncoder.encode(messsage,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+messsage+"&status=success";
	}
	
	@RequestMapping("/search")
	public String search(@RequestParam String keyword, Model model) {
		
		
		List<Data> searchData = managerService.search(this.data, keyword);
		
		String message = searchData.size() +" results for '"+keyword+"'";
		
		model.addAttribute("collectedData", searchData);
		model.addAttribute("message",message);
		model.addAttribute("status", "success");	
		
		return "Overview/Overview.html";
	}
	
	@RequestMapping("/sort")
	public String sortData(@RequestParam String type) {
		
		this.data = managerService.sort(this.data, type);
		
		String message = "Data sorted by "+type;
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
	}
	
	@RequestMapping("/edit")
	public String editData(@RequestParam int index,@RequestParam String mode, Model model) {
		
		Data checkData = this.data.get(index);
		
		model.addAttribute("data",checkData);
		if(mode.equals("save")) {
			/*case store to database*/
			model.addAttribute("mode", "save");
		}else {
			/*case store to file output*/
			model.addAttribute("mode", "export");
		}
		
		
		if (checkData.getType().equals("INVOICE")) {
			/*case invoice*/
			return "Overview/EditInvoice.html";
		}
		/*case client*/
		return "Overview/SaveClient.html";
		
		
	}
	
	@RequestMapping("/clients")
	public String clients(Model model) {
		
		displayClients = managerService.getAllClients();
		
		model.addAttribute("clientsList", displayClients);
		
		return "Manage/Clients.html";
	}
	
	@RequestMapping("/invoices")	
	public String invoices(Model model) {
		
		displayInvoices = managerService.getAllInvoices();
		
		model.addAttribute("invoicesList",displayInvoices);
		
		return "Manage/Invoices.html";
	}
	
	@RequestMapping("/client/view")
	public String editClient(@RequestParam int index, Model model) {
		
		Client client = this.displayClients.get(index);
		
		List<Request> displayRequests = client.getRequests();
		List<Invoice> displayInvoices = client.getInvoice();
		
		model.addAttribute("client", client);
		model.addAttribute("displayRequests", displayRequests);
		model.addAttribute("displayInvoices",displayInvoices);
		
		return "Manage/ViewClient.html";
	}
	
	@RequestMapping("/client/delete")
	public String removeClient(@RequestParam int index) {
		Client client = this.displayClients.get(index);
		
		managerService.deleteClientFromDatabase(client);
		
		String message = "Client '"+client.getName()+"' has been removed";
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=fail";
	}
	
	@RequestMapping("/invoice/delete")
	public String removeInvoice(@RequestParam int index) {
		Invoice invoice = this.displayInvoices.get(index);
		
		managerService.deleteInvoiceFromDatabase(invoice);
		
		String message = "Invoice '"+invoice.getInvoiceNumber()+"' has been removed";
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=fail";
	}
	
	@RequestMapping("/data/extract")
	public String extractData(@ModelAttribute("data") Data data, RedirectAttributes redirectAttrs) {
		
		
		
		//add data to export
		this.extractedData.add(data);
		
	
		//remove from display 
		this.data = managerService.deleteData(data.getSource(), this.data);
		
		
		
		//return the message
		String message = "Data '"+ data.getSource() +"' has been extracted succesfully!";
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
	}
	
	@RequestMapping("/data/extracted")
	public String displayExtractedData(HttpSession session, Model model) {
		
		model.addAttribute("exportedData", this.extractedData);
		session.setAttribute("extData", this.extractedData);//for google sheets controller
		
		
		return "Extract/ExtractedData.html";
	}
	
	@RequestMapping("/data/delete")
	public String deleteExtractData(@RequestParam int index) {
		
		System.out.println("Delete Data");
		
		Data delData = this.extractedData.get(index);
		
		this.extractedData = managerService.deleteData(delData.getSource(), this.extractedData);
		
		return "redirect:/data/extracted";
		
	}
	
	@RequestMapping("/export")
	public ResponseEntity<Resource> exportData(@RequestParam String type, RedirectAttributes redirectAttrs) {
		
		
		if(type.equals("csv")) {
			
			/*case csv*/
			ByteArrayInputStream inputStream = managerService.exportToCsv(this.extractedData);
			
			
			
			return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION, 
			               "attachment; filename=output.csv")
			        .contentType(MediaType.parseMediaType("text/csv"))
			        .body(new InputStreamResource(inputStream));
			 
		}
		
		/*case excel*/
		ByteArrayInputStream inputStream = managerService.exportToXls(this.extractedData);
		
		
		
		
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, 
		               "attachment; filename=output.xlsx")
		        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		        .body(new InputStreamResource(inputStream));
		
	}
	
	
	@RequestMapping("/delete/export/data")
	public String deleteExportData() {
		this.extractedData = new ArrayList<Data>();
		String message = "Data for export has been delete!";
		message = URLEncoder.encode(message,  StandardCharsets.UTF_8);
		return "redirect:/dashboard?message="+message+"&status=success";
		
	}
	
	

}