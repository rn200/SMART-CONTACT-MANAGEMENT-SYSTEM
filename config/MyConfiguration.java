package com.smart.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.smart.contact.Entity.User;

@Configuration
@EnableWebSecurity
public class MyConfiguration {
	
	@Autowired
	private AuthSuccessHandlerImpl authSuccessHandlerImpl;
	
	@Bean
    public User user() {
        return new User(); // You may need to pass necessary arguments here
    }

	@Bean
	public UserDetailsService getuserDetailsService() {
		
		return new UserDetailsServiceImpl();
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getuserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	//configure method
	// This is what I need to rewrite
	
	 protected AuthenticationManagerBuilder configure(AuthenticationManagerBuilder auth) {
	   return auth.authenticationProvider(authenticationProvider());
	 }
	 @Bean
	 protected DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception 
	  {
		 
		 http.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
			.authorizeHttpRequests(req->req.requestMatchers("/user/**").hasRole("USER")
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.requestMatchers("/**").permitAll())
			        .formLogin(form->form.loginPage("/signin")
			        		.loginProcessingUrl("/dologin")
			        //		.defaultSuccessUrl("/")
			        		.successHandler(authSuccessHandlerImpl))
			        .logout(logout->logout.permitAll());
		 
//		 http
//         .authorizeHttpRequests()
//             .requestMatchers("user/**").hasRole("USER")
//             .requestMatchers("admin/**").hasRole("ADMIN")
//             .requestMatchers("/**").permitAll()
//             .and()
//         .formLogin()
//             .loginPage("/signin")
//             .loginProcessingUrl("/dologin");
           //  .defaultSuccessUrl("/user/index");
        
		 
		 return http.build();
	 	}
	 
	 }

