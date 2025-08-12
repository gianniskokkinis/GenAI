package com.example.GenAI.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GenAI.model.Invoice;

@Repository
public interface InvoiceDAO extends JpaRepository<Invoice,Integer>{
	
	public Invoice findById(int id);
	
}
