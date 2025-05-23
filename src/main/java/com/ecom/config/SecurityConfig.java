package com.ecom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig 
{
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsServiceImpl();
	}
	
	@Autowired
	private AuthenticationSuccessHandler handler; 
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		
		httpSecurity.csrf(customizer->customizer.disable())
							.authorizeHttpRequests(	req->req
							.requestMatchers("/admin/**").hasRole("ADMIN")
							.requestMatchers("/user/**").hasRole("USER")
							.requestMatchers("/**").permitAll()
							.anyRequest().authenticated()
										).formLogin(form->form.loginPage("/signin")
												.loginProcessingUrl("/login")
												//.defaultSuccessUrl("/"))
												.successHandler(handler)
										)
										.logout(logout->logout.permitAll());
		
		
		return httpSecurity.build();
		
	}
}
