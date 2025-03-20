package com.ecom.service_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.IService.ProductService;
import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService
{

	@Autowired
	private ProductRepository prdRepo;
	
	@Override
	public Product saveProduct(Product product) 
	{
		return prdRepo.save(product);
	}

	@Override
	public List<Product> getAllProducts(String category)
	{
			return prdRepo.findByCategory(category);
	}

	@Override
	public List<Product> getAllProducts() 
	{
		
		return prdRepo.findAll();
	}
	
	@Override
	public Boolean deleteProduct(Integer id)
	{
	
		Product product = prdRepo.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(product))
		{
			prdRepo.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(Integer id) 
	{
		return prdRepo.findById(id).orElse(null);
	}

	@Override
	public boolean existProduct(String title) 
	{
		return prdRepo.existsByTitle(title);
	}

	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize)
	{
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		return prdRepo.findAll(pageable);
	}

	

}
