package com.example.GenAI.model;

import java.sql.Date;

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
@Table(name="invoices")
public class Invoice {

	@Id
	@Column(name="invoice_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@Column(name="invoice_number")
	private String invoiceNumber;
	
	@Column(name="date")
	private String date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="client_id")
	private Client client;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="fpa")
	private double fpa;

	@Column(name="total_ammount")
	private double totalAmount;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getFpa() {
		return fpa;
	}

	public void setFpa(double fpa) {
		this.fpa = fpa;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	
	
	
	
}
