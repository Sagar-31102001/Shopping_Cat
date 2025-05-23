package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.UserDtls;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer>
{
	public abstract boolean existsByEmail(String email);
	
	public abstract  UserDtls findByEmail(String name);
}
