package com.example.GenAI.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GenAI.model.User;

@Repository
public interface UserDAO extends JpaRepository<User,Integer>{
	
	Optional<User> findByUsername(String username);
}
