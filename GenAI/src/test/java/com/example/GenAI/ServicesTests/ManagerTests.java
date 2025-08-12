package com.example.GenAI.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.GenAI.Services.ManagerService;
import com.example.GenAI.model.Client;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.Invoice;
import com.example.GenAI.model.Request;

import jakarta.transaction.Transactional;

import org.springframework.core.io.ClassPathResource;

@SpringBootTest
@Transactional
class ManagerTests {

	@Autowired
	ManagerService managerService;
	

	@Test
	void readEmail() {
		
		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/emails/email_01.eml");
		
		try {
			paths.add(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		List<Data> checkData = managerService.readData(paths);
		
		
		Data getData = checkData.get(0);
		
		
//
//			System.out.println("----------------------------------------------------");
//			System.out.println("Type: "+ getData.getType());
//			System.out.println("Source: "+ getData.getSource());
//			System.out.println("Client name: "+ getData.getClientName());
//			System.out.println("email: "+ getData.getEmail());
//			System.out.println("Date: "+ getData.getDate());
//			System.out.println("Company: "+ getData.getCompany());
//			System.out.println("Address: "+ getData.getAddress());
//			System.out.println("Phone: "+ getData.getPhone());
//			System.out.println("Priority: "+ getData.getPriority());
//			System.out.println("Message: "+ getData.getMessage());
//			System.out.println("Amount: "+ getData.getAmmount());
//			System.out.println("VAT: "+ getData.getVAT());
//			System.out.println("Total Amount: "+ getData.getTotalAmmount());
//			System.out.println("Invoice Number: "+ getData.getInvoiceNumber());
//
//		
		
		
		assertTrue(
				getData.getType().equals("EMAIL")
				&& getData.getSource().equals("email_01.eml")
				&& getData.getClientName().equals(" Σπύρος Μιχαήλ")
				&& getData.getEmail().equals(" spyros.michail@techcorp.gr")
				&& getData.getDate().equals("20-01-2024")
				&& getData.getCompany().equals(" TechCorp AE")
				&& getData.getPhone().equals(" 210-3344556")
				&& getData.getPriority().equals("High")
				&& getData.getMessage().equals("Καλησπέρα,\n"
						+ "\n"
						+ "Είμαι ο Σπύρος Μιχαήλ από την TechCorp AE και θα θέλαμε να συζητήσουμε για ένα σύστημα CRM.")
				
				);
	}
	
	
	
	
	@Test
	void readForms() {
		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/forms/contact_form_1.html");
		
		try {
			paths.add(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		List<Data> checkData = managerService.readData(paths);
		Data data = checkData.get(0);
		

//			System.out.println("----------------------------------------------------");
//			System.out.println("Type: "+ data.getType());
//			System.out.println("Source: "+ data.getSource());
//			System.out.println("Date: "+ data.getDate());
//			System.out.println("Client name: "+ data.getClientName());
//			System.out.println("email: "+ data.getEmail());
//			System.out.println("Phone: "+ data.getPhone());
//			System.out.println("Company: "+ data.getCompany());
//			System.out.println("Interest Service: "+ data.getServiceInterest());
//			System.out.println("Priority: "+ data.getPriority());
//			System.out.println("Message: "+ data.getMessage());
//			


		
		
		assertTrue(
				data.getType().equals("FORM")
				&& data.getSource().equals("contact_form_1.html")
				&& data.getDate().equals("15-01-2024")
				&& data.getClientName().equals("Νίκος Παπαδόπουλος")
				&& data.getEmail().equals("nikos.papadopoulos@example.gr")
				&& data.getPhone().equals("210-1234567")
				&& data.getCompany().equals("Digital Marketing Pro")
				&& data.getServiceInterest().equals("Ανάπτυξη Website")
				&& data.getPriority().equals("HIGH")
				&& data.getMessage().equals("Χρειαζόμαστε ένα νέο e-commerce website για την εταιρεία μας. Έχουμε περίπου 200 προϊόντα και θέλουμε integration με το ERP μας.")
				
				
				);
	}
	
	@Test
	void readInvoices() {
		
		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/invoices/invoice_TF-2024-001.html");
		
		try {
			paths.add(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		List<Data> checkData = managerService.readData(paths);
		Data data = checkData.get(0);
		

//			System.out.println("----------------------------------------------------");
//			System.out.println("Type: "+ data.getType());
//			System.out.println("Source: "+ data.getSource());
//			System.out.println("Client_name: "+ data.getClientName());
//			System.out.println("Email: "+ data.getEmail());
//			System.out.println("Phone: "+ data.getPhone());
//			System.out.println("Company: "+ data.getCompany());
//			System.out.println("Message: "+ data.getMessage());
//			System.out.println("Date: "+ data.getDate());
//			System.out.println("invoice_number"+ data.getInvoiceNumber());
//			System.out.println("Amount: "+ data.getAmmount());
//			System.out.println("VAT: "+ data.getVAT());
//			System.out.println("Total Amount: "+ data.getTotalAmmount());

		
		
		
		assertTrue(
				data.getType().equals("INVOICE")
				&& data.getSource().equals("invoice_TF-2024-001.html")
				&& data.getClientName().equals("Office Solutions Ltd")
				&& data.getDate().equals(" 21-01-2024")
				&& data.getInvoiceNumber().equals(" TF-2024-001")
				&& data.getAmmount().equals("850.00")
				&& data.getVAT().equals("204.00")
				&& data.getTotalAmmount().equals("1054.00")
			);
	}
	
	@Test
	void readInvoicesPDF() {
		
		
		List<String> paths = new ArrayList<String>();
		ClassPathResource resource = new ClassPathResource("dummy_data/emails/attachments/invoice_TF-2024-001.pdf");
		
		try {
			paths.add(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
		List<Data> checkData = managerService.readData(paths);
		Data data = checkData.get(0);
		
		

//			System.out.println("----------------------------------------------------");
//			System.out.println("Type: "+ data.getType());
//			System.out.println("Source: "+ data.getSource());
//			System.out.println("Client_name: "+ data.getClientName());
//			System.out.println("Email: "+ data.getEmail());
//			System.out.println("Phone: "+ data.getPhone());
//			System.out.println("Company: "+ data.getCompany());
//			System.out.println("Message: "+ data.getMessage());
//			System.out.println("Date: "+ data.getDate());
//			System.out.println("invoice_number: "+ data.getInvoiceNumber());
//			System.out.println("Amount: "+ data.getAmmount());
//			System.out.println("VAT: "+ data.getVAT());
//			System.out.println("Total Amount: "+ data.getTotalAmmount());

			
		
		
		assertTrue(
					data.getType().equals("INVOICE")
					&& data.getSource().equals("invoice_TF-2024-001.pdf")
					&& data.getClientName().equals("Office Solutions Ltd")
					&& data.getDate().equals("21-01-2024")
					&& data.getInvoiceNumber().equals("TF-2024-001")
					&& data.getAmmount().equals("850.00")
					&& data.getVAT().equals("204.00")
					&& data.getTotalAmmount().equals("1054.00")
				);
	}
	
	@Test
	void fixDataTest() {
		
		List<Data> oldDataList = new ArrayList<Data>();
		List<Data> newDataList = new ArrayList<Data>();
		
		//add old data
		Data dataOld1 = new Data();
		dataOld1.setSource("data1");
		Data dataold2 = new Data();
		dataold2.setSource("data2");
		oldDataList.add(dataOld1);
		oldDataList.add(dataold2);
		
		//add new data
		Data newData1 = new Data();
		newData1.setSource("data1");
		Data newData2 = new Data();
		newData2.setSource("data3");
		newDataList.add(newData1);
		newDataList.add(newData2);
		
		List<Data> resultData = managerService.fixData(newDataList, oldDataList);
		//System.out.println("ResultData Size: "+ resultData.size());
		
		assertTrue(resultData.size()==1);
		
		
	}
	

	
	@Test
	void testClient() {
		
		Data data = new Data();
		data.setEmail("test");
		data.setPhone("test");
		managerService.saveClient(data);
		
		Client checkClient = managerService.getClient(data.getEmail());
		
		assertTrue(
					checkClient.getPhone().equals(data.getPhone())
				);
		
		
		
	}
	
	@Test
	void testUpdateClient() {
		
		Data data = new Data();
		data.setEmail("test");
		data.setPhone("test");
		managerService.saveClient(data);
		
		Client client = managerService.getClient(data.getEmail());
		
		
		data.setPhone("123");
		managerService.updateClient(client, data);
	
		Client checkClient = managerService.getClient(data.getEmail());
		
		assertTrue(
				checkClient.getPhone().equals(data.getPhone())
				);
	}
	
	@Test
	void testInvoice() {
		
		Data data = new Data();
		data.setEmail("test");
		data.setAmmount("1.2");
		data.setVAT("1.2");
		data.setTotalAmmount("1.2");
		data.setInvoiceNumber("TF-TEST");
		managerService.saveInvoice(data);
		
		Client checkClient = managerService.getClient(data.getEmail());
		Invoice checkInvoice = checkClient.getInvoice().get(0);
		

		assertTrue(checkInvoice.getInvoiceNumber().equals(data.getInvoiceNumber()));
		
	}
	
	@Test 
	void testClientExists() {
		
		Data data = new Data();
		data.setEmail("test");
		data.setPhone("test");
		managerService.saveClient(data);
		
		assertTrue(managerService.isClientExists(data.getEmail()));
		
	}
	
	@Test
	void testGetAllClients() {
		
		Data data = new Data();
		data.setEmail("test");
		managerService.saveClient(data);
		data.setEmail("test2");
		managerService.saveClient(data);
		
		List<Client> clients = managerService.getAllClients();
		
		
		boolean ifIn1 = false;
		boolean ifIn2 = false;
		
		
		for(Client cl: clients) {
			if (cl.getEmail().equals("test")) {
				ifIn1 = true;
			}
			if (cl.getEmail().equals("test2")) {
				ifIn2 = true;
			}
		}
		
		
		assertTrue(ifIn1 && ifIn2);
		
		
	}
	
	@Test
	void testGetAllInvoices() {
		
		Data data = new Data();
		data.setEmail("test");
		data.setAmmount("1.2");
		data.setVAT("1.2");
		data.setTotalAmmount("1.2");
		data.setInvoiceNumber("TF-TEST");
		managerService.saveInvoice(data);
		data.setInvoiceNumber("TF-TEST2");
		managerService.saveInvoice(data);
		
		List<Invoice> invoices = managerService.getAllInvoices();
		
		boolean ifIn1 = false;
		boolean ifIn2 = false;
		
		
		for(Invoice inv: invoices) {
			if(inv.getInvoiceNumber().equals("TF-TEST")) {
				ifIn1 = true;
			}
			if(inv.getInvoiceNumber().equals("TF-TEST2")) {
				ifIn2 = true;
			}
		}
		
		
		
	}
	
	@Test
	void deleteClientDatabase() {
		
		//store to database 
		Data data = new Data();
		data.setEmail("test");
		managerService.saveClient(data);
		
		//get from database 
		Client client = managerService.getClient(data.getEmail());
		
		//delete from database 
		managerService.deleteClientFromDatabase(client);
		
		//check 
		List<Client> clients = managerService.getAllClients();
		boolean check = true;
		
		for(Client cl: clients) {
			if(cl.getEmail().equals(data.getEmail())) {
				check = false;
			}
		}
		
		assertTrue(check);
		
		
		
	}
	
	@Test
	void testExportCSV() {
		Data data = new Data();
		data.setType("test");
		data.setSource("test");
		data.setDate("test");
		data.setClientName("test");
		data.setEmail("test");
		data.setCompany("test");
		data.setAddress("test");
		data.setPhone("test");
		data.setServiceInterest("test");
		data.setAmmount("test");
		data.setVAT("test");
		data.setTotalAmmount("test");
		data.setInvoiceNumber("test");
		data.setPriority("test");
		data.setMessage("test");
		data.setBodyMail("test");
		
		List<Data> dataList = new ArrayList<Data>();
		dataList.add(data);
		
		ByteArrayInputStream check =  managerService.exportToCsv(dataList);
		
		assertTrue(check!=null);
		
	}
	
	
	@Test
	void testExportXLS() {
		Data data = new Data();
		data.setType("test");
		data.setSource("test");
		data.setDate("test");
		data.setClientName("test");
		data.setEmail("test");
		data.setCompany("test");
		data.setAddress("test");
		data.setPhone("test");
		data.setServiceInterest("test");
		data.setAmmount("test");
		data.setVAT("test");
		data.setTotalAmmount("test");
		data.setInvoiceNumber("test");
		data.setPriority("test");
		data.setMessage("test");
		data.setBodyMail("test");
		
		List<Data> dataList = new ArrayList<Data>();
		dataList.add(data);
		
		ByteArrayInputStream check =  managerService.exportToXls(dataList);
		
		assertTrue(check!=null);
		
	}
	
	@Test
	void testSort() {
		
		//sort by name
		
		List<Data> notSorted = new ArrayList<Data>();
		Data data1 = new Data();
		data1.setClientName("B");
		notSorted.add(data1);
		Data data2 = new Data();
		data2.setClientName("C");
		notSorted.add(data2);
		Data data3 = new Data();
		data3.setClientName("A");
		notSorted.add(data3);
		
		List<Data> sorted = managerService.sort(notSorted, "name");
		
		assertTrue(
					sorted.get(0).getClientName().equals(data3.getClientName())
					&& sorted.get(1).getClientName().equals(data1.getClientName())
					&& sorted.get(2).getClientName().equals(data2.getClientName())
				);
		
		
		
		 ;
	}
	
	@Test
	void testSortByDate() {
		
		//sort by name
		
		List<Data> notSorted = new ArrayList<Data>();
		Data data1 = new Data();
		data1.setDate("2025-03-25");
		notSorted.add(data1);
		Data data2 = new Data();
		data2.setDate("2025-03-27");
		notSorted.add(data2);
		Data data3 = new Data();
		data3.setDate("2025-03-01");
		notSorted.add(data3);
		
		List<Data> sorted = managerService.sort(notSorted, "date");
		
		assertTrue(
					sorted.get(0).getDate().equals(data3.getDate())
					&& sorted.get(1).getDate().equals(data1.getDate())
					&& sorted.get(2).getDate().equals(data2.getDate())
				);
		
		
		
		
	}

}
