package com.example.GenAI.DaoTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
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

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ClientDaoTests {

	@Autowired
	ClientDAO clientDAO;
	
	
	@Test
	void clientWithSameEmailTest() {
		
		String chEmail = "mail@test.gr";
		
		Client client = new Client();
		client.setEmail(chEmail);
		clientDAO.save(client);
		
		List<Client> checkClients = clientDAO.findByEmail(chEmail);
		
		System.out.println("CheckClients Size: "+ checkClients.size());
		
		assertTrue(checkClients.size()!=0);
		
	}
	

}
