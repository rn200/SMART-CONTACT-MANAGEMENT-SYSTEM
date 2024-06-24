package com.smart.contact.controller;

import java.security.Principal;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contact.Entity.User;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.Message;
import com.smart.contact.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {
	
    @Autowired
	private EmailService emailService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	Random random = new Random(1000);
	
	@GetMapping("/forgot-password")
	public String forgotPassword(Model model) {
		model.addAttribute("title","Register Your Email");
		return "forgot_password_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email,
			HttpSession session,
			Model model
			) throws AddressException, MessagingException {
		
	User user = this.userRepository.getUserByName(email);
	
	  if(user!=null) {
		  
		  System.out.println(email);
			//generating random otp
				
			int otp = random.nextInt(999999);
			System.out.println(otp);
			
			//sending otp to email
			
		        String host = "smtp.gmail.com";
		        String port = "587";
		        String mailFrom = "rn.kalikapur@gmail.com";
		        String password = "iihwxialhsvnknoe";

		        // Outgoing message information
		        String mailTo = email.toLowerCase();
		        String subject="OTP from smart contact management system";
		        String message=" OTP ="+otp;
			boolean flag=this.emailService.sendEmail( host,  port, mailFrom, password,  mailTo,
	                 subject, message);
			
			if(flag) {
				
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
                model.addAttribute("title","verify-otp-page");
				return "verify_otp";
			}
			else {
				session.setAttribute("message", "Please check your email id");
				return "forgot_password_form";
			}
		  
	  }
	  else {
		  session.setAttribute("message", "User doesn't exist!");
		  return "forgot_password_form";
	  }
					
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp")int otp,HttpSession session,Model model) {
		
		int myotp = (int) session.getAttribute("myotp");
		
		if(myotp==otp) {
		        model.addAttribute("title","change password");
				return "new_password";
						
		}
		else {
			
			session.setAttribute("message", "you have entered wrong OTP");
			
			return "verify_otp";
		}
			
	}
	
	
	//change password after forgetting old password
	
	@PostMapping("/change-password")
	public String getNewPasswordChange(Principal principal,
			@RequestParam("getnewpassword") String getnewpassword,
			HttpSession session) {
		
//		String userName = principal.getName();
		String email = (String) session.getAttribute("email");
		User currentUser = this.userRepository.getUserByName(email);
		currentUser.setPassword(this.bCryptPasswordEncoder.encode(getnewpassword));
		this.userRepository.save(currentUser);
		session.setAttribute("message","Password Changed !");
		
		return "redirect:/signin";
	}
	
}
