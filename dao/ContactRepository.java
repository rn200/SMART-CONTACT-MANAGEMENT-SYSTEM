package com.smart.contact.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contact.Entity.Contact;
import com.smart.contact.Entity.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// current page 0{page}
	//contact per page 5
	@Query("From Contact as c Where c.user.id= :userId")
	public Page<Contact> getContactsByUserNameContacts(@RequestParam("userId") int userId,Pageable pageable);

//search
	public List<Contact> findByNameContainingAndUser(String name,User user);

}
