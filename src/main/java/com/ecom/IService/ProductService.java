package com.ecom.IService;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.model.Product;

public interface ProductService 
{
	public Product saveProduct(Product product);
	
	public abstract List<Product> getAllProducts(String category);
	
	public abstract List<Product> getAllProducts();
	
	public Boolean deleteProduct(Integer id);
	
	public Product getProductById(Integer id);
	
	public abstract boolean existProduct(String prdName);
	
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);
	
}
