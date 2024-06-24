package com.smart.contact.controller;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.swing.JPopupMenu.Separator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.contact.Entity.Contact;
import com.smart.contact.Entity.User;
import com.smart.contact.dao.ContactRepository;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserDashboardController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//method to add common data too response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userName = principal.getName();
		User user = userRepository.getUserByName(userName);
		model.addAttribute("user",user);
		model.addAttribute("welcomeUser","welcome");
	}
	@GetMapping("/index")
	public String dashboard(Model model,Principal principal) {
		model.addAttribute("title","your dashboard");
		
		return "normal/dashboard";
	}
	
	//Add-contact handler
@GetMapping("/add-contact-form")	
public String addContactForm(Model model,Principal principal) {
	model.addAttribute("title","add-contact");
	model.addAttribute("contact",new Contact());
	return "normal/add_contact_form";
}

@PostMapping("/process-contact")
public String addFormHandler( @ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal, Model model,HttpSession session) {
	 
	try {
		 
		  //added to data base with proper user id
		  
		  String name = principal.getName();
		  User user = this.userRepository.getUserByName(name);
		  
		  //add file
		  if(file.isEmpty()) {
			  System.out.println("file is empty");
			  contact.setImage("contact.png");
		  }
		  else {
			  contact.setImage(file.getOriginalFilename());
			  
			  File file2 = new ClassPathResource("static/image").getFile();
			  
			  Path path = Paths.get(file2.getAbsolutePath()+ File.separator+file.getOriginalFilename());
			  
			  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
		      System.out.println("Image is uploaded");
		  }
		  
		  contact.setUser(user);
		  user.getContacts().add(contact);
		  this.userRepository.save(user);
		  System.out.println("data added to data base");
		  System.out.println(contact);
		 
		  //return displaying message
		  
		  session.setAttribute("message", new Message("Contact Added Successfully","success"));
		  model.addAttribute("session",session);
		return "normal/add_contact_form";
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		e.printStackTrace();
		session.setAttribute("message", new Message("please allows terms and conditions .something went wrong!!","danger"));
		model.addAttribute("session",session);
		return "normal/add_contact_form";
	}
	
}
// current page 0 {page}
//contact per page 5
@GetMapping("/show-contacts/{page}")
public String showContacts(@PathVariable("page") Integer page, Model m,Principal principal) {
	m.addAttribute("title","show-contact-page");
	String userName = principal.getName();
	User user = this.userRepository.getUserByName(userName);
	
	// current page 0{page}
	//contact per page 5           pagination
	Pageable pageable = PageRequest.of(page, 2);
	Page<Contact> contacts= this.contactRepository.getContactsByUserNameContacts(user.getId(),pageable);
	
	m.addAttribute("contacts",contacts);
	m.addAttribute("currentPage",page);
	m.addAttribute("totalPage",contacts.getTotalPages());
	return "normal/show_contacts";
}

// make email as a link and create a user friendly frontend
@GetMapping("/contacts/{cid}")
public String contactEmailHandler(@PathVariable("cid") Integer cid,Model model,Principal principal) {
	     
	    Optional<Contact> contactOptional = this.contactRepository.findById(cid);
	    Contact contact = contactOptional.get();
	     
	    String userName = principal.getName();
	    User user = this.userRepository.getUserByName(userName);
	    
	       if(user.getId()==contact.getUser().getId()) {
	    	   
	    	   model.addAttribute("contact",contact);
	    	   model.addAttribute("title",contact.getName());
	    	   
	       }
	   
	    
	return "normal/contact_email_form";
}

//delete contact from show_contacts
@GetMapping("/delete/{cid}")
public String deleteControllerHandler(@PathVariable("cid") Integer cid,Model model) {
	
	Optional<Contact> contactOptional = this.contactRepository.findById(cid);
	Contact contact = contactOptional.get();
			
	contact.setUser(null);
	this.contactRepository.delete(contact);
	     
//	session.setAttribute("message", new Message("contact deleted successfully","alert-success"));     
//	model.addAttribute("session",session);     
	return "redirect:/user/show-contacts/0";
	
}
// update form
@PostMapping("/update/{cid}")
public String contactUpdateForm(@PathVariable("cid") Integer cid, Model model) {
	model.addAttribute("title","update-form");
	Contact contact = this.contactRepository.findById(cid).get();
	model.addAttribute("contact",contact);
	
	return "normal/contact_update_form";
	
}
//update form-handler
@PostMapping("/update-contact")
public String updateFormHandler(@ModelAttribute Contact contact,
		@RequestParam("profileImage") MultipartFile file,  
		Model model,Principal principal,
		HttpSession session) {
	
	    Contact oldContactDetails = this.contactRepository.findById(contact.getCid()).get();
	
	try {	  
		  //add file
		  if(!file.isEmpty()) {
			  
             File saveFile = new ClassPathResource("static/image").getFile();
			  
			  Path path = Paths.get(saveFile.getAbsolutePath()+ File.separator+file.getOriginalFilename());
			  
			  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
			  contact.setImage(file.getOriginalFilename());
			  
		  }	
		  else {
			  contact.setImage(oldContactDetails.getImage());
		  }
			User user = this.userRepository.getUserByName(principal.getName()); 			
		    contact.setUser(user);
            
		    //updating contact
		    this.contactRepository.save(contact);
	        
		    // send proper message to view
		    
		    session.setAttribute("message",new Message("Contact Updated Successfully","success") );
		 
		 
		  //return displaying message		  	
		return "redirect:/user/show-contacts/0";
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
		e.printStackTrace();
		
	}
	return "redirect:/user/show-contacts/0";	
}

//profile handler form
@GetMapping("/add-profile")
public String profileHandlerForm(Model model,Principal principal) {
	model.addAttribute("title","your-profile");
	String username = principal.getName();
	User user = this.userRepository.getUserByName(username);
	model.addAttribute("user",user);
	return "normal/profile";
	
}

//open settings handler
@GetMapping("/settings")
public String openSettings() {
	return "normal/settings";
}

//change password field

@PostMapping("/new-password")
public String changeSettings(@RequestParam("oldPassword") String oldPassword,
		@RequestParam("newPassword") String newPassword,
		HttpSession session,
		Principal principal) {
	
	String userName = principal.getName();
	User currentUser = this.userRepository.getUserByName(userName);
	
	if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
		currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(currentUser);
		session.setAttribute("message", new Message("Password Changed !","success"));
		return "redirect:/user/index";
		
	}
	else {
		//error
		session.setAttribute("message", new Message("Enter correct old password","success"));
	}
	
	return "redirect:/user/settings";
}

}



