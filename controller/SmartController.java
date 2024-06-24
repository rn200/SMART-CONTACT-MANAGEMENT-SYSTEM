package com.smart.contact.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contact.Entity.User;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
public class SmartController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/")
	
public String home(Model model) {
		 model.addAttribute("title","home-smart contact manager");
	return "home";
}
	
	@GetMapping("/about")	
public String about(Model model) {
    model.addAttribute("title","about-smart contact manager");
	return "about";
}
	
	@GetMapping("/signup")	
public String signup( Model model) {
	
    model.addAttribute("title","sign up-smart contact manager");
    model.addAttribute("user",new User());
	return "signup";
}
	
	@PostMapping("/do-register")
	public String signUpPageHandler(@Valid  @ModelAttribute("user") User user ,BindingResult result1,
			 @RequestParam(value="agreement", defaultValue= "false") boolean agreement ,Model model,
			HttpSession session) {
		
		try {
			if(result1.hasErrors()) {
//				Validator.validate(result1,user); 
				model.addAttribute("user",user);
				 System.out.println(result1.toString());
				return "signup";
			}
           
			if(!agreement) {
				System.out.println("please allows terms and conditions to continue !!");
			    throw new Exception();
			}
		
			System.out.println("agreement"+agreement);
			System.out.println("user"+user);
			
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			user.setImageUrl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result = this.userRepository.save(user);
			model.addAttribute("user",result);
			
			session.setAttribute("message", new Message("User Registered Successfully .","alert-success"));
			model.addAttribute("session",session);
			return "signup";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("please allows terms and conditions .something went wrong!!","alert-danger"));
			model.addAttribute("session",session);
			return "signup";
		}
	}
	
	@GetMapping("/signin")
	public String signin(Model model) {
		model.addAttribute("title","welcome to sign in page");
		return "signin";
	}
	
}
	

