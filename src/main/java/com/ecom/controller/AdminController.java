package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.service_impl.CategoryServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController 
{
	@Autowired
	private CategoryServiceImpl serviceImpl;
	
	@GetMapping("/")
	public String index()
	{
		return "admin/index";
	}
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct()
	{
		return "admin/add_product";
	}
	
	@GetMapping("/category")
	public String addCategory(Model m)
	{
		m.addAttribute("category", serviceImpl.getAllCategory());
		return "admin/category";
	}
	
	@GetMapping("/viewproduct")
	public String viewProducts()
	{
		return "admin/products";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category,@RequestParam MultipartFile file,HttpSession session) throws IOException
	{
		String imageName= file !=null ? file.getOriginalFilename() : "default.jpg";
		category.setImagename(imageName);
		
		if (serviceImpl.existCategory(category.getName())) 
		{
			session.setAttribute("errorMsg", "Category Already Exists...!!");
		} 
		else
		{
			System.out.println(category);
			Category saveCategory = serviceImpl.saveCategory(category);
			if (ObjectUtils.isEmpty(saveCategory)) 
			{
				session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
			} 
			else 
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+imageName);
				
				System.out.println(path);
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
				session.setAttribute("succMsg", "Saved Successfully");
			}
			
		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id,HttpSession session)
	{
		boolean value = serviceImpl.deleteCategory(id);
		
		if(value)
		{
			session.setAttribute("succMsg", "Delete Successfully...");
		}
		else
		{
			session.setAttribute("errorMsg", "Something Went Wrong...");
		}
		return "redirect:/admin/category";
	}
	
	
	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id ,Model m)
	{
		Category category = serviceImpl.getCategory(id);
		m.addAttribute("category", category);
		return "admin/edit_category";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam MultipartFile file,HttpSession session)
	{
		System.out.println("AdminController.updateCategory()");
		return null;
	}

	
	
}
