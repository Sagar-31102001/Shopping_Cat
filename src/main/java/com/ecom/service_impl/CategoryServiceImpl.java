package com.ecom.service_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.IService.CategoryService;
import com.ecom.model.Category;
import com.ecom.repository.CategoryRepo;

@Service
public class CategoryServiceImpl implements CategoryService
{
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public Category saveCategory(Category category) 
	{
		return categoryRepo.save(category);
	}

	@Override
	public List<Category> getAllCategory()
	{
		return categoryRepo.findAll();
	}

	@Override
	public boolean existCategory(String CategoryName) 
	{
		return categoryRepo.existsByName(CategoryName);
	}

	@Override
	public boolean deleteCategory(int id) 
	{
		Category category = categoryRepo.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(category))
		{
			categoryRepo.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategory(int id)
	{
		Category category = categoryRepo.findById(id).orElse(null);
		return category;
	}
	
}
