package com.smart.contact.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.contact.Entity.User;


public interface UserRepository extends JpaRepository<User,Integer>{
	@Query("select u from User u where u.email= :email")
public User getUserByName(@Param("email") String email);

	public List<User> findByRole(String role) ;
	
	public User findById(int id);
}
