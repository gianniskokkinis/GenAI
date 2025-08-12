package com.example.GenAI.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.*;

@Entity
@Table(name="clients")
public class Client {
	
	@Id
	@Column(name="client_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="company")
	private String company;
	
	@Column(name="interest_services")
	private String interestServices;
	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="client_id")
	private List<Invoice> invoice;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="client_id")
	private List<Request> requests;

	public List<Invoice> getInvoice() {
		return invoice;
	}

	public void setInvoice(List<Invoice> invoice) {
		this.invoice = invoice;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getInterestServices() {
		return interestServices;
	}

	public void setInterestServices(String interestServices) {
		this.interestServices = interestServices;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
	
	
	
	
	
}
