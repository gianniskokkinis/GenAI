package com.example.GenAI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="requests")
public class Request {

	@Id
	@Column(name="request_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int ID;
	
	@Column(name="date")
	private String date;
	
	@Column(name="priority")
	public String priority ;
	
	@Column(name="important_info")
	public String importantInfo;
	
	@Column(name="mail_body")
	public String mailBody;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="client_id")
	private Client client;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getImportantInfo() {
		return importantInfo;
	}

	public void setImportantInfo(String importantInfo) {
		this.importantInfo = importantInfo;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
	
	
}
