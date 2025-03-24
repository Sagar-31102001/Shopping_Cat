package com.ecom.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecom.model.UserDtls;

@SuppressWarnings("serial")
public class CustomUser implements UserDetails
{

	private UserDtls userDtls;
	
	
	public CustomUser() {
		super();
	}

	public CustomUser(UserDtls userDtls) 
	{
		super();
		this.userDtls = userDtls;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userDtls.getRole());
		return Arrays.asList(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userDtls.getPassword();
	}

	@Override
	public String getUsername() {
		
		return userDtls.getEmail();
	}
	
	@Override
	public boolean isAccountNonLocked() 
	{
		return true;
	}
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() 
	{
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		
		return true;
	}
}
