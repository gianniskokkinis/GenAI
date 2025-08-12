package com.example.GenAI.Services;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GenAI.model.Client;
import com.example.GenAI.model.Data;
import com.example.GenAI.model.Invoice;
import com.example.GenAI.model.Request;

@Service
public interface ManagerService {

	/*Collect Data from files*/
	public List<Data> readData(List<String> filePaths);
	
	/*to void repeat data*/
	public List<Data> fixData(List<Data> newData, List<Data> historyData);
	
	/*To save client to database*/
	public void saveClient(Data data);
	
	/*Update client, case add more requests*/
	public void updateClient(Client client, Data data);
	
	/*Save invoice to database*/
	public void saveInvoice(Data data);
	
	/*Check if Client already in database*/
	public boolean isClientExists(String email);
	
	/*Get Client from database*/
	public Client getClient(String email);
	
	/*Delete Data from list*/
	public List<Data> deleteData(String sourceName, List<Data> historyData);
	
	/*Get All clients to display*/
	public List<Client> getAllClients();
	
	/*Get All invoices to display*/
	public List<Invoice> getAllInvoices();
	
	/*Delete Client from database*/
	public void deleteClientFromDatabase(Client client);
	
	/*Delete Invoice from database*/
	public void deleteInvoiceFromDatabase(Invoice invoice);
	
	/*Export extracted data to CSV file*/
	public ByteArrayInputStream exportToCsv(List<Data> data);
	
	/*Export extracted data to XLS file*/
	public ByteArrayInputStream exportToXls(List<Data> data);
	
	/*Sort Collected data by filter*/
	public List<Data> sort(List<Data> data, String filter);
	
	/*Search Data by keyword*/
	public List<Data> search(List<Data> data, String keyword);
	
	
	
	
	
	
	
	
	
	
}