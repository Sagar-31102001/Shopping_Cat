package com.ecom.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.IService.UserService;
import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public UserDtls saveUser(UserDtls userDtls) 
	{
		userDtls.setRole("ROLE_USER");
		String encode = encoder.encode(userDtls.getPassword());
		userDtls.setPassword(encode);
		
		return userRepo.save(userDtls);
	}

	@Override
	public boolean existsUser(String email) 
	{
		return userRepo.existsByEmail(email) ? true : false;
	}

	@Override
	public UserDtls getUserByEmail(String email)
	{
		return userRepo.findByEmail(email);
	}

}
