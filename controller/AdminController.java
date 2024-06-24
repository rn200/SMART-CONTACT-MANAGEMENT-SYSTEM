package com.smart.contact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.contact.Entity.User;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String adminHome() {
		return"admin/home";
	}
	
	@GetMapping("/showuser")
	public String getUserDetails(@ModelAttribute User user,Model model) {
		 List<User> byRole = this.userRepository.findByRole("ROLE_USER");
		model.addAttribute("users", byRole);
		return "admin/show_user";
	}
	
	@GetMapping("/{id}/delete")
	public String deleteUser(@PathVariable("id") int id,Model model,HttpSession session) {
		User user = this.userRepository.findById(id);
		this.userRepository.delete(user);
		session.setAttribute("message", new Message("User Deleted Successfully .","success"));
		return "redirect:/admin/showuser";
	}
}
