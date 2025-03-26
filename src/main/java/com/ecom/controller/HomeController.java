package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service_impl.CartServiceImpl;
import com.ecom.service_impl.CategoryServiceImpl;
import com.ecom.service_impl.ProductServiceImpl;
import com.ecom.service_impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CartServiceImpl cartService;
	
	@GetMapping("/")
	public String index(Model m)
	{
		List<Category> categories = categoryServiceImpl.getAllCategory();
		m.addAttribute("categories", categories);
		 return "index";
	}
	
	// When Request Come This Controller Then This Method Automatically Called 
	// Get User Details
	@ModelAttribute
	public void getUserDetails(Principal principal,Model m)
	{
		if(principal!=null)
		{
			String email = principal.getName();
			UserDtls user = userService.getUserByEmail(email);
			m.addAttribute("user", user);
			
			int countCart = cartService.getCountCart(user.getId());
			m.addAttribute("count", countCart);
		}
	}
	
	@GetMapping("/signin")
	public String login() {
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
	
	@GetMapping("/specificCategory")
	public String specificCategory(Model m,@RequestParam String category)
	{
		List<Product> allProducts = productServiceImpl.getAllProducts(category);
		m.addAttribute("products", allProducts);
		return "specific_category";
	}
	
	@GetMapping("/viewproduct/{id}")
	public String product(@PathVariable int id,Model m)
	{
		Product product = productServiceImpl.getProductById(id);
		System.out.println(product);
		m.addAttribute("product", product);
		return "view_product";
	}
	
	// Start User Operation
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		Boolean existsEmail = userService.existsUser(user.getEmail());

		if (existsEmail) {
			session.setAttribute("errorMsg", "Email already exist");
		} else {
			String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
			user.setProfileImage(imageName);
			UserDtls saveUser = userService.saveUser(user);

			if (!ObjectUtils.isEmpty(saveUser)) {
				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();

					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +"profile_img"+ File.separator
							+ file.getOriginalFilename());

					System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Register successfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		}

		return "redirect:/register";
	}
}
