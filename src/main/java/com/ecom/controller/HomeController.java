package com.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service_impl.CategoryServiceImpl;
import com.ecom.service_impl.ProductServiceImpl;

@Controller
public class HomeController 
{

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@GetMapping("/")
	public String index()
	{
		 return "index";
	}
	
	@GetMapping("/login")
	public String login()
	{
		return "login";
	}
	
	@GetMapping("/register")
	public String register()
	{
		return "register";
	}
	
	@GetMapping("/products")
	public String products(Model m,@RequestParam(defaultValue = "") String category)
	{
		List<Category> allCategory = categoryServiceImpl.getAllCategory();
		
		
		m.addAttribute("categories", allCategory);
		
		
		if (category.length()==0) 
		{
			List<Product> allProducts = productServiceImpl.getAllProducts();
			m.addAttribute("products", allProducts);
		} 
		else 
		{
			 List<Product> allProducts = productServiceImpl.getAllProducts(category);
			 m.addAttribute("products", allProducts);
			 m.addAttribute("paramValue", category);
			 m.addAttribute("paramValue1", category.length());
		}
		
		return "product";
	}
	
	@GetMapping("/viewproduct/{id}")
	public String product(@PathVariable int id,Model m)
	{
		Product product = productServiceImpl.getProductById(id);
		System.out.println(product);
		m.addAttribute("product", product);
		return "view_product";
	}
}
