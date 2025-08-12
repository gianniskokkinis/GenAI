package com.example.GenAI.ModelsTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.example.GenAI.DAO.ClientDAO;
import com.example.GenAI.Services.FileService;
import com.example.GenAI.Services.ManagerService;
import com.example.GenAI.model.Client;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.Invoice;
import com.example.GenAI.model.Request;
import com.example.GenAI.model.User;

import jakarta.transaction.Transactional;

@SpringBootTest
class ModelsTests {

	@Test
	void testUser() {
		User user = new User();
		user.setUsername("test");
		user.setPassword("test");
		
		assertTrue(user.getUsername().equals("test") && user.getPassword().equals("test"));
		
	}
	
	@Test
	void testClient() {
		Client client = new Client();
		client.setName("test");
		client.setEmail("test");
		client.setPhone("test");
		client.setCompany("test");
		client.setInterestServices("test");
		
		assertTrue( 
					client.getName().equals("test")
					&& client.getEmail().equals("test")
					&& client.getPhone().equals("test")
					&& client.getCompany().equals("test")
					&& client.getInterestServices().equals("test")
				);
		
	}
	
	@Test 
	void testInvoice() {
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber("test");
		invoice.setDate("test");
		invoice.setAmount(1.2);
		invoice.setFpa(1.2);
		invoice.setTotalAmount(1.2);
		
		assertTrue(
					invoice.getInvoiceNumber().equals("test")
					&& invoice.getDate().equals("test")
					&& (invoice.getAmount()==1.2)
					&& (invoice.getFpa()==1.2)
					&& (invoice.getTotalAmount()==1.2)
				);
		
	}
	
	@Test
	void testData() {
		
		Date date = new Date();
		
		Data data = new Data();
		data.setType("test");
		data.setSource("test");
		data.setDate("test");
		data.setSortDate(date);
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
		data.setWarning(true);
		data.setBodyMail("test");
		data.setError("test");
		
		assertTrue(
				data.getType().equals("test")
				&& data.getSource().equals("test")
				&& data.getDate().equals("test")
				&& data.getSortDate()==date
				&& data.getClientName().equals("test")
				&& data.getEmail().equals("test")
				&& data.getCompany().equals("test")
				&& data.getAddress().equals("test")
				&& data.getPhone().equals("test")
				&& data.getServiceInterest().equals("test")
				&& data.getAmmount().equals("test")
				&& data.getVAT().equals("test")
				&& data.getTotalAmmount().equals("test")
				&& data.getInvoiceNumber().equals("test")
				&& data.getPriority().equals("test")
				&& data.getMessage().equals("test")
				&& data.getWarning()
				&& data.getBodyMail().equals("test")
				&& data.getError().equals("test")
				
				);
		
	}
	
	@Test
	void testRequest() {
		Request req = new Request();
		req.setDate("test");
		req.setPriority("test");
		req.setImportantInfo("test");
		req.setMailBody("test");
		
		assertTrue(
					req.getDate().equals("test")
					&& req.getPriority().equals("test")
					&& req.getImportantInfo().equals("test")
					&& req.getMailBody().equals("test")
				);
		
	}
	
	
	
	

}
