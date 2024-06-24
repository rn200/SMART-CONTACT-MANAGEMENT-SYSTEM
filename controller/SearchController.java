package com.smart.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.contact.Entity.Contact;
import com.smart.contact.Entity.User;
import com.smart.contact.dao.ContactRepository;
import com.smart.contact.dao.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	@GetMapping("/search/{query}")
	 public ResponseEntity<?> handleSearchFunstionality(@PathVariable("query") String query, Principal principal){
		 
		User user = this.userRepository.getUserByName(principal.getName());
		
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	 }
}
