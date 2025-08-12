package com.example.GenAI.Services;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.regex.*;
import edu.stanford.nlp.simple.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.annotations.processing.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.GenAI.DAO.ClientDAO;
import com.example.GenAI.DAO.InvoiceDAO;
import com.example.GenAI.GenAiApplication;
import com.example.GenAI.model.Client;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.Invoice;
import com.example.GenAI.model.Request;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;


@Service
public class ManagerServiceImpl implements ManagerService{
	
	@Autowired
	ClientDAO clientDao;
	
	@Autowired
	InvoiceDAO invoiceDao;
	
	


	@Override
	public List<Data> readData(List<String> filePaths) {
		
		/*Return this data*/
		List<Data> collectedData = new ArrayList<Data>();
		
		
		
		try {
		
			String type = "UNKNOWN";
			/*read all files*/
			for (String getPath: filePaths) {
				
				//System.out.println("filepaths: "+ getPath); 
				
				if (getPath.endsWith(".eml")) {
					/*case read email*/
					type = "EMAIL";
					Data data = readEmail(getPath);
					data.setWarning(getWarning(data));
					if(!isDataExist(data, collectedData)) {
						collectedData.add(data);
					}
				}else {
					
					if(getPath.endsWith(".pdf")) {
						/*case read pdf*/
						Data data = readPDF(getPath);
						data.setWarning(getWarning(data));
						if(!isDataExist(data,collectedData)) {
							collectedData.add(data);
						}
						
					}else {
						if (getPath.endsWith(".html")) {
							/*case read html*/
							Data data = readFile(getPath);
							data.setWarning(getWarning(data));
							//System.out.println("Type: "+ data.getType());//test
							if(!isDataExist(data,collectedData)) {
								collectedData.add(data);
							}
						}else {
							/*File is not supported*/
							Data data = new Data();
							String error = "This file is not supported:\n"+getPath;
							data.setError(error);
							collectedData.add(data);
							return collectedData;
						}
						
					}
					
					
				}
				
				
			}
		
			
			
		} catch (Exception e) {
			System.out.println("Unable to process your request. Please try again later or contact support if the issue persists");
			e.printStackTrace();
		}
		
		
		return collectedData;
		
		
	}
	
	/*For forms and invoices*/
	private Data readFile(String path) {
		Data data = new Data();
		
		try {
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			data.setSource(file.getName());
			String line;
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				
				
				/*change type to form*/
				if(line.contains("Φόρμα Επικοινωνίας - TechFlow Solutions")) {
					data.setType("FORM");
				}
				
				
				/*change type to Invoice*/
				if(containsWordWithoutAccents(line,"τιμολογιο")) {
					data.setType("INVOICE");
				}
				
				
				/**
				 * 	For form 
				 * 
				 * */
				
				/*get the name*/
				if (line.contains("Όνομα και Επώνυμο:")) {
					
					
					try {
						line = scanner.nextLine();
						String name = line.split("value=\"")[1].split("\"")[0];
						data.setClientName(name);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
					
				}
				
				/*getEmail*/
				if(line.contains("<label>Email:</label>")) {
					
					
					try {
						line = scanner.nextLine();
						String email = line.split("value=\"")[1].split("\"")[0];
						data.setEmail(email);
					} catch (Exception e) {
						e.printStackTrace();
						continue;// TODO: handle exception
					}
					
					
				}
				
				/*get phone*/
				if(line.contains("Τηλέφωνο:")) {
					
					
					try {
						line = scanner.nextLine();
						String phone = line.split("value=\"")[1].split("\"")[0];
						data.setPhone(phone);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get company*/
				if(line.contains("Εταιρεία:")) {
					
					
					try {
						line = scanner.nextLine();
						String company = line.split("value=\"")[1].split("\"")[0];
						data.setCompany(company);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get service of interest*/
				if(line.contains("Υπηρεσία Ενδιαφέροντος:")) {
					
					try {
						scanner.nextLine();
						line = scanner.nextLine();
						String interestService = line.split("selected>")[1].split("<")[0];
						data.setServiceInterest(interestService);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get message*/
				if(line.contains("Μήνυμα:")) {
					
					try {
						line = scanner.nextLine();
						String message = line.split("readonly>")[1].split("<")[0];
						data.setMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get date*/
				if(line.contains("Ημερομηνία Υποβολής:")) {
					
					try {
						line = scanner.nextLine();
						String date = line.split("value=\"")[1].split("\"")[0].split("T")[0];
						String[] elements = date.split("-");
						data.setDate(elements[2]+"-"+elements[1]+"-"+elements[0]);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get priority*/
				if(line.contains("Προτεραιότητα:")) {
					
					try {
						
						scanner.nextLine();
						line = scanner.nextLine();
						String priority = line.split("selected>")[1].split("<")[0];
						if(priority.equals("Υψηλή")) {
							data.setPriority("HIGH");
						}else {
							if(priority.equals("Μέτρια")) {
								data.setPriority("MEDIUM");
							}else {
								data.setPriority("LOW");
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
					
				}
				
				
				/**
				 * 
				 * For Invoice
				 * 
				 * */
				
				
				/*Get client*/
				if(line.contains("<strong>Πελάτης:</strong>")) {
					
					try {
						line = scanner.nextLine();
						String clientName = line.split("<br>")[0].trim();
						data.setClientName(clientName);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get invoice number*/
				if(line.contains("Αριθμός:")) {
					
					try {
						String invoiceNumber = line.split("</strong>")[1].split("<br>")[0];
						data.setInvoiceNumber(invoiceNumber);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get date */
				if(line.contains("Ημερομηνία:")) {
					
					try {
						String date = line.split("</strong>")[1].split("<br>")[0].replace("/","-");
						data.setDate(date);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get amount*/
				if(line.contains("Καθαρή Αξία:")) {
					
					try {
						line = scanner.nextLine();
						String amount = line.split("<strong>€")[1].split("</strong>")[0];
						data.setAmmount(amount.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get VAT*/
				if(line.contains("ΦΠΑ")) {
					
					try {
						line = scanner.nextLine();
						String vat = line.split("<strong>€")[1].split("</strong>")[0];
						data.setVAT(vat.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get total amount*/
				if(line.contains("ΣΥΝΟΛΟ:")) {
					
					try {
						line = scanner.nextLine();
						String totalAmount = line.split("<strong>€")[1].split("</strong>")[0];
						data.setTotalAmmount(totalAmount.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
			}
			
		} catch (Exception e) {
			System.out.println("Error: Unable to read the file: \n"+path);
			e.printStackTrace();
			data = new Data();
			data.setWarning(true);
		}
		
		return data;
		
	}
	
	/*Read email*/
	private Data readEmail(String path) {
		
		Data data = new Data();
		
		
		
		try {
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			String line;
			data.setSource(file.getName());
			data.setType("EMAIL");
			
			String emailBody = "";
			boolean readBody = false;
			
			
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				
				/*Case email references to invoice then change type*/
				if(containsWordWithoutAccents(line,"τιμολογιο")){
					
					//get the last line 
					while(scanner.hasNextLine()) {
						line = scanner.nextLine();
					}
					
					String invoiceFileName = line.split(":")[1].split("]")[0].trim();
					scanner.close();
					
					Path absolutePath = Paths.get(path);
					String updatePath = absolutePath
							.getParent() //move back to emails
							.resolve("attachments").resolve(invoiceFileName)+"";
			
					String tempMail = data.getEmail();
					data = readPDF(updatePath);
					data.setEmail(tempMail);
					data.setType("INVOICE");
					return data;
					
				}
				/*Get email and client possible name*/
				if (line.startsWith("From:")) {
					
					try {
						String[] NameAndEmail = line.split(":")[1].split("<");
						data.setClientName( NameAndEmail[0]);
						String email  = NameAndEmail[1].split(">")[0];
						data.setEmail(email);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				/*Get interest service*/
				if(line.startsWith("Subject:")) {
					try {
						String interestService = line.split(":")[1];
						data.setServiceInterest(interestService);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				
				/*Get the Date*/
				if (line.startsWith("Date:")) {
					SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH");
					String givenDate = line.split(":")[1].trim();
					try {
						Date date = inputFormat.parse(givenDate);
						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
						data.setDate(outputFormat.format(date));  
					} catch (ParseException e) {
						System.out.println("Can't convert the date!");
						e.printStackTrace();
						continue;
					}	
				}
				
				/*Get name*/
				if (line.toLowerCase().contains("όνομα") || line.toLowerCase().contains("Ονομα") || line.contains("name")) {
					try {
						String clientName = line.split(":")[1];
						data.setClientName(clientName);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
				
				/*Email*/
				if (line.toLowerCase().contains("email")) {
					
					try {
						String email = line.split(":")[1];
						data.setEmail(email);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				/*get phone number*/
				if (containsWordWithoutAccents(line,"κινητο") || line.toLowerCase().contains("phone") || containsWordWithoutAccents(line,"τηλεφωνο")) {
					
					try {
						int checkLength = line.split(":").length;
						if(checkLength>1) {
							String phone = line.split(":")[1];
							data.setPhone(phone);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
					
					
				}
				/*get company*/
				if (containsWordWithoutAccents(line,"εταιρια") || containsWordWithoutAccents(line,"εταιρεια") || line.toLowerCase().contains("company")) {
					
					try {
						int checkLength = line.split(":").length;
						if(checkLength>1) {
							String company = line.split(":")[1];
							data.setCompany(company);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				/*get address*/
				if (containsWordWithoutAccents(line,"διευθυνση") || line.toLowerCase().contains("address")) {
					
					try {
						int checkLength = line.split(":").length;
						if(checkLength>1) {
							String address = line.split(":")[1];
							data.setAddress(address);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*For priority and Message*/
				if(line.contains("Content-Type:")) {
					readBody = true;
					if(scanner.hasNextLine()) {
						line = scanner.nextLine();
					}
					
				}
				/*read email body lines*/
				if(readBody) {
					emailBody += line + "\n";
				}
			}
			
			scanner.close();
			
			/*set message and priority*/
			Data cpData = getPrioAndMessage(emailBody);
			data.setBodyMail(emailBody);
			data.setPriority(cpData.getPriority());
			data.setMessage(cpData.getMessage());
			
			
			
			
		} catch (FileNotFoundException e) {
			Path absolutePath = Paths.get(path);
			System.out.println("Unable to read the file: '" + absolutePath.getFileName() +"'");
			e.printStackTrace();
		}
		
		
		
		
		return data;
	}
	
	/*read PDF files*/
	private Data readPDF(String path) {
		Data data = new Data();
		
		
		try {
			File file = new File(path);
			data.setSource(file.getName());
			data.setType("INVOICE");
			PDDocument document = Loader.loadPDF(file);
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			String[] lines = text.split("\n");
			
			
			int i;
			for(i=0; i<lines.length; i++) {
				
				
				/*get invoice number*/
				if(lines[i].contains("Αριθμός:")) {
					
					try {
						String invoiceNumber = lines[i].split(":")[1].trim();
						data.setInvoiceNumber(invoiceNumber);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get date*/
				if(lines[i].contains("Ημερομηνία:")) {
					
					try {
						String date = lines[i].split(":")[1].trim().replace("/", "-");
						data.setDate(date);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get client*/
				if(lines[i].contains("Πελάτης:")) {
					i+=1;
					try {
						data.setClientName(lines[i].trim());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
				}
				
				/*get Amount*/
				if(lines[i].contains("Καθαρή Αξία:")) {
					
					try {
						String amount = lines[i].split(":")[1].trim().split("€")[1];
						data.setAmmount(amount.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get VAT*/
				if(lines[i].contains("ΦΠΑ")) {
					
					try {
						String VAT = lines[i].split(":")[1].trim().split("€")[1];
						data.setVAT(VAT.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				}
				
				/*get total amount*/
				if(lines[i].contains("ΣΥΝΟΛΟ:")) {
					
					try {
						String totalAmount = lines[i].split(":")[1].trim().split("€")[1];
						data.setTotalAmmount(totalAmount.replace(",", ""));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					
				
				}
				
				
			}
			
			
			
			
		} catch (IOException e) {
			System.out.println("Error: Unable to read the file:/n" + path);
			Path absolutePath = Paths.get(path);
			data.setError("Unable to read the file. '"+ absolutePath.getFileName()+"'");
			e.printStackTrace();
			return data;
		}
		
		
		return data;
	}
	
	
	/*To remove accents from greek words*/
	private boolean containsWordWithoutAccents(String line, String word) {
		String normalizeLine = removeAccents(line.toLowerCase());
		String normalizeWord = removeAccents(word.toLowerCase());
		return normalizeLine.contains(normalizeWord);
		
	}
	
	private String removeAccents(String text) {
		String normalize = Normalizer.normalize(text.toLowerCase() , Normalizer.Form.NFD);
		String fixed = normalize.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return fixed;
	}
	
	/*This function is about getting the importan information from email body*/
	private Data getPrioAndMessage(String text) {
		
		Data tempData = new Data();
		tempData.setBodyMail(text);
		
		/*For priorities*/
		String[] urgentKeywords = { "επειγον", "urgent", 
				"ASAP", "CRM", "συστημα", "app", "επειγουσα",
				"αναγκη", "αμεση", "κατεπειγον", "αμεσα", "αμεσως",
				"συντομοτερα", "προτεραιοτητα",
				"κρισιμο", "επικινδυνο"};
		
		String[] mediumKeywords = {
				"τιμολογιο", "invoice"
		};
		
		
		/*for set message*/
		List<String> keywords = Arrays.asList(
				"χρειαζόμαστε", "θέλουμε", "ανάγκη", 
			    "επείγον", "άμεσα", "προθεσμία"
		);
		
		List<String> contactKeywords = Arrays.asList(
		        "στοιχεία επικοινωνίας", "επικοινωνία", "όνομα", "email", 
		        "τηλέφωνο", "εταιρεία", "θέση", "τοποθεσία", "διεύθυνση"
		);
		
		
		
		
		/*Initialize*/
		
		String priority = "Low";
		
		for(String key: urgentKeywords) {
			if(containsWordWithoutAccents(text,key)) {
				priority = "High";
				break;
			}
		}
		
		if(priority.equals("Low")) {
			for(String key: mediumKeywords) {
				if(containsWordWithoutAccents(text,key)) {
					priority = "Medium";
					break;
				}
			}
		}
		
		tempData.setPriority(priority);
		
		/*get the matching words*/
		Document doc = new Document(text);
		List<String> sentences = doc.sentences().stream().map(Sentence::text).collect(Collectors.toList());
		
		for(String sent: sentences) {
			String checkSent = sent.toLowerCase();
			
			if(contactKeywords.stream().anyMatch(checkSent::contains)) {
				continue;
			}
			
			if(keywords.stream().anyMatch(checkSent::contains)) {
				tempData.setMessage(sent.trim());
				return tempData;
			}
		}
		
		tempData.setMessage(sentences.get(0));
		return tempData;
	}

	@Override
	public List<Data> fixData(List<Data> newData, List<Data> historyData) {
		
		List<Data> fixedData = new ArrayList<Data>();
		
		List<String> historySources = new ArrayList<String>();
		
		//get the sources
		for(Data histData: historyData) {
			historySources.add(histData.getSource().trim());
		}
		
		
		//if source match then we don't have to add again
		for(Data nData: newData) {
			if (!(historySources.contains(nData.getSource().trim()))) {
				fixedData.add(nData);
			}
		}
		
		
		
		
		return fixedData;
	}

	@Override
	public void saveClient(Data data) {
		
		Client client;
		
		//check if client exist
		List<Client> existClient = clientDao.findByEmail(data.getEmail());
		if(existClient.size()!=0) {
			client = existClient.get(0);
		}else {
			//save new client
			client = new Client();	
		}	
		
		client.setName(data.getClientName());
		client.setEmail(data.getEmail());
		client.setPhone(data.getPhone());
		client.setCompany(data.getCompany());
		client.setInterestServices(data.getServiceInterest());
		List<Request> clientReq = client.getRequests();
		if(clientReq==null) {
			clientReq = new ArrayList<Request>();
		}	
		
		//add the request
		Request req = new Request();
		req.setDate(data.getDate());
		req.setPriority(data.getPriority());
		req.setImportantInfo(data.getMessage());
		//System.out.println("BODYMAIL:" + data.getBodyMail()); //test
		
		req.setMailBody(data.getBodyMail());
		
		//case exists
		if (!(isReqExists(req, clientReq))) {
			clientReq.add(req);
		}
		
		client.setRequests(clientReq);
		
		clientDao.save(client);	
	}
	
	@Override
	public void updateClient(Client client, Data data) {
		client.setName(data.getClientName());
		client.setEmail(data.getEmail());
		client.setPhone(data.getPhone());
		client.setCompany(data.getCompany());
		client.setInterestServices(data.getServiceInterest());
		clientDao.save(client);	
		
	}


	@Override
	public void saveInvoice(Data data) {
		
		
		//create invoice 
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(data.getInvoiceNumber());
		invoice.setDate(data.getDate());
		String fixAmount = data.getAmmount().replace(",", "");
		invoice.setAmount(Double.valueOf(fixAmount));
		String fixVat = data.getVAT().replace(",", "");
		invoice.setFpa(Double.valueOf(fixVat));
		String totalAmount = data.getTotalAmmount().replace(",", "");
		invoice.setTotalAmount(Double.valueOf(totalAmount));
		
		
		
		//check if client exist
		List<Client> existClient = clientDao.findByEmail(data.getEmail());
		if(existClient.size()!=0) {
			
			//get the client
			Client client = existClient.get(0);
			List<Invoice> clientsInvoices = client.getInvoice();
			if(clientsInvoices==null) {
				clientsInvoices = new ArrayList<Invoice>();
			}
			
			//check if invoice exists
			for(Invoice chInvoice: clientsInvoices) {
				if(chInvoice.getInvoiceNumber().equals(invoice.getInvoiceNumber())) {
					return;
				}
			}
			
			//add the new invoice
			clientsInvoices.add(invoice);
			client.setInvoice(clientsInvoices);
			clientDao.save(client);
			
			
		}else {
			
			
			//save new client
			Client client = new Client();
			client.setName(data.getClientName());
			client.setEmail(data.getEmail());
			client.setPhone(data.getPhone());
			client.setCompany(data.getCompany());
			client.setInterestServices(data.getServiceInterest());
			
			//set the invoice
			List<Invoice> invoices = new ArrayList<Invoice>();
			invoices.add(invoice);
			client.setInvoice(invoices);
			
			//save there
			clientDao.save(client);
			
		}
		
		
		
	}
	

	@Override
	public boolean isClientExists(String email) {
		
		List<Client> clients = clientDao.findByEmail(email);
		return clients.size()!=0;
	}

	@Override
	public Client getClient(String email) {
		List<Client> clients = clientDao.findByEmail(email);
		return clients.get(0);
	}

	@Override
	public List<Data> deleteData(String sourceName, List<Data> historyData) {
		
		List<Data> resultData = new ArrayList<Data>();
		for(Data chData: historyData) {
			if(!(chData.getSource().equals(sourceName))) {
				resultData.add(chData);
			}
		}
		
		return resultData;
	}

	@Override
	public List<Client> getAllClients() {
		List<Client> allClients = clientDao.findAll();
		return allClients;
	}

	@Override
	public List<Invoice> getAllInvoices() {
		List<Invoice> allInvoices = invoiceDao.findAll();
		return allInvoices;
	}

	@Override
	public void deleteClientFromDatabase(Client client) {
		Client delClient = clientDao.findByEmail(client.getEmail()).get(0);
		clientDao.deleteById(delClient.getID());
	}

	@Override
	public void deleteInvoiceFromDatabase(Invoice invoice) {
		
		Client client = clientDao.findByEmail(invoice.getClient().getEmail()).get(0);
		
		List<Invoice> clientInvoices = client.getInvoice();
		List<Invoice> fixList = new ArrayList<Invoice>();
		
		
		for(Invoice checkInvoice: clientInvoices) {
			if(!(checkInvoice.getInvoiceNumber().equals(invoice.getInvoiceNumber()))) {
				fixList.add(checkInvoice);
			}
		}
		
		client.setInvoice(fixList);
		clientDao.save(client);
		
		//delete from database
		invoiceDao.delete(invoice);
				
		
	}
	
	private boolean isReqExists(Request req, List<Request> histRequests) {
		
		for(Request chRequest: histRequests) {
			if(req.getImportantInfo().equals(chRequest.getImportantInfo())) {
				return true;
			}
		}
		
		return false;
	}

	

	@Override
	public ByteArrayInputStream exportToCsv(List<Data> data) {
		
		
		
		//initial line 
		String csvContent = "Type,Source,Date,Client_Name,Email,Phone,Company,Service_Interest,Amount,VAT,Total_Amount,Invoice_Number,Priority,Message\n";
		
		
		//get lines 
		for (Data dataToWrite: data) {
			csvContent += dataToWrite.getType()+","+
					dataToWrite.getSource()+","+
					dataToWrite.getDate()+","+
					dataToWrite.getClientName()+","+
					dataToWrite.getEmail()+","+
					dataToWrite.getPhone()+","+
					dataToWrite.getCompany()+","+
					dataToWrite.getServiceInterest()+","+
					dataToWrite.getAmmount()+","+
					dataToWrite.getVAT()+","+
					dataToWrite.getTotalAmmount()+","+
					dataToWrite.getInvoiceNumber()+","+
					dataToWrite.getPriority()+","+
					dataToWrite.getMessage().replace("\n", " ").replace("\r", " ").replace("\"", "\"\"")  	+"\n";
		}
		
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				csvContent.getBytes(StandardCharsets.UTF_8)
		);
		
		return inputStream;
		
		
		
	}

	@Override
	public ByteArrayInputStream exportToXls(List<Data> data) {
		
		
		Workbook workbook =  new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Exported Data");
		
		//initial values 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Type");
		row.createCell(1).setCellValue("Source");
		row.createCell(2).setCellValue("Date");
		row.createCell(3).setCellValue("Client_Name");
		row.createCell(4).setCellValue("Email");
		row.createCell(5).setCellValue("Phone");
		row.createCell(6).setCellValue("Company");
		row.createCell(7).setCellValue("Service_Interest");
		row.createCell(8).setCellValue("Amount");
		row.createCell(9).setCellValue("VAT");
		row.createCell(10).setCellValue("Total_Amount");
		row.createCell(11).setCellValue("Invoice_Number");
		row.createCell(12).setCellValue("Priority");
		row.createCell(13).setCellValue("Message");		
		
		//data 
		
		int index = 1;
		for (Data dataToWrite: data) {
			row = sheet.createRow(index);
			row.createCell(0).setCellValue(dataToWrite.getType());
			row.createCell(1).setCellValue(dataToWrite.getSource());
			row.createCell(2).setCellValue(dataToWrite.getDate());
			row.createCell(3).setCellValue(dataToWrite.getClientName());
			row.createCell(4).setCellValue(dataToWrite.getEmail());
			row.createCell(5).setCellValue(dataToWrite.getPhone());
			row.createCell(6).setCellValue(dataToWrite.getCompany());
			row.createCell(7).setCellValue(dataToWrite.getServiceInterest());
			row.createCell(8).setCellValue(dataToWrite.getAmmount());
			row.createCell(9).setCellValue(dataToWrite.getVAT());
			row.createCell(10).setCellValue(dataToWrite.getTotalAmmount());
			row.createCell(11).setCellValue(dataToWrite.getInvoiceNumber());
			row.createCell(12).setCellValue(dataToWrite.getPriority());
			row.createCell(13).setCellValue(dataToWrite.getMessage());
			index++;
		}
		
		
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		 try {
			 workbook.write(out);
			 return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			System.out.println("Something went wrong exporting to excel");
			e.printStackTrace();
			return null;
		}
		 
		 
		
	
	}
	
	
	private boolean getWarning(Data data) {
		
		if (data.getType().length()==0 || data.getSource().length()==0 || data.getClientName().length()==0 ) {
			return true;
		}
		
		if(data.getType().equals("INVOICE")) {
			if (data.getAmmount().length()==0 || data.getVAT().length()==0 || 
					data.getTotalAmmount().length()==0 || data.getInvoiceNumber().length()==0
					) {
				return true;
			}
		}else {
			if (data.getEmail().length()==0 || data.getPhone().length()==0 || data.getCompany().length()==0 || data.getServiceInterest().length()==0 
					|| data.getPriority().length()==0 || data.getMessage().length()==0
					) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isDataExist(Data newData, List<Data> historyData) {
			
			
			
			List<String> historySources = new ArrayList<String>();
			
			//get the sources
			for(Data histData: historyData) {
				historySources.add(histData.getSource().trim());
			}
			
			
			// check if new data repeat
			if ((historySources.contains(newData.getSource().trim()))) {
				return true;
			}
			
			
			return false;
		}

	@Override
	public List<Data> sort(List<Data> data, String filter) {
		
		//copy 
		List<Data> sortedList = new ArrayList<Data>();
		for(Data cpData: data) {
			sortedList.add(cpData);
		}
		
		
		//sort
		if (filter.equals("name")) {
			Collections.sort(sortedList, Comparator.comparing(Data::getClientName));
		}else {
			if(filter.equals("date")) {
				
				//convert to date 
				for(Data dataToFix: sortedList) {
					try {
						dataToFix.setSortDate(new SimpleDateFormat("dd-MM-yyyy").parse(dataToFix.getDate().trim()));
						//dataToFix.setSortDate(new SimpleDateFormat("yyyy-MM-dd").parse(dataToFix.getDate().trim()));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				Collections.sort(sortedList, Comparator.comparing(Data::getSortDate));
				
			}else {
				
				if(filter.equals("EMAIL")) {
					List<Data> emailSortedList = new ArrayList<>();
					
					//add email first
					for(Data chData: sortedList) {
						if(chData.getType().equals("EMAIL")) {
							emailSortedList.add(chData);
						}
					}
					
					//add others first
					for(Data chData: sortedList) {
						if(!(chData.getType().equals("EMAIL"))) {
							emailSortedList.add(chData);
						}
					}
					return emailSortedList;
					
				}else {
					if(filter.equals("INVOICE")) {
						
						List<Data> invoiceSortedList = new ArrayList<>();
						
						//add email first
						for(Data chData: sortedList) {
							if(chData.getType().equals("INVOICE")) {
								invoiceSortedList.add(chData);
							}
						}
						
						//add others first
						for(Data chData: sortedList) {
							if(!(chData.getType().equals("INVOICE"))) {
								invoiceSortedList.add(chData);
							}
						}
						return invoiceSortedList;
						
					}else {
						
						//FORM
						List<Data> formSortedList = new ArrayList<>();
						
						//add email first
						for(Data chData: sortedList) {
							if(chData.getType().equals("FORM")) {
								formSortedList.add(chData);
							}
						}
						
						//add others first
						for(Data chData: sortedList) {
							if(!(chData.getType().equals("FORM"))) {
								formSortedList.add(chData);
							}
						}
						return formSortedList;
						
					}
				}
				
				
				
			}
		}
		
		
		return sortedList;
	}

	@Override
	public List<Data> search(List<Data> data, String keyword) {
		
		List<Data> searchedData = new ArrayList<Data>();
		
		String[] keys = keyword.split(" ");
		
		for(String key: keys) {
			for (Data chData: data) {
				if ( chData.getType().contains(key) 
						|| chData.getSource().contains(key)
						|| chData.getDate().contains(key)
						|| chData.getClientName().contains(key)
						|| chData.getEmail().contains(key)
						|| chData.getCompany().contains(key)
						|| chData.getAddress().contains(key)
						|| chData.getPhone().contains(key)
						|| chData.getServiceInterest().contains(key)
						|| chData.getAmmount().contains(key)
						|| chData.getVAT().contains(key)
						|| chData.getTotalAmmount().contains(key)
						|| chData.getInvoiceNumber().contains(key)
						|| chData.getPriority().contains(key)
						|| chData.getBodyMail().contains(key)
						) {
					searchedData.add(chData);
				}
			}
		}
		
		return searchedData.reversed();
	}
	
	


	
	
	
	
	
	

}