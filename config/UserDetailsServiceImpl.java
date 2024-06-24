package com.smart.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.contact.Entity.User;
import com.smart.contact.dao.UserRepository;

public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	 private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByName(username);
		if(user==null) {
			throw new UsernameNotFoundException("could not found user");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}

}
