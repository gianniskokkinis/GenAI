package com.example.GenAI.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GenAI.model.Client;



@Repository
public interface ClientDAO extends JpaRepository<Client, Integer>{
	
	public Client findById(int id);
	
	@Query(value="SELECT * FROM clients WHERE email=:companyName", nativeQuery=true)
	public List<Client> findByEmail(@Param("companyName") String chEmail);
	
}
