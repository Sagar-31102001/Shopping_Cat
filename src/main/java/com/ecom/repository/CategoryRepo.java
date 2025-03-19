package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer>
{
	public abstract boolean existsByName(String name);
}
