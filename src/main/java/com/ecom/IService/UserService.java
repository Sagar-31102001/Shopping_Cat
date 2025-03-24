package com.ecom.IService;

import com.ecom.model.UserDtls;

public interface UserService 
{
	public abstract UserDtls saveUser(UserDtls userDtls);
	
	public abstract boolean existsUser(String email);
	
	public abstract UserDtls getUserByEmail(String email);
}
