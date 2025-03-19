package com.ecom.IService;

import java.util.List;

import com.ecom.model.Category;

public interface CategoryService 
{
	public abstract Category saveCategory(Category category);
	
	public List<Category> getAllCategory();
	
	public abstract boolean existCategory(String CategoryName);
	
	public abstract boolean deleteCategory(int id);
	
	public abstract Category getCategory(int id);
}
