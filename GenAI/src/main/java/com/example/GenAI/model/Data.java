package com.example.GenAI.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


public class Data {
	
	
	private String type;
	
	
	private String source;
	
	
	private String date;
	
	private Date sortDate;
	
	
	private String clientName;
	
	
	private String email;
	
	
	private String company;
	
	private String address;
	

	private String phone;
	

	private String serviceInterest;
	
	private String ammount;
	
	
	private String VAT;
	
	private String totalAmmount;
	
	private String invoiceNumber;
	
	private String priority;
	
	private String Message;
	
	private boolean warning;
	
	private String bodyMail;
	
	private String error; //in case something wrong happend
	
	
	public Data() {
		this.type="";
		this.source="";
		this.date="";
		this.clientName="";
		this.email="";
		this.company="";
		this.address="";
		this.phone="";
		this.serviceInterest="";
		this.ammount="";
		this.VAT="";
		this.totalAmmount="";
		this.invoiceNumber="";
		this.priority="";
		this.Message="";
		this.error="";
		this.warning=false;
		this.bodyMail="";
	}
	
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getServiceInterest() {
		return serviceInterest;
	}

	public void setServiceInterest(String serviceInterest) {
		this.serviceInterest = serviceInterest;
	}

	public String getAmmount() {
		return ammount;
	}

	public void setAmmount(String ammount) {
		this.ammount = ammount;
	}

	public String getVAT() {
		return VAT;
	}

	public void setVAT(String vAT) {
		VAT = vAT;
	}

	public String getTotalAmmount() {
		return totalAmmount;
	}

	public void setTotalAmmount(String totalAmmount) {
		this.totalAmmount = totalAmmount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}




	public String getError() {
		return error;
	}




	public void setError(String error) {
		this.error = error;
	}




	public boolean getWarning() {
		return warning;
	}




	public void setWarning(boolean warning) {
		this.warning = warning;
	}




	public String getBodyMail() {
		return bodyMail;
	}




	public void setBodyMail(String bodyMail) {
		this.bodyMail = bodyMail;
	}




	public Date getSortDate() {
		return sortDate;
	}




	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}
	
	
	
	
	
	

}
